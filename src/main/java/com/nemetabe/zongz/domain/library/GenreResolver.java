package com.nemetabe.zongz.domain.library;

import com.nemetabe.zongz.domain.port.GenreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class GenreResolver implements EntityResolver<Genre> {

    private final GenreRepository genreRepository;

    public GenreResolver(GenreRepository repository){
        this.genreRepository = repository;
    }

    @Override
    public Optional<Genre> resolve(String rawName) {
        if (rawName == null || rawName.isBlank()) return Optional.empty();
        String normalized = rawName.strip().toLowerCase();
        return genreRepository.findByNormalizedNameContaining(rawName);
    }

    @Override
    public Genre resolveOrCreate(String rawName) {
        return resolve(rawName).orElseGet(() -> {
            Genre genre = new Genre(rawName);
            return genreRepository.save(genre);
        });
    }

}
