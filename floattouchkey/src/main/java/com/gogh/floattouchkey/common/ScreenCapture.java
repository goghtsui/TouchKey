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
import android.os.Handler;
import android.view.Surface;

import com.gogh.floattouchkey.entity.GraphicPath;
import com.gogh.floattouchkey.uitls.FileUtil;
import com.gogh.floattouchkey.uitls.Logger;
import com.gogh.floattouchkey.uitls.Screen;
import com.gogh.floattouchkey.view.CaptureDialog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScreenCapture {

    private static final String TAG = "ScreenCapture";

    private Context context;
    private Surface surface;
    private Intent resultData;
    private int screenDensity;
    private int screenWidth;
    private int screenHeight;

    private CaptureDialog captureDialog;

    private int mEventType = Event.CAPTURE_NONE;
    private GraphicPath graphicPath;
    private ImageReader imageReader;
    private VirtualDisplay virtualDisplay;
    private MediaProjection mediaProjection;

    private AtomicInteger isAquire = new AtomicInteger(0);

    private ScreenCapture() {
    }

    public static ScreenCapture get() {
        return SingleHolder.HOLDER;
    }

    public void init(Context context, GraphicPath graphicPath, Intent data, int eventType) {
        this.context = context;
        this.resultData = data;
        this.mEventType = eventType;
        this.graphicPath = graphicPath;
        captureDialog = new CaptureDialog(context);
        setUpMediaProjection();
        initScreenSize();
        createImageReader();
        beginScreenShot();
    }

    private void setUpMediaProjection() {
        if (resultData == null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);
        } else {
            mediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK,
                    resultData);
        }
    }

    private void initScreenSize() {
        screenDensity = Screen.get().getDensity();
        screenWidth = Screen.get().getWidth();
        screenHeight = Screen.get().getHeight();
    }

    private void createImageReader() {
        imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1);
    }

    private void beginScreenShot() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                beginVirtual();
            }
        }, 0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                beginCapture();
            }
        }, 150);
    }

    private void beginVirtual() {
        if (null != mediaProjection) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    private void virtualDisplay() {
        surface = imageReader.getSurface();
        virtualDisplay = mediaProjection.createVirtualDisplay("screen-mirror", screenWidth,
                screenHeight, screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, surface,
                null, null);
    }

    private MediaProjectionManager getMediaProjectionManager() {
        return (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private void beginCapture() {
        if (isAquire.getAndSet(1) == 0) {
            Logger.d(TAG, "beginCapture");
            Image image;
            image = imageReader.acquireLatestImage();
            if (image == null) {
                isAquire.set(0);
                beginScreenShot();
            } else {
                acquireImage(image);
            }
        }
    }

    private void release() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                releaseVirtual();
                stopMediaProjection();
            }
        }, 1500);
    }

    private void releaseVirtual() {
        if (null != virtualDisplay) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
    }

    private void stopMediaProjection() {
        if (null != mediaProjection) {
            mediaProjection.stop();
            mediaProjection = null;
        }
    }

    private void acquireImage(final Image image) {
        Flowable.create(new FlowableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(FlowableEmitter<Bitmap> e) {
                int width = image.getWidth();
                int height = image.getHeight();

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
                e.onNext(bitmap);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        Logger.d(TAG, "onNext");
                        if (surface.isValid()) {
                            surface.release();
                        }
                        isAquire.set(0);
                        captureDialog.setFirstBitmap(bitmap);
                        // 保存全屏截图
                        FileUtil.saveBitmap(context, bitmap);
                        // 裁剪指定区域截图
                        cutBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logger.d(TAG, "onError " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d(TAG, "onComplete");
                        release();
                    }
                });
    }

    private void cutBitmap(Bitmap bitmap) {
        Logger.d(TAG, "bitmap cuted first");
        Rect mRect = null;
        if (graphicPath != null) {
            mRect = new Rect(graphicPath.getLeft(), graphicPath.getTop(), graphicPath.getRight(), graphicPath.getBottom());
        }
        if (mRect != null) {
            if (mRect.left < 0) {
                mRect.left = 0;
            }
            if (mRect.right < 0) {
                mRect.right = 0;
            }
            if (mRect.top < 0) {
                mRect.top = 0;
            }
            if (mRect.bottom < 0) {
                mRect.bottom = 0;
            }

            int cutWidth = Math.abs(mRect.left - mRect.right);
            int cutHeight = Math.abs(mRect.top - mRect.bottom);
            if (cutWidth > 0 && cutHeight > 0) {
                Bitmap cutBitmap = Bitmap.createBitmap(bitmap, mRect.left, mRect.top, cutWidth, cutHeight);
                Logger.d(TAG, "bitmap cuted second");
                if (graphicPath != null) {
                    // 准备画笔
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    paint.setColor(Color.WHITE);
                    Bitmap temp = Bitmap.createBitmap(cutWidth, cutHeight, Bitmap.Config.ARGB_8888);
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

                    // 关键代码，Xfermode和SRC_IN
                    canvas.drawBitmap(cutBitmap, 0, 0, paint);
                    Logger.d(TAG, "bitmap cuted third");
                    captureDialog.setSecondBitmap(temp);
                    FileUtil.saveBitmap(context, temp);
                }
            }
        }
//        ((Activity)context).finish();
        captureDialog.show();
//        bitmap.recycle();//自由选择是否进行回收
    }

    private static final class SingleHolder {
        protected static final ScreenCapture HOLDER = new ScreenCapture();
    }
}