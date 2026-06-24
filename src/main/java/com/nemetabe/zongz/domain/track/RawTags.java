package com.nemetabe.zongz.domain.track;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Raw, unverified tag data extracted directly from the audio file.
 * Strings only — no resolved entities. Source of truth: JAudiotagger.
 * Null fields mean the tag was absent or unparseable, not that the
 * value doesn't exist in the world.
 */
@Getter
@Setter
@Entity
@Table(name = "raw_tags")
public class RawTags {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private java.util.UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(name = "raw_title", length = 255)
    private String title;

    @Column(name = "raw_artist", length = 255)
    private String artist;

    @Column(name = "raw_album", length = 255)
    private String album;

    @Column(name = "raw_genre", length = 100)
    private String genre;

    @Column(name = "track_number", length = 20)
    private String trackNumber;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "contains_cover_art")
    private Boolean containsCoverArt;

    protected RawTags() {}

    public RawTags(Track track) {
        this.track = track;
    }

}