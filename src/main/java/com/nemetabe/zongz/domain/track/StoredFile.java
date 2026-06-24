package com.nemetabe.zongz.domain.track;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Value object — where the file physically lives and what kind it is.
 * @Embeddable: no separate table, no separate ID.
 * Columns live directly on the tracks table.
 *
 * Immutable after construction — use StoredFile.of() factory.
 */
@Getter
@Setter
@Embeddable
public class StoredFile {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Setter
    @Column(name = "file_path", nullable = false, length = 512)
    private String path;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "audio_format", nullable = false, length = 10)
    private AudioFormat format;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "audio_quality_tier", nullable = false, length = 20)
    private AudioQualityTier qualityTier;

    @Setter
    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    protected StoredFile() {}

    private StoredFile(String path, AudioFormat format, Long fileSizeBytes) {
        if (path == null || path.isBlank()) throw new IllegalArgumentException("path must not be blank");
        if (format == null) throw new IllegalArgumentException("format must not be null");
        this.path = path;
        this.format = format;
        this.qualityTier = format.qualityTier();  // derived — single source of truth
        this.fileSizeBytes = fileSizeBytes;
    }

    public static StoredFile of(String path, AudioFormat format, Long fileSizeBytes) {
        return new StoredFile(path, format, fileSizeBytes);
    }

    public String path() { return path; }
    public AudioFormat format() { return format; }
    public AudioQualityTier qualityTier() { return qualityTier; }
    public Long fileSizeBytes() { return fileSizeBytes; }
}