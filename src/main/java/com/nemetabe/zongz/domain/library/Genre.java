package com.nemetabe.zongz.domain.library;

import jakarta.persistence.*;

@Entity
@Table(name = "genres")
public class Genre extends AbstractLibraryEntity {

    protected Genre() {}

    public Genre(String rawName) {
        setDisplayName(rawName);
        setNormalizedName(rawName);
    }

    @Override
    public String getDisplayName() {
        return super.displayName;
    }
}