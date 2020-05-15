package com.example.qwikpik;

import android.graphics.Bitmap;

import java.io.File;

public class Picture {

    private Bitmap image;
    private String title;
    private String picturePath;
    private File Directory;


    public Picture(Bitmap image, String title, String picturePath, File directory) {
        this.image = image;
        this.title = title;
        this.picturePath = picturePath;
        this.Directory = directory;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public File getDirectory() {
        return Directory;
    }

    public void setDirectory(File directory) {
        Directory = directory;
    }
}
