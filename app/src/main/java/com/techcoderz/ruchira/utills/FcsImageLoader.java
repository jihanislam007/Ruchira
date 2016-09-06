package com.techcoderz.ruchira.utills;//package com.gonona.gt_sondhan.com.techcoderz.ruchira.utills;
//
///**
// * Created by stephen on 02/07/2015.
// */
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//
//import com.gonona.gt_sondhan.R;
//import com.gonona.gt_sondhan.com.techcoderz.ruchira.proportionalimageviewer.MemoryCache;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.WeakHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class FcsImageLoader {
//    private static final String TAG = FcsImageLoader.class.getName();
//    // Initialize MemoryCache
//    MemoryCache memoryCache = new MemoryCache();
//
//    //Create Map (collection) to store image and image imageUrl in key value pair
//    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
//    private List<String> downloadingList = new ArrayList<>();
//    ExecutorService executorService;
//
//    //handler to display images in UI thread
//    Handler handler = new Handler();
//
//    public FcsImageLoader() {
//        // Creates a thread pool that reuses a fixed number of
//        // threads operating off a shared unbounded queue.
//        executorService = Executors.newFixedThreadPool(5);
//    }
//
//    // default image show in list (Before online image download)
//    int placeHolderImage = R.drawable.loading;
//
//    public void displayImage(String url, ImageView imageView) {
//        displayImage(url, imageView, null);
//    }
//
//    public void displayImage(String url, ImageView imageView, ProgressBar loadingView) {
//        if (TaskUtils.isFromLocalStorage(url)) {
//            Log.d(TAG, "displayImage isFromLocalStorage true");
//            imageView.setImageURI(Uri.parse(url));
//            return;
//        }
//
//        Log.d(TAG, "displayImage isFromLocalStorage false");
//
//        Bitmap bitmap = memoryCache.get(url);
//
//        imageViews.put(imageView, url);
//
//        if (bitmap != null) {
//            imageView.setImageBitmap(bitmap);
//            return;
//        }
//
//        //queue Photo to find locally or download from imageUrl
//        addInUrlProcessingQueue(url, imageView, loadingView);
//
//        if (loadingView == null) {
//            //Before downloading image show default image
//            imageView.setImageResource(placeHolderImage);
//        }
//    }
//
//    private void addInUrlProcessingQueue(String url, ImageView imageView, ProgressBar loadingView) {
//        PhotoToLoad photoToLoad = new PhotoToLoad(url, imageView, loadingView);
//        if (photoToLoad.loadingView != null) {
//            photoToLoad.loadingView.setVisibility(View.VISIBLE);
//        }
//
//        executorService.submit(new PhotosLoader(photoToLoad));
//    }
//
//    //Task for the queue
//    private class PhotoToLoad {
//        public String imageUrl;
//        public ImageView targetImageView;
//        public ProgressBar loadingView;
//
//        public PhotoToLoad(String u, ImageView i, ProgressBar loadingView) {
//            imageUrl = u;
//            targetImageView = i;
//            this.loadingView = loadingView;
//        }
//    }
//
//    class PhotosLoader implements Runnable {
//        PhotoToLoad photoToLoad;
//
//        PhotosLoader(PhotoToLoad photoToLoad) {
//            this.photoToLoad = photoToLoad;
//        }
//
//        @Override
//        public void run() {
//            try {
//                File localFile;
//                final String imageUrl = photoToLoad.imageUrl;
//
//                if (imageViewReused(photoToLoad)) {
//                    return;
//                }
//
//                if (FcsCacheManager.getInstance().isFileCacheExist(imageUrl)) {
//                    Log.d(TAG, "image found in local cache");
//                    localFile = FcsCacheManager.getInstance().getFilePath(imageUrl);
//
//                } else {
//                    Log.d(TAG, "image will be downloaded");
//                    if (downloadingList.contains(imageUrl)) {
//                        Log.d(TAG, "image already in download queue, won't download add again");
//                        return;
//                    }
//                    downloadingList.add(imageUrl);
//                    localFile = downloadImage(imageUrl);
//                    downloadingList.remove(imageUrl);
//                }
//
//                if (imageViewReused(photoToLoad)) {
//                    return;
//                }
//
//                ImageDisplayer imageDisplayer = new ImageDisplayer(localFile, photoToLoad);
//                handler.post(imageDisplayer);
//
//            } catch (Throwable th) {
//                th.printStackTrace();
//            }
//        }
//    }
//
//    private File downloadImage(String url) {
//        File downloadedFile = null;
//        try {
//            URL imageUrl = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
//            conn.setConnectTimeout(30000);
//            conn.setReadTimeout(30000);
//            conn.setInstanceFollowRedirects(true);
//            InputStream is = conn.getInputStream();
//
//            // Constructs a new FileOutputStream that writes to file
//            // if file not exist then it will create file
//            downloadedFile = FcsCacheManager.getInstance().getFilePathToCache(url);
//            OutputStream os = new FileOutputStream(downloadedFile);
//
//            // See Utils class CopyStream method
//            // It will each pixel from input stream and
//            // write pixels to output stream (file)
//            Utils.CopyStream(is, os);
//
//            os.close();
//            conn.disconnect();
//            Log.d(TAG, "image downloaded");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return downloadedFile;
//    }
//
//    private Bitmap getBitmap(String url, Context context) {
//        File cacheFile = FcsCacheManager.getInstance().getFilePathToCache(url);
//
//        //from SD cache
//        //CHECK : if trying to decode file which not exist in cache return null
//        Bitmap b = decodeFile(cacheFile, context);
//        if (b != null) {
//            return b;
//        }
//
//        // Download image file from web
//        try {
//            Bitmap bitmap = null;
//            URL imageUrl = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
//            conn.setConnectTimeout(30000);
//            conn.setReadTimeout(30000);
//            conn.setInstanceFollowRedirects(true);
//            InputStream is = conn.getInputStream();
//
//            // Constructs a new FileOutputStream that writes to file
//            // if file not exist then it will create file
//            OutputStream os = new FileOutputStream(cacheFile);
//
//            // See Utils class CopyStream method
//            // It will each pixel from input stream and
//            // write pixels to output stream (file)
//            Utils.CopyStream(is, os);
//
//            os.close();
//            conn.disconnect();
//
//            //Now file created and going to resize file with defined height
//            // Decodes image and scales it to reduce memory consumption
//            bitmap = decodeFile(cacheFile, context);
//
//            return bitmap;
//
//        } catch (Throwable ex) {
//            ex.printStackTrace();
//        }
//
//        return null;
//    }
//
//    //Decodes image and scales it to reduce memory consumption
//    private Bitmap decodeFile(File imageFile, Context context) {
//        try {
//            //Decode image size
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
//
//            //Find the correct scale value. It should be the power of 2.
//
//            // Set width/height of recreated image
//            //final int deviceWidth=85;
//
//            int imageWidth = options.outWidth;
//
//            int deviceWidth = ViewUtils.getScreenWidthInPixels(context);
//
//            int scale = 1;
//            while (true) {
//                if (imageWidth / 2 < deviceWidth) {
//                    break;
//                }
//                imageWidth /= 2;
//                scale *= 2;
//            }
//
//            //decode with current scale values
//            options.inJustDecodeBounds = false;
//            options.inDither = false;
//            options.inSampleSize = imageWidth / deviceWidth;
//            options.inScaled = false;
//            options.inPurgeable = true;
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//            Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
//
//            return sampledSrcBitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * this method is needed to check whether request is valid or invalid
//     *
//     * @param photoToLoad : last time requested info
//     * @return request no longer exist, image view now holds new url to load
//     */
//
//    boolean imageViewReused(PhotoToLoad photoToLoad) {
//        String currentlyStoredUrl = imageViews.get(photoToLoad.targetImageView);
//        final String lastTimeRequestUrl = photoToLoad.imageUrl;
//        //Check imageUrl is already exist in imageViews MAP
//        if (currentlyStoredUrl == null || !currentlyStoredUrl.equals(lastTimeRequestUrl)) {
//            return true;
//        }
//        return false;
//    }
//
//    //Used to display bitmap in the UI thread
//    class ImageDisplayer implements Runnable {
//        File fileToshow;
//        PhotoToLoad photoToLoad;
//
//        public ImageDisplayer(File fileToShow, PhotoToLoad photoToLoad) {
//            this.fileToshow = fileToShow;
//            this.photoToLoad = photoToLoad;
//        }
//
//        public void run() {
//            if (imageViewReused(photoToLoad)) {
//                return;
//            }
//
//            final Target target = new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
//                    photoToLoad.targetImageView.setImageBitmap(bitmap);
//                    if (photoToLoad.loadingView != null) {
//                        photoToLoad.loadingView.setVisibility(View.GONE);
//                    }
//                    memoryCache.put(photoToLoad.imageUrl, bitmap);
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable drawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable drawable) {
//
//                }
//            };
//            photoToLoad.targetImageView.setTag(target);
//            Picasso.with(photoToLoad.targetImageView.getContext())
//                    .load(fileToshow)
//                    .into(target);
//        }
//    }
//
//    public void clearCache() {
//        //Clear cache directory downloaded images and stored data in maps
//        memoryCache.clear();
//        FcsCacheManager.getInstance().clearAll();
//    }
//
//}
