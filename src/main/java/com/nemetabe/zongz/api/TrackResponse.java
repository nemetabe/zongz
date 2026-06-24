package com.nemetabe.zongz.api;

import com.nemetabe.zongz.domain.track.Track;
import java.util.UUID;

public record TrackResponse(
        UUID id,
        String storagePath,
        String format,
        String status,
        String rawArtist,
        String rawTitle
) {
    public static TrackResponse from(Track track) {
        return new TrackResponse(
                track.getId(),
                track.getStoredFile().path(),
                track.getStoredFile().format().name(),
                track.getStatus().name(),
                track.getRawTags() != null ? track.getRawTags().getArtist() : null,
                track.getRawTags() != null ? track.getRawTags().getTitle() : null
        );
    }
}