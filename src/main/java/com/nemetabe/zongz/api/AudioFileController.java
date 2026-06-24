package com.nemetabe.zongz.api;

import com.nemetabe.zongz.application.IngestionService;
import com.nemetabe.zongz.domain.track.Track;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/tracks")
public class AudioFileController {

    private final IngestionService ingestionService;


    public AudioFileController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrackResponse> upload(@RequestParam("file") MultipartFile file) {
        try {
            Track track = ingestionService.ingest(file);
            return ResponseEntity.ok(TrackResponse.from(track));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
