package com.gogh.floattouchkey.uitls;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {

    private static final String TAG = "FileUtil";
    private static final String CAPTURE_PATH = "ScreenCapture" + File.separator;
    private static final String FILE_SUFFIX = ".png";
    private static String SDCARD_ROOT = null;
    //
    private static final String ASSET_ORC = "orc";
    private static final String TESSERACT = "tesseract/";
    private static final String TESSDATA = "tessdata/";
    // chi_tra
    public static final String DEFAULT_LANGUAGE = "chi_sim";
    public static final String DEFAULT_LANGUAGE_NAME = ".traineddata";

    public static void getAppPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        } else {
            SDCARD_ROOT = context.getFilesDir().toString() + File.separator;
        }
    }

    public static String getTessbasePath(Context context){
        getAppPath(context);
        File path = new File(SDCARD_ROOT + TESSERACT);
        if(!path.exists()){
            path.mkdirs();
        }
        Logger.d(TAG, "getTessbasePath :" + path.getAbsolutePath());
        return path.getAbsolutePath();
    }

    private static String getOcrDicPath(Context context, String originalName){
        File basePath = new File(getTessbasePath(context));
        File path = new File(basePath.getAbsolutePath() + File.separator + TESSDATA);
        if(!path.exists()){
            path.mkdirs();
        }

        Logger.d(TAG, "getOcrDicPath " + path.getAbsolutePath());

        File fileName = new File(path.getAbsolutePath() + File.separator + originalName);
        if(!fileName.exists()){
            try {
                boolean isCreate = fileName.createNewFile();
                if(isCreate){
                    Logger.d(TAG, "getOcrDicPath fileName " + fileName.getAbsolutePath());
                    return fileName.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                fileName.delete();
                fileName.createNewFile();
                return fileName.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String createFile(Context context) {
        if (SDCARD_ROOT == null) {
            getAppPath(context);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.CHINA);
        String date = simpleDateFormat.format(new Date());

        File path = new File(SDCARD_ROOT + CAPTURE_PATH + date);
        if (!path.exists()) {
            boolean mkdirs = path.mkdirs();
            if (!mkdirs) {
                Logger.d(TAG, "create file path error:" + path.getAbsolutePath());
            }
        }
        return SDCARD_ROOT + CAPTURE_PATH + date + FILE_SUFFIX;
    }

    public static void saveBitmap(Context context, Bitmap bitmap) {
        File localFile = new File(createFile(context));
        String fileName = localFile.getAbsolutePath();
        Logger.d(TAG, "saveBitmap filename : " + fileName);
        try {
            if (!localFile.exists()) {
                boolean newFile = localFile.createNewFile();
                if (!newFile) {
                    Logger.d(TAG, "saveBitmap file created error.");
                    return;
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(localFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            Logger.e(TAG, "saveBitmap file saved error:" + e.getMessage());
            return;
        }
        Logger.e(TAG, "saveBitmap file path: " + fileName);
    }

    /**
     * 按新的宽高缩放图片
     *
     * @param bm
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale = 0.6f;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        /*if (!bm.isRecycled()) {
            bm.recycle();
        }*/
        return newbm;
    }

    public static void copyFile(Context context) throws IOException {
        AssetManager mAssetManger = context.getAssets();
        String[] fileNames = mAssetManger.list(ASSET_ORC);
        for(String name : fileNames){
            InputStream is = mAssetManger.open(ASSET_ORC + File.separator + name);
            String fileName = getOcrDicPath(context, name);
            if(TextUtils.isEmpty(fileName)){
                Logger.e(TAG, "copy file failed.");
                return;
            }
            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        }
    }

}