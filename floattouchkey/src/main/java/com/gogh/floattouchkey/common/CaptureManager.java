package com.gogh.floattouchkey.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Handler;

import com.gogh.floattouchkey.uitls.FileUtil;
import com.gogh.floattouchkey.uitls.Logger;
import com.gogh.floattouchkey.uitls.Screen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.xiaomi.ad.common.GlobalHolder.getApplicationContext;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 5/9/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/9/2018 do fisrt create. </li>
 */
public class CaptureManager {

    private static final String TAG = "CaptureManager";

    private Context context;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private Intent mResultData = null;
    private ImageReader mImageReader;

    public CaptureManager(Context context, Intent mResultData) {
        this.context = context;
        this.mResultData = mResultData;
        createImageReader(context);
    }

    private void createImageReader(Context context) {
        mImageReader = ImageReader.newInstance(Screen.getWidth(context),
                Screen.getHeight(context), PixelFormat.RGBA_8888, 2);
    }

    public void startScreenShot() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取当前屏幕内容
                startVirtual();
            }
        }, 5);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //生成图片保存到本地
                startCapture();

            }
        }, 100);
    }

    private void startCapture() {

        final Image image = mImageReader.acquireLatestImage();

        if (image == null) {
            startScreenShot();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doInBackground(image);
                }
            }).start();
        }
    }

    public void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                Screen.getWidth(context), Screen.getHeight(context),
                Screen.getDensity(context), DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    public void setUpMediaProjection() {
        if (mResultData == null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);
        } else {
            //mResultData是在Activity中用户授权后返回的结果
            mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, mResultData);
        }
    }

    private MediaProjectionManager getMediaProjectionManager() {
        return (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private Bitmap doInBackground(Image image) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        //每个像素的间距
        int pixelStride = planes[0].getPixelStride();
        //总的间距
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        File fileImage = null;
        if (bitmap != null) {
            try {
                fileImage = new File(FileUtil.getScreenShotsName(getApplicationContext()));
                if (!fileImage.exists()) {
                    fileImage.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(fileImage);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileImage);
                media.setData(contentUri);
                context.sendBroadcast(media);
            } catch (IOException e) {
                e.printStackTrace();
                fileImage = null;
            }
        }
        if (fileImage != null) {
            Logger.d(TAG, "fileImage " + fileImage.getAbsolutePath());
            return bitmap;
        }
        return null;
    }

    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }
}
