package com.shikhar.edforamusic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Music {

    @SerializedName("song")
    @Expose
    private String song;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("artists")
    @Expose
    private String artists;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;

    public String getSong() {
        return song;
    }

    public String getUrl() {
        return url;
    }

    public String getArtists() {
        return artists;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public Music(String song, String url, String artists, String coverImage) {
        this.song = song;
        this.url = url;
        this.artists = artists;
        this.coverImage = coverImage;
    }
}