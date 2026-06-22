package com.nemetabe.zongz.service;

import com.nemetabe.zongz.dao.model.metadata.file.AudioFileReference;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AudioFileMetadataService {

    public AudioFileReference processAudioUpload(MultipartFile file) throws Exception {
        File tempFile = convertMultipartToFile(file);

        try {
            AudioFile audioFile = AudioFileIO.read(tempFile);
            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();

            AudioFileReference ref = new AudioFileReference();
            ref.setFileSizeBytes(file.getSize());
            ref.setUploadedAt(LocalDateTime.now());

            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")){
                ref.setFileExtension(
                        originalName.substring(
                                originalName.lastIndexOf(".") + 1
                        ).toLowerCase()
                );
            }

            ref.setDurationSeconds(header.getTrackLength());
            ref.setBitRateKbps((int) header.getBitRateAsNumber());
            ref.setSampleRateHz(header.getSampleRateAsNumber());
            ref.setIsLossless(header.isLossless());

            if(tag != null) {
                ref.setTitle(tag.getFirst(FieldKey.TITLE));
                ref.setArtist(tag.getFirst(FieldKey.ARTIST));
                ref.setAlbum(tag.getFirst(FieldKey.ALBUM));
                ref.setGenre(tag.getFirst(FieldKey.GENRE));
                ref.setTrackNumber(tag.getFirst(FieldKey.TRACK));

                String yearStr = tag.getFirst(FieldKey.YEAR);
                if (yearStr != null && !yearStr.isEmpty()) {
                    try {
                        ref.setReleaseYear(
                                Integer.parseInt(
                                        yearStr.substring(
                                                0, Math.min(yearStr.length(), 4)
                                        )
                                )
                        );
                    } catch (NumberFormatException e) {
                        ref.setReleaseYear(null);
                    }
                }
                ref.setContainsCoverArt(!tag.getArtworkList().isEmpty());
            }
                return ref;

        } finally {
                if (tempFile.exists()){
                    tempFile.delete();
                }

    }
}

    private File convertMultipartToFile(MultipartFile file) throws IOException{
        File convFile = File.createTempFile("temp-", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)){
            fos.write(file.getBytes());
        }
        return convFile;
    }

}
