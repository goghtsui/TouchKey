package com.gogh.floattouchkey.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.os.AsyncTaskCompat;
import android.view.Surface;
import android.view.WindowManager;

import com.gogh.floattouchkey.entity.GraphicPath;
import com.gogh.floattouchkey.uitls.Logger;
import com.gogh.floattouchkey.uitls.Screen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 5/9/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/9/2018 do fisrt create. </li>
 */
public class CaptureManager {

    private static final String TAG = "CaptureManager";
    public static Surface surface;
    public static GraphicPath graphicPath;
    private static WindowManager windowManager;
    private static int screenDensity;
    private static int screenWidth;
    private static int screenHeight;
    private static MediaProjection mediaProjection;
    private static VirtualDisplay virtualDisplay;
    private static ImageReader imageReader;
    private static AtomicInteger isAquire = new AtomicInteger(0);

    public static void setUpMediaProjection(Activity activity, Intent scIntent) {
        if (scIntent == null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            activity.startActivity(intent);
        } else {
            mediaProjection = getMediaProjectionManager(activity).getMediaProjection(Activity.RESULT_OK,
                    scIntent);
        }
    }

    public static void initScreenSize() {
        screenDensity = Screen.get().getDensity();
        screenWidth = Screen.get().getWidth();
        screenHeight = Screen.get().getHeight();
    }

    public static void createImageReader() {
        imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1);
    }

    public static void beginScreenShot(final Activity activity, final Intent intent) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                beginVirtual(activity, intent);
            }
        }, 0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                beginCapture(activity, intent);
            }
        }, 150);
    }

    private static void beginVirtual(Activity activity, Intent intent) {
        if (null != mediaProjection) {
            virtualDisplay();
        } else {
            setUpMediaProjection(activity, intent);
            virtualDisplay();
        }
    }

    private static void virtualDisplay() {
        surface = imageReader.getSurface();
        virtualDisplay = mediaProjection.createVirtualDisplay("screen-mirror", screenWidth,
                screenHeight, screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, surface,
                null, null);
    }

    private static MediaProjectionManager getMediaProjectionManager(Activity activity) {
        return (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private static void beginCapture(Activity activity, Intent intent) {
        if(isAquire.getAndSet(1) == 0){
            Logger.d(TAG, "beginCapture");
            Image acquireLatestImage;
            acquireLatestImage = imageReader.acquireLatestImage();
            if (acquireLatestImage == null) {
                isAquire.set(0);
                beginScreenShot(activity, intent);
            } else {
                SaveTask saveTask = new SaveTask();
                AsyncTaskCompat.executeParallel(saveTask, acquireLatestImage);
            }
        }
    }

    public static void release(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                releaseVirtual();
                stopMediaProjection();
            }
        }, 1500);
    }

    private static void releaseVirtual() {
        if (null != virtualDisplay) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
    }

    private static void stopMediaProjection() {
        if (null != mediaProjection) {
            mediaProjection.stop();
            mediaProjection = null;
        }
    }

    //
    public static class SaveTask extends AsyncTask<Image, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Image... args) {
            if (null == args || 1 > args.length || null == args[0]) {
                return null;
            }

            Image image = args[0];

            int width;
            int height;
            try {
                width = image.getWidth();
                height = image.getHeight();
            } catch (IllegalStateException e) {
                return null;
            }

            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            // 每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            // 总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
            //
            /*if (width != screenWidth || rowPadding != 0) {
                int[] pixel = new int[width + rowPadding / pixelStride];
                bitmap.getPixels(pixel, 0, width + rowPadding / pixelStride, 0, 0, width + rowPadding / pixelStride, 1);
                int leftPadding = 0;
                int rightPadding = width + rowPadding / pixelStride;
                for (int i = 0; i < pixel.length; i++) {
                    if (pixel[i] != 0) {
                        leftPadding = i;
                        break;
                    }
                }
                for (int i = pixel.length - 1; i >= 0; i--) {
                    if (pixel[i] != 0) {
                        rightPadding = i;
                        break;
                    }
                }
                width = Math.min(width, screenWidth);
                if (rightPadding - leftPadding > width) {
                    rightPadding = width;
                }
                bitmap = Bitmap.createBitmap(bitmap, leftPadding, 0, rightPadding - leftPadding, height);
            }*/
            //
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (surface.isValid()) {
                surface.release();
            }
            isAquire.set(0);
            cutBitmap(bitmap);
            release();
        }

        private void cutBitmap(Bitmap bitmap) {
            Logger.d(TAG, "bitmap cuted first");
            Rect mRect = null;
            if (graphicPath != null) {
                mRect = new Rect(graphicPath.getLeft(), graphicPath.getTop(), graphicPath.getRight(), graphicPath.getBottom());
            }
            if (mRect != null) {

                if (mRect.left < 0)
                    mRect.left = 0;
                if (mRect.right < 0)
                    mRect.right = 0;
                if (mRect.top < 0)
                    mRect.top = 0;
                if (mRect.bottom < 0)
                    mRect.bottom = 0;
                int cut_width = Math.abs(mRect.left - mRect.right);
                int cut_height = Math.abs(mRect.top - mRect.bottom);
                if (cut_width > 0 && cut_height > 0) {
                    Bitmap cutBitmap = Bitmap.createBitmap(bitmap, mRect.left, mRect.top, cut_width, cut_height);
                    Logger.d(TAG, "bitmap cuted second");
                    if (graphicPath != null) {
                        // 准备画笔
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        paint.setColor(Color.WHITE);
                        Bitmap temp = Bitmap.createBitmap(cut_width, cut_height, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(temp);

                        Path path = new Path();
                        if (graphicPath.size() > 1) {
                            path.moveTo((float) ((graphicPath.pathX.get(0) - mRect.left)), (float) ((graphicPath.pathY.get(0) - mRect.top)));
                            for (int i = 1; i < graphicPath.size(); i++) {
                                path.lineTo((float) ((graphicPath.pathX.get(i) - mRect.left)), (float) ((graphicPath.pathY.get(i) - mRect.top)));
                            }
                        } else {
                            return;
                        }
                        canvas.drawPath(path, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                        // 关键代码，关于Xfermode和SRC_IN请自行查阅
                        canvas.drawBitmap(cutBitmap, 0, 0, paint);
                        Logger.d(TAG, "bitmap cuted third");

                        saveCutBitmap(temp);

                    } else {
                        saveCutBitmap(cutBitmap);
                    }
                }
            } else {
                saveCutBitmap(bitmap);
            }
            bitmap.recycle();//自由选择是否进行回收
        }

        private void saveCutBitmap(Bitmap cutBitmap) {
            File localFile = new File(createFile());
            String fileName = localFile.getAbsolutePath();
            try {
                if (!localFile.exists()) {
                    localFile.createNewFile();
                    Logger.d(TAG, "image file created");
                }
                FileOutputStream fileOutputStream = new FileOutputStream(localFile);
                if (fileOutputStream != null) {
                    cutBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Logger.e(TAG, "image file created error.");
                return;
            }
            Logger.e(TAG, "image file path: " + fileName);
        }

        // 输出目录
        private String createFile() {
            String outDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.US);
            String date = simpleDateFormat.format(new Date());
            return outDir + date + ".png";
        }
    }


}
