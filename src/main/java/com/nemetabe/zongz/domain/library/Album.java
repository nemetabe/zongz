package com.nemetabe.zongz.domain;

public class Album extends AbstractLibraryEntity{
    private String title;
    private Artist artist;

    @Override
    public String getDisplayName() {
        return "";
    }
}
