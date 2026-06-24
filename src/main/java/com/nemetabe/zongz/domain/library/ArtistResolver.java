package com.nemetabe.zongz.infrastructure;

import com.nemetabe.zongz.domain.library.Artist;
import com.nemetabe.zongz.domain.library.EntityResolver;
import com.nemetabe.zongz.domain.port.ArtistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class ArtistResolver implements EntityResolver<Artist> {

    private final ArtistRepository artistRepository;

    public ArtistResolver(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Optional<Artist> resolve(String rawName) {
        if (rawName == null || rawName.isBlank()) return Optional.empty();
        String normalized = rawName.strip().toLowerCase();
        return artistRepository.findByNormalizedNameContaining(normalized).stream().findFirst();
    }

    @Override
    public Artist resolveOrCreate(String rawName) {
        return resolve(rawName).orElseGet(() -> {
            Artist artist = new Artist(rawName);
            return artistRepository.save(artist);
        });
    }

}
