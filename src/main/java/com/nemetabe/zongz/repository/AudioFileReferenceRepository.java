package com.nemetabe.zongz.repository;

import com.nemetabe.zongz.dao.model.metadata.file.AudioFileReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioFileReferenceRepository extends JpaRepository<AudioFileReference, Long>{
    List<AudioFileReference> findByArtistContainingIgnoreCase(String artist);
}
