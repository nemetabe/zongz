package com.nemetabe.zongz.domain.port;

import com.nemetabe.zongz.domain.track.AudioFormat;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Port — domain defines what it needs, infrastructure decides how.
 * No Spring types, no JPA, no framework imports.
 * Does NOT extend JpaRepository or any Spring interface.
 */
public interface FileStore {
    /**
     * Persists an audio stream to storage.
     * @return the stable path the file was written to
     */
    Path store(InputStream inputStream, AudioFormat format, String originalFilename);

    Path load(String filename);
}