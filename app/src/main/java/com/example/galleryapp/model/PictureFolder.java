package com.example.galleryapp.model;

public class PictureFolder {
    private String path;
    private String name;
    private long totalPicture = 0;
    private String firstPicture;

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

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
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
