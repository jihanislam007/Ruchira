package com.techcoderz.ruchira.ModelClasses;

/**
 * Created by Shahriar on 9/27/2016.
 */

public class Beat {
    String id;
    String title;

    public Beat() {
    }

    public Beat(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
