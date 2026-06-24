package com.nemetabe.zongz.domain.port;

import com.nemetabe.zongz.domain.library.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByNormalizedNameContaining(String normalizedName);
}
