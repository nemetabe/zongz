package com.nemetabe.zongz.domain.library;

import jakarta.persistence.*;

@Entity
@Table(name = "albums")
public class Album extends AbstractLibraryEntity {

    // Album may eventually carry release year, label — add when CSV audit confirms the data
    private Integer releaseYear;  // nullable — ID3 coverage is partial

    protected Album() {}

    public Album(String rawName) {
        setDisplayName(rawName);
        setNormalizedName(rawName);
    }

    @Override
    public String getDisplayName() {
        return super.displayName;
    }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
}