package com.nemetabe.zongz.domain.track;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Abstract base for format-specific technical audio properties.
 * Holds fields common to every audio format.
 * Concrete subclasses add format-specific columns.
 *
 * Not an @Entity — no table for the abstract concept.
 * Each subclass gets its own table via @Inheritance(JOINED).
 */
@Getter
@Setter
@MappedSuperclass
public abstract class FileProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false, unique = true)
    private Track track;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "sample_rate_hz")
    private Integer sampleRateHz;

    @Column(name = "channels")
    private Integer channels;

    @Column(name = "is_lossless")
    private Boolean lossless;

    protected FileProperties() {}

    protected FileProperties(Track track) {
        if (track == null) throw new IllegalArgumentException("track must not be null");
        this.track = track;
    }
}