package com.nemetabe.zongz.application;

import com.nemetabe.zongz.domain.port.FileStore;
import com.nemetabe.zongz.domain.port.TrackRepository;
import com.nemetabe.zongz.domain.track.*;
import com.nemetabe.zongz.infrastructure.TagExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class IngestionService {

    private final FileStore fileStore;
    private final TrackRepository trackRepository;
    private final TagExtractor tagExtractor;

    public IngestionService(FileStore fileStore,
                            TrackRepository trackRepository,
                            TagExtractor tagExtractor) {
        this.fileStore = fileStore;
        this.trackRepository = trackRepository;
        this.tagExtractor = tagExtractor;
    }


    /**
     * @param file - audiofile to be uploaded and analysed
     * @return - returns the created aggregator Track class
     * @throws IOException
     *
     * 1. write file to disk — get stable path back
     * 2. create Track aggregate root with StoredFile
     * 3. extract tags — non-fatal if absent
     * 4. extract technical properties (MP3 only for now)
     */
    @Transactional
    public Track ingest(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        AudioFormat format = resolveFormat(originalName);

    // 1.
        Path storedPath = fileStore.store(
                file.getInputStream(),
                format,
                originalName);
    // 2.
        StoredFile storedFile = StoredFile.of(
                storedPath.toString(),
                format,
                file.getSize()
        );
        Track track = new Track(storedFile);
        trackRepository.save(track);
    // 3.
        RawTags rawTags = new RawTags(track);
        tagExtractor.extractTags(storedPath, format, rawTags);
        track.setRawTags(rawTags);
    // 4.
        if (format == AudioFormat.MP3) {
            Mp3Properties props = new Mp3Properties(track);
            tagExtractor.extractMp3Properties(storedPath, props);
            track.setMp3Properties(props);
        }

        return trackRepository.save(track);
    }

    private AudioFormat resolveFormat(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Cannot determine format: no file extension in '" + filename + "'");
        }
        String ext = filename.substring(filename.lastIndexOf('.') + 1);
        return AudioFormat.fromExtension(ext);
    }
}