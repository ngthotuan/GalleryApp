package com.example.galleryapp.model;

public class TableRowCount {
    private long imageRow;
    private long albumRow;
    private long linkRow;

    public TableRowCount(long imageRow, long albumRow, long linkRow) {
        this.imageRow = imageRow;
        this.albumRow = albumRow;
        this.linkRow = linkRow;
    }

    public long getImageRow() { return imageRow; }

    public long getAlbumRow() { return albumRow; }

    public long getLinkRow() { return linkRow; }
}
