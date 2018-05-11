package com.gogh.floattouchkey.uitls;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {

    private static final String TAG = "FileUtil";
    private static String SDCARD_ROOT = null;
    private static final String CAPTURE_PATH = "ScreenCapture" + File.separator;
    private static final String FILE_SUFFIX = ".png";

    public static void getAppPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            SDCARD_ROOT =  Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        } else {
            SDCARD_ROOT = context.getFilesDir().toString() + File.separator;
        }
    }

    private static String createFile(Context context) {
        if(SDCARD_ROOT == null){
            getAppPath(context);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.CHINA);
        String date = simpleDateFormat.format(new Date());

        File path = new File(SDCARD_ROOT + CAPTURE_PATH + date);
        if(!path.exists()){
            boolean mkdirs = path.mkdirs();
            if(!mkdirs){
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

}