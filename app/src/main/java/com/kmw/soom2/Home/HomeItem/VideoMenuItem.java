package com.kmw.soom2.Home.HomeItem;

import android.graphics.Bitmap;
import android.net.Uri;

public class VideoMenuItem {

    private String title;
    private Bitmap img;
    private Uri uri;
    private long id;

    public VideoMenuItem(String title, Bitmap img, Uri uri, long id) {
        this.title = title;
        this.img = img;
        this.uri = uri;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImg() {
        return img;
    }

    public Uri getUri() {
        return uri;
    }

    public long getId(){return id;}
}
