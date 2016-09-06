package com.techcoderz.ruchira.proportionalimageviewer;

import android.content.Context;

import java.io.File;


public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        //Find the dir at SDCARD to save cached images

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            //if SDCARD is mounted (SDCARD is present on device and mounted)
            String fcSocialCachedImage = "fcAppraiseCachedImages";
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), fcSocialCachedImage);
        } else {
            // if checking on simulator the create cache dir in your application context
            cacheDir = context.getCacheDir();
        }

        if (!cacheDir.exists()) {
            // create cache dir in your application context
            cacheDir.mkdirs();
        }
    }

    public File getFile(String url) {
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename = String.valueOf(url.hashCode());

        File file = new File(cacheDir, filename);
        return file;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null) {
            return;
        }
        for (File aFile : files) {
            aFile.delete();
        }
    }
}
