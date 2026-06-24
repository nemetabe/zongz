package com.nemetabe.zongz.infrastructure;

import com.nemetabe.zongz.domain.port.FileStore;
import com.nemetabe.zongz.domain.track.AudioFormat;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * implements FileStore by writing audio files to a configured local directory.
 * lives in infrastructure — the only place allowed to know about disk I/O.
 *
 * spring types (MultipartFile) are handled upstream in IngestionService,
 * which extracts the InputStream before calling here.
 */
@Component
public class DiskFileStore implements FileStore {

    private static final Logger log = LoggerFactory.getLogger(DiskFileStore.class);

    private final Path rootLocation;

    public DiskFileStore(AudioStoreProperties properties) {
        String dir = properties.getUploadDir();
        if (dir == null || dir.isBlank()) {
            throw new IllegalArgumentException("audio.store.upload-dir must not be blank");
        }
        this.rootLocation = Paths.get(dir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            log.info("Audio file store initialised at: {}", rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialise storage directory: " + rootLocation, e);
        }
    }

    @Override
    public Path store(InputStream inputStream, AudioFormat format, String originalFilename) {
        String extension = extractExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID() + extension;

        Path destination = rootLocation.resolve(uniqueFilename).normalize().toAbsolutePath();

        // Path traversal guard — ensure destination is inside rootLocation
        if (!destination.getParent().equals(rootLocation)) {
            throw new SecurityException(
                    "Resolved path escapes storage root: " + destination);
        }

        try {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored audio file: {}", uniqueFilename);
            return destination;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + originalFilename, e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename).normalize();
    }

    private String extractExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot).toLowerCase() : "";
    }
}