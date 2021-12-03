package com.example.galleryapp.model;

import android.net.Uri;

import com.example.galleryapp.utils.DateUtil;

import java.util.Date;

public class Picture {
    private String name;
    private String path;
    private long size;
    private String type;
    private Uri uri;
    private boolean selected;
    private Date createdDate;
    private Date modifiedDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", uri=" + uri +
                ", selected=" + selected +
                ", createdDate=" + DateUtil.getDate(createdDate.getTime()) +
                ", modifiedDate=" + DateUtil.getDate(modifiedDate.getTime()) +
                '}';
    }
}
