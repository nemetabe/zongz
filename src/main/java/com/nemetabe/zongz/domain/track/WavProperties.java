package com.nemetabe.zongz.domain.track;

import jakarta.persistence.*;

/**
 * stub — not wired into IngestionService yet.
 * –––
 * WAV-specific technical properties.
 * add when you have WAV files to test against.
 */
@Entity
@Table(name = "wav_properties")
public class WavProperties extends FileProperties {

    @Column(name = "bit_depth")
    private Integer bitDepth;   // 16, 24, 32 — key for HIGH_RES classification

    protected WavProperties() {}

    public WavProperties(Track track) {
        super(track);
    }

    public Integer getBitDepth() { return bitDepth; }
    public void setBitDepth(Integer bitDepth) { this.bitDepth = bitDepth; }

}
