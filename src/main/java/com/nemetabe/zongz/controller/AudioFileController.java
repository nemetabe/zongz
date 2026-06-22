package com.nemetabe.zongz.controller;

import com.nemetabe.zongz.dao.model.metadata.file.AudioFileReference;
import com.nemetabe.zongz.service.AudioFileMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/audio")
public class AudioFileController {

    @Autowired
    private AudioFileMetadataService metadataService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AudioFileReference> uploadAudio(@RequestParam("file")MultipartFile file){
        try {
            AudioFileReference ref = metadataService.processAudioUpload(file);
            return ResponseEntity.ok(ref);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
