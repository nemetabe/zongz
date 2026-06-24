package com.nemetabe.zongz.infrastructure;

import com.nemetabe.zongz.application.RekordboxCsvRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * parses a Rekordbox CSV export into RekordboxCsvRow records.
 * rekordbox CSV export: File → Export Collection in m3u format,
 * or the track list export from the library view.
 *
 * TODO: [implement] after running CSV column audit to confirm
 *                   which columns are present and what their exact header names are.
 */
@Component
public class RekordboxCsvParser {

    public List<RekordboxCsvRow> parse(InputStream csvStream) {
        // implementation depends on your actual CSV column headers.
        // run: df.columns.tolist() on your export first.
        throw new UnsupportedOperationException(
                "CSV parser not implemented yet — run column audit first");
    }
}