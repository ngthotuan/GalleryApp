package com.example.galleryapp.model;

import android.net.Uri;

import com.example.galleryapp.utils.DateUtil;

import java.io.Serializable;

public class Picture implements Serializable {
    private String name;
    private String path;
    private long size;
    private String type;
    private Uri uri;
    private boolean selected;
    private long createdDate;
    private long modifiedDate;

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

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
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
                ", createdDate=" + DateUtil.getDate(createdDate) +
                ", modifiedDate=" + DateUtil.getDate(modifiedDate) +
                '}';
    }
}
