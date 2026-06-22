package com.nemetabe.zongz.dao.model.metadata.file;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "audio_file_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioFileReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storage_path", nullable = false, length = 512)
    private String storagePath;     // S3 object key or absolute disk path

    @Column(name = "file_extension", nullable = false, length = 10)
    private String fileExtension;   // mp3, flac, wav

    @Column(name = "file_size_bytes", nullable = false)
    private Long fileSizeBytes;

    // Unified Text Metadata Properties
    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "artist", length = 255)
    private String artist;

    @Column(name = "album", length = 255)
    private String album;

    @Column(name = "genre", length = 100)
    private String genre;

    @Column(name = "track_number", length = 20)
    private String trackNumber;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "contains_cover_art")
    private Boolean containsCoverArt;

    // Audio Stream Technical Properties
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "bit_rate_kbps")
    private Integer bitRateKbps;

    @Column(name = "sample_rate_hz")
    private Integer sampleRateHz;

    @Column(name = "is_lossless")
    private Boolean isLossless;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;
}

