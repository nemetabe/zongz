package com.nemetabe.zongz.dao.model.library;

@Entity
public class Track {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String storagePath;
    private Instant uploadedAt;

    @Enumerated(EnumType.STRING)
    private TrackStatus status;

    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TrackFileMetadata fileMetadata;

    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TrackLibraryMetadata libraryMetadata;

    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TrackAnalysis analysis;
}