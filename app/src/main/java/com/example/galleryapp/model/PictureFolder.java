package com.example.galleryapp.model;

import android.net.Uri;

import java.io.Serializable;

public class PictureFolder implements Serializable {
    private String path;
    private String name;
    private long totalPicture = 0;
    private transient Uri firstPicture;
    private String first;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalPicture() {
        return totalPicture;
    }

    public void setTotalPicture(long totalPicture) {
        this.totalPicture = totalPicture;
    }

    public void increaseTotalPicture() {
        totalPicture++;
    }

    public Uri getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(Uri firstPicture) {
        this.firstPicture = firstPicture;
    }

    @Override
    public String toString() {
        return "PictureFolder{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", totalPicture=" + totalPicture +
                ", firstPicture='" + firstPicture + '\'' +
                '}';
    }
}
