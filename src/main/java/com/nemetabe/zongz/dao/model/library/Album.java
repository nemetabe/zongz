package com.nemetabe.zongz.dao.model.library;

public class Album extends AbstractLibraryEntity{
    private String title;
    private Artist artist;

    @Override
    public String getDisplayName() {
        return "";
    }
}
