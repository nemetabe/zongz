package com.nemetabe.zongz.domain.port;

import com.nemetabe.zongz.domain.library.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByNormalizedNameContaining(String normalizedName);
}
