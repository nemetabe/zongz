package com.nemetabe.zongz.infrastructure;

import com.nemetabe.zongz.domain.library.Album;
import com.nemetabe.zongz.domain.library.EntityResolver;
import com.nemetabe.zongz.domain.port.AlbumRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class AlbumResolver implements EntityResolver<Album> {

    private final AlbumRepository albumRepository;

    public AlbumResolver(AlbumRepository repository){
        this.albumRepository = repository;
    }

    @Override
    public Optional<Album> resolve(String rawName) {
        return Optional.empty();
    }

    @Override
    public Album resolveOrCreate(String rawName) {
        return resolve(rawName).orElseGet(() -> {
            Album album = new Album(rawName);
            return albumRepository.save(album);
        });
    }
}
