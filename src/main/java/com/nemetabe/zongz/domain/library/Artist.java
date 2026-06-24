package com.nemetabe.zongz.domain;

import com.nemetabe.zongz.dao.model.metadata.TrackLibraryMetadata;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist extends AbstractLibraryEntity {

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
    private List<TrackLibraryMetadata> tracks = new ArrayList<>();

    protected Artist() {}  // JPA requires no-arg

    public Artist(String rawName) {
        setDisplayName(rawName);
        setNormalizedName(rawName);
    }

    @Override
    public String getDisplayName() {
        return super.displayName;
    }

    public List<TrackLibraryMetadata> getTracks() { return tracks; }
}