package com.techcoderz.ruchira.ModelClasses;

/**
 * Created by Shahriar on 10/19/2016.
 */

public class Area {
    String beatId;
    String beatName;

    public Area() {
    }

    public Area(String beatId, String beatName) {
        this.beatId = beatId;
        this.beatName = beatName;
    }

    public String getBeatId() {
        return beatId;
    }

    public void setBeatId(String beatId) {
        this.beatId = beatId;
    }

    public String getBeatName() {
        return beatName;
    }

    public void setBeatName(String beatName) {
        this.beatName = beatName;
    }
}
