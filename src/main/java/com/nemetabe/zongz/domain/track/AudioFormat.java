package com.nemetabe.zongz.domain.track;

/**
 * Supported audio formats.
 * Each format carries its quality tier — single source of truth.
 * StoredFile derives qualityTier from format at construction time.
 */
public enum AudioFormat {

    MP3(AudioQualityTier.LOSSY),
    AAC(AudioQualityTier.LOSSY),
    WAV(AudioQualityTier.LOSSLESS),
    FLAC(AudioQualityTier.LOSSLESS),
    AIFF(AudioQualityTier.LOSSLESS);

    private final AudioQualityTier qualityTier;

    AudioFormat(AudioQualityTier qualityTier) {
        this.qualityTier = qualityTier;
    }

    public AudioQualityTier qualityTier() {
        return qualityTier;
    }

    /**
     * parses a file extension to an AudioFormat.
     * called by IngestionService after reading the uploaded filename.
     * non-MP3 formats are supported structurally but may skip
     * tag extraction until their TagExtractor support is added.
     */
    public static AudioFormat fromExtension(String ext) {
        if (ext == null) throw new IllegalArgumentException("extension must not be null");
        return switch (ext.strip().toLowerCase()) {
            case "mp3"        -> MP3;
            case "aac"        -> AAC;
            case "wav"        -> WAV;
            case "flac"       -> FLAC;
            case "aif","aiff" -> AIFF;
            default -> throw new IllegalArgumentException("Unsupported audio format: " + ext);
        };
    }
}