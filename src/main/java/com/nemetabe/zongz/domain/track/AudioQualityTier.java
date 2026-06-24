package com.nemetabe.zongz.domain.track;

/**
 * Coarse quality classification derived from AudioFormat.
 * Never set directly — always computed via AudioFormat.qualityTier().
 *
 * HIGH_RES reserved for 24-bit+ WAV/FLAC once WavProperties.bitDepth
 * is populated and a classification service can evaluate it.
 */
public enum AudioQualityTier {
    LOSSY,
    LOSSLESS,
    HIGH_RES
}