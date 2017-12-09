package com.techcoderz.ruchira.Utils;

import android.content.Context;

import java.io.File;


public class FcsCacheManager {
    private File cacheDir;
    private File cacheDirTemp; // for shout image.
    final String GT_SONDHAN_CACHE_DIR = "CachedImages";
    final String TEMP_CACHED_IMAGE = GT_SONDHAN_CACHE_DIR + File.separator + "TempCachedImages";

    private static FcsCacheManager fcsCacheManager;

    public static FcsCacheManager getInstance() {
        if (fcsCacheManager == null) {
            fcsCacheManager = new FcsCacheManager();
        }

        return fcsCacheManager;
    }

    private FcsCacheManager() {
    }

    public void init(Context context) {
        //Find the dir at SDCARD to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            //if SDCARD is mounted (SDCARD is present on device and mounted)
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), GT_SONDHAN_CACHE_DIR);
            cacheDirTemp = new File(android.os.Environment.getExternalStorageDirectory(), TEMP_CACHED_IMAGE);
        } else {
            // if checking on simulator the create cache dir in your application context
            cacheDir = context.getCacheDir();
            cacheDirTemp = context.getCacheDir();
        }

        if (!cacheDir.exists()) {
            // create cache dir in your application context
            cacheDir.mkdirs();
        }

        if (!cacheDirTemp.exists()) {
            // create cache dir in your application context
            cacheDirTemp.mkdirs();
        }
    }

    public File getFilePathToCache(String url) {
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename = String.valueOf(url.hashCode());

        File file = new File(cacheDir, filename);
        return file;
    }

    public File getFilePath(String url) {
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename = String.valueOf(url.hashCode());

        File file = new File(cacheDir, filename);
        return file;
    }

    public File getTemporaryFilePathToCache(String url) {
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename = String.valueOf(url.hashCode());

        File file = new File(cacheDirTemp, filename);
        return file;
    }

    public Boolean isFileCacheExist(String url) {
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename = String.valueOf(url.hashCode());

        File file = new File(cacheDir, filename);
        if (file.exists()) {
            return true;
        }

        File tempfile = new File(cacheDirTemp, filename);
        if (tempfile.exists()) {
            return true;
        }

        return false;
    }

    public void clearAll() {
        File[] files = cacheDir.listFiles();
        if (files == null) {
            return;
        }
        for (File aFile : files) {
            aFile.delete();
        }

        clearTemporaryCache();
    }

    public void clearTemporaryCache() {
        File[] files = cacheDirTemp.listFiles();
        if (files == null) {
            return;
        }
        for (File aFile : files) {
            aFile.delete();
        }
    }
}
