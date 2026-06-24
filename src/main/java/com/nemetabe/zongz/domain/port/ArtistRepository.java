package com.nemetabe.zongz.domain.port;

import com.nemetabe.zongz.domain.library.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> findByNormalizedNameContaining(String normalizedName);
}
