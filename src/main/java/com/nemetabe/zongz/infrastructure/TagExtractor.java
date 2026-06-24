package com.nemetabe.zongz.infrastructure;

import com.nemetabe.zongz.domain.track.AudioFormat;
import com.nemetabe.zongz.domain.track.Mp3Properties;
import com.nemetabe.zongz.domain.track.RawTags;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * extracts raw tags and technical properties from an audio file on disk.
 * currently, it only supports MP3.
 * other formats are guarded and logged —
 * they will not fail the ingestion, but tags will be absent.
 *
 * lives in Infrastructure because it depends on JAudiotagger, a third-party
 * library. Domain has no knowledge of how tags are extracted.
 */
@Component
public class TagExtractor {

    private static final Logger log = LoggerFactory.getLogger(TagExtractor.class);

    /**
     * extracts raw ID3 tags from a file on disk into the provided RawTags entity.
     * mutates rawTags in place — caller is responsible for persisting.
     */
    public void extractTags(Path audioPath, AudioFormat format, RawTags rawTags) {
        if (format != AudioFormat.MP3) {
            log.warn("Tag extraction skipped for format {} — only MP3 is fully supported. Path: {}",
                    format, audioPath);
            return;
        }

        try {
            AudioFile audioFile = AudioFileIO.read(audioPath.toFile());
            Tag tag = audioFile.getTag();

            if (tag == null) {
                log.info("No ID3 tags found in file: {}", audioPath);
                return;
            }

            rawTags.setTitle(nullIfBlank(tag.getFirst(FieldKey.TITLE)));
            rawTags.setArtist(nullIfBlank(tag.getFirst(FieldKey.ARTIST)));
            rawTags.setAlbum(nullIfBlank(tag.getFirst(FieldKey.ALBUM)));
            rawTags.setGenre(nullIfBlank(tag.getFirst(FieldKey.GENRE)));
            rawTags.setTrackNumber(nullIfBlank(tag.getFirst(FieldKey.TRACK)));
            rawTags.setContainsCoverArt(!tag.getArtworkList().isEmpty());
            rawTags.setReleaseYear(parseYear(tag.getFirst(FieldKey.YEAR)));

        } catch (Exception e) {
            // tag extraction failure is non-fatal — track still ingests, tags stay null
            log.error("Failed to extract tags from {}: {}", audioPath, e.getMessage());
        }
    }

    /**
     * extracts technical stream properties into the provided Mp3Properties entity.
     * only called for MP3 files. mutates in place.
     */
    public void extractMp3Properties(Path audioPath, Mp3Properties props) {
        try {
            AudioFile audioFile = AudioFileIO.read(audioPath.toFile());
            AudioHeader header = audioFile.getAudioHeader();

            props.setDurationSeconds(header.getTrackLength());
            props.setBitRateKbps((int) header.getBitRateAsNumber());
            props.setSampleRateHz(header.getSampleRateAsNumber());
            props.setVbr(header.isVariableBitRate());

        } catch (Exception e) {
            log.error("Failed to extract MP3 properties from {}: {}", audioPath, e.getMessage());
        }
    }

    private String nullIfBlank(String value) {
        return (value == null || value.isBlank()) ? null : value.strip();
    }

    private Integer parseYear(String yearStr) {
        if (yearStr == null || yearStr.isBlank()) return null;
        try {
            return Integer.parseInt(yearStr.substring(0, Math.min(yearStr.length(), 4)));
        } catch (NumberFormatException e) {
            log.debug("Could not parse year string: '{}'", yearStr);
            return null;
        }
    }
}