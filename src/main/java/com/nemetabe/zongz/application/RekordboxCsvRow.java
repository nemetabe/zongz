// application/RekordboxCsvRow.java
package com.nemetabe.zongz.application;

/**
 * Value object carrying one parsed row from a Rekordbox CSV export.
 * All fields nullable — CSV exports vary by Rekordbox version and
 * which columns the user has enabled.
 *
 * This is a data transfer object between the CSV parser and
 * LibraryImportService. It is not a domain entity.
 *
 * Field names map to the Rekordbox CSV column headers
 * after normalisation (trimmed, lowercased, spaces replaced).
 */
public record RekordboxCsvRow(
        Long rekordboxContentId,
        String title,
        String artist,
        String album,
        String genre,
        String remixer,
        String composer,
        String label,
        String comment,
        String subtitle,
        String originalArtist,
        String musicalKey,       // Camelot notation e.g. "8A"
        String colorLabel,       // e.g. "Red", "Blue"
        String analysisDataPath,
        Double bpm,
        Integer releaseYear,
        Integer trackNumber,
        Integer discNumber,
        Integer djPlayCount,
        Integer rating           // normalised 0–5
) {}