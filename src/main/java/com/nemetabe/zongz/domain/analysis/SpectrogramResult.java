package com.nemetabe.zongz.domain.analysis;

import com.nemetabe.zongz.domain.track.Track;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SpectrogramResult {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    Track track;
}