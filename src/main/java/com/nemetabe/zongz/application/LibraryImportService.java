// application/LibraryImportService.java
package com.nemetabe.zongz.application;

import com.nemetabe.zongz.domain.library.RekordboxRecord;
import com.nemetabe.zongz.domain.port.TrackRepository;
import com.nemetabe.zongz.domain.track.Track;
import com.nemetabe.zongz.domain.library.AlbumResolver;
import com.nemetabe.zongz.domain.library.ArtistResolver;
import com.nemetabe.zongz.domain.library.GenreResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Imports a single Rekordbox CSV row into a RekordboxRecord
 * and attaches it to an existing Track.
 *
 * Precondition: Track must already exist with status TAGGED or PENDING.
 * Postcondition: Track status advances to LIBRARY_ENRICHED.
 *
 * Does not parse the CSV itself — that's the caller's responsibility.
 * Accepts a RekordboxCsvRow value object carrying the raw strings.
 */
@Service
@Transactional
public class LibraryImportService {

    private static final Logger log = LoggerFactory.getLogger(LibraryImportService.class);

    private final TrackRepository trackRepository;
    private final ArtistResolver artistResolver;
    private final GenreResolver genreResolver;
    private final AlbumResolver albumResolver;

    public LibraryImportService(
            TrackRepository trackRepository,
            ArtistResolver artistResolver,
            GenreResolver genreResolver,
            AlbumResolver albumResolver) {
        this.trackRepository = trackRepository;
        this.artistResolver = artistResolver;
        this.genreResolver = genreResolver;
        this.albumResolver = albumResolver;
    }

    /**
     * Imports one CSV row. Idempotent — safe to call again if a previous
     * import failed partway through. The transaction rolls back on any
     * unchecked exception, leaving Track status unchanged.
     */
    public RekordboxRecord importRow(UUID trackId, RekordboxCsvRow row) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No track found for id: " + trackId));

        RekordboxRecord record = new RekordboxRecord(track);

        // --- Resolved entities ---
        if (row.artist() != null) {
            record.setArtist(artistResolver.resolveOrCreate(row.artist()));
        }
        if (row.genre() != null) {
            record.setGenre(genreResolver.resolveOrCreate(row.genre()));
        }
        if (row.album() != null) {
            record.setAlbum(albumResolver.resolveOrCreate(row.album()));
        }
        if (row.remixer() != null) {
            record.setRemixer(artistResolver.resolveOrCreate(row.remixer()));
        }
        if (row.composer() != null) {
            record.setComposer(artistResolver.resolveOrCreate(row.composer()));
        }

        // --- Scalar fields from LibraryRecord base ---
        record.setTitle(row.title());
        record.setLabel(row.label());
        record.setComment(row.comment());
        record.setReleaseYear(row.releaseYear());
        record.setTrackNumber(row.trackNumber());
        record.setDiscNumber(row.discNumber());
        record.setSubtitle(row.subtitle());
        record.setOriginalArtist(row.originalArtist());

        if (row.bpm() != null) {
            record.setBpm(row.bpm());
        }

        // --- Rekordbox-specific fields ---
        record.setMusicalKey(row.musicalKey());
        record.setColorLabel(row.colorLabel());
        record.setAnalysisDataPath(row.analysisDataPath());
        record.setDjPlayCount(row.djPlayCount());

        if (row.rekordboxContentId() != null) {
            record.setRekordboxContentId(row.rekordboxContentId());
        }
        if (row.rating() != null) {
            // CSV export gives normalised 0-5, not raw Rekordbox 0/51/102...
            // If reading directly from master.db, use setRatingFromRekordbox() instead
            record.setRating(row.rating());
        }

        track.setRekordboxRecord(record);
        track.markLibraryEnriched();

        Track saved = trackRepository.save(track);
        log.info("Library record imported for track {} — artist: {}, bpm: {}",
                trackId, row.artist(), row.bpm());

        return saved.getRekordboxRecord();
    }
}