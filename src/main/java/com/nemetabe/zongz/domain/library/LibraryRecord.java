package com.nemetabe.zongz.domain.library;

import com.nemetabe.zongz.domain.track.Track;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * abstract base for library metadata sourced from an external library tool.
 * fields here are universal
 * — any collection library (Spotify, SoundCloud, BandCamp, Apple Music)
 * - any DJ software (RekordBox, Serato, Traktor)
 * would have equivalents.
 *
 * resolved entities (Artist, Genre, Album) live here — not raw strings.
 * raw strings live in RawTags. these two classes represent different
 * epistemic states: unverified input vs committed domain knowledge.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LibraryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false, unique = true)
    private Track track;

    // --- resolved library entities ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remixer_id")
    private Artist remixer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "composer_id")
    private Artist composer;

    // --- universal track metadata ---

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "track_number")
    private Integer trackNumber;

    @Column(name = "disc_number")
    private Integer discNumber;

    @Column(name = "comment", length = 1024)
    private String comment;

    @Column(name = "label", length = 255)
    private String label;

    /**
     * BPM stored as integer (Rekordbox stores as BPM * 100 internally,
     * e.g. 12850 = 128.50 BPM). We store the human-readable value * 100
     * to preserve decimal precision without floating point drift.
     */
    @Column(name = "bpm_hundredths")
    private Integer bpmHundredths;

    @Column(name = "imported_at")
    private Instant importedAt;

    protected LibraryRecord() {}

    protected LibraryRecord(Track track) {
        if (track == null) throw new IllegalArgumentException("track must not be null");
        this.track = track;
        this.importedAt = Instant.now();
    }

    // convenience method — callers work with human-readable BPM
    public Double getBpm() {
        return bpmHundredths != null ? bpmHundredths / 100.0 : null;
    }

    public void setBpm(double bpm) {
        this.bpmHundredths = (int) Math.round(bpm * 100);
    }

}