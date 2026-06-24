package com.nemetabe.zongz.domain.library;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist extends AbstractLibraryEntity {

    protected Artist() {}  // JPA requires no-arg

    public Artist(String rawName) {
        setDisplayName(rawName);
        setNormalizedName(rawName);
    }

    @Override
    public String getDisplayName() {
        return super.displayName;
    }
}