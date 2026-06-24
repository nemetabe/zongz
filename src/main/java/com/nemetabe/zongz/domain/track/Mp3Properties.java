// domain/Mp3Properties.java
package com.nemetabe.zongz.domain.track;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * MP3-specific technical properties.
 * Extends FileProperties with encoding details only MP3 has.
 *
 * Table: mp3_properties
 * Joins to file_properties via track_id (JOINED strategy not needed here
 * because @MappedSuperclass flattens columns into the concrete table directly).
 * All FileProperties columns + Mp3-specific columns live in one table.
 */
@Getter
@Setter
@Entity
@Table(name = "mp3_properties")
public class Mp3Properties extends FileProperties {

    @Column(name = "bit_rate_kbps")
    private Integer bitRateKbps;

    /**
     * Variable bit rate — affects duration accuracy.
     * VBR files report approximate bitrate; CBR files are exact.
     */
    @Column(name = "is_vbr")
    private Boolean vbr;

    @Column(name = "id3_version", length = 10)
    private String id3Version;   // e.g. "ID3v2.3", "ID3v1" — for debugging tag issues

    protected Mp3Properties() {}

    public Mp3Properties(Track track) {
        super(track);
    }
}