package com.information.rxjavaapplication.fragments;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

class RecyclerItem {
    private Drawable image;
    private String title;

    public RecyclerItem(Drawable image, String text) {
        this.image = image;
        this.title = text;
    }

    public Drawable getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
