package com.nemetabe.zongz.domain.port;

import com.nemetabe.zongz.domain.track.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrackRepository extends JpaRepository<Track, UUID> {
    List<Track> findByArtistContainingIgnoreCase(String artist);

}
