package com.nemetabe.zongz.domain.library;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
    // TODO

@MappedSuperclass
public abstract class AbstractLibraryEntity {

    // Getters (no setters on normalizedName — it's derived, not set directly)
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    protected String normalizedName;   // lowercase, stripped — dedup key

    @Column(nullable = false)
    protected String displayName;      // original casing — what the UI shows

    protected Instant createdAt = Instant.now();

    // Dedup logic lives here — subclasses inherit it
    public boolean matches(String rawInput) {
        if (rawInput == null || rawInput.isBlank()) return false;
        return this.normalizedName.equals(rawInput.strip().toLowerCase());
    }

    public abstract String getDisplayName();

    public UUID getId() {
        return id;
    }

    public String getNormalizedName() { return normalizedName; }
    public Instant getCreatedAt() { return createdAt; }

    protected void setNormalizedName(String raw) {
        this.normalizedName = raw == null ? null : raw.strip().toLowerCase();
    }
    protected void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
