package com.example.musicplayer;

public class Track {
    private String titile;
    private int image;

    public Track(String titile, int image) {
        this.titile = titile;
        this.image = image;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
