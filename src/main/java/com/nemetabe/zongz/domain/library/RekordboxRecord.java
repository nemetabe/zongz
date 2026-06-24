package com.nemetabe.zongz.domain.metadata;

import com.nemetabe.zongz.domain.track.Track;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Rekordbox-specific library metadata sourced from djmdContent (master.db)
 *
 * extends LibraryRecord with fields that are Pioneer/Rekordbox-specific:
 * DJ play count, color grouping, musical key, star rating,
 * analysis data path, and the Rekordbox-internal content ID.
 *
 * field names map directly to djmdContent columns where possible,
 * with Java naming conventions applied.
 *
 * source: pyrekordbox djmdContent schema
 * https://pyrekordbox.readthedocs.io/en/latest/formats/db6.html
 */

@Getter
@Setter
@Entity
@Table(name = "rekordbox_records")
public class RekordboxRecord extends LibraryRecord {

    /**
     * Rekordbox internal content ID from djmdContent.ID.
     * Preserved for cross-referencing with the original database
     * and for future direct master.db integration.
     */
    @Column(name = "rb_content_id")
    private Long rekordboxContentId;

    /**
     * musical key in Camelot notation (e.g. "8A", "11B").
     * Rekordbox stores this as a foreign key to djmdKey.
     * we store the resolved display value — no need for a separate Key entity
     * at this stage since key values are a fixed closed set.
     */
    @Column(name = "musical_key", length = 10)
    private String musicalKey;

    /**
     * star rating 0–5. Rekordbox stores 0, 51, 102, 153, 204, 255
     * internally (multiples of 51). We normalise to 0–5 on import.
     */
    @Column(name = "rating")
    private Integer rating;

    /**
     * DJ play count from djmdContent.DJPlayCount.
     * incremented when track is played via CDJ or Rekordbox performance mode.
     */
    @Column(name = "dj_play_count")
    private Integer djPlayCount;

    /**
     * color label used for visual grouping in Rekordbox.
     * stored as the color name string (e.g. "Red", "Blue", "Green").
     * Rekordbox has a fixed set of colors via djmdColor table.
     */
    @Column(name = "color_label", length = 30)
    private String colorLabel;

    /**
     * path to Rekordbox ANLZ analysis files (beatgrid, waveform, phrase data).
     * relative to the Rekordbox database root.
     * useful if you later want to parse beatgrids or waveform data directly.
     */
    @Column(name = "analysis_data_path", length = 512)
    private String analysisDataPath;

    /**
     * subtitle or mix name (e.g. "Original Mix", "Club Edit").
     * maps to djmdContent.Subtitle.
     */
    @Column(name = "subtitle", length = 255)
    private String subtitle;

    /**
     * original artist — for remixes where artist is the remixer
     * and original_artist is who made the source track.
     * maps to djmdContent.OrgArtistID → djmdArtist.Name.
     */
    @Column(name = "original_artist", length = 255)
    private String originalArtist;

    protected RekordboxRecord() {}

    public RekordboxRecord(Track track) {
        super(track);
    }

    /**
     * accepts Rekordbox's raw rating value (0, 51, 102, 153, 204, 255)
     * and normalises to 0–5.
     */
    public void setRatingFromRekordbox(int rawRating) {
        this.rating = rawRating / 51;
    }

    public void setRating(Integer rating) {
        if (rating != null && (rating < 0 || rating > 5)) {
            throw new IllegalArgumentException("Rating must be 0–5, got: " + rating);
        }
        this.rating = rating;
    }
}