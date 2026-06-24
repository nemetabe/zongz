package com.nemetabe.zongz.domain.track;

import com.nemetabe.zongz.domain.analysis.SpectrogramResult;
import com.nemetabe.zongz.domain.library.RekordboxRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "tracks")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private StoredFile storedFile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TrackStatus status;

    @Column(nullable = false, updatable = false)
    private Instant uploadedAt;

    // Enrichment setters — called by IngestionService and LibraryImportService
    @Setter
    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RawTags rawTags;

    @Setter
    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Mp3Properties mp3Properties;

    @Setter
    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RekordboxRecord rekordboxRecord;

    @Setter
    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SpectrogramResult spectrogramResult;

    protected Track() {}   // JPA only

    public Track(StoredFile storedFile) {
        if (storedFile == null) throw new IllegalArgumentException("StoredFile must not be null");
        this.storedFile = storedFile;
        this.status = TrackStatus.PENDING;
        this.uploadedAt = Instant.now();
    }

    // Status transitions — explicit methods, not a raw setter
    // Forces callers to be intentional about state changes
    public void markTagged() {
        this.status = TrackStatus.TAGGED;
    }

    public void markLibraryEnriched() {
        this.status = TrackStatus.LIBRARY_ENRICHED;
    }

    public void markAnalysed() {
        this.status = TrackStatus.ANALYSED;
    }

    public void markFailed() {
        this.status = TrackStatus.FAILED;
    }
}