package com.gogh.floattouchkey.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.uitls.FileUtil;
import com.gogh.floattouchkey.uitls.ImgPretreatment;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 5/11/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/11/2018 do fisrt create. </li>
 */
public class CaptureDialog {

    private static final String TAG = "CaptureDialog";

    private Context context;
    private MaterialDialog dialog;
    private Bitmap firstBitmap;

    private List<View> viewList = new ArrayList<>();

    public void init(Context context) {
        this.context = context;
    }

    public void setFirstBitmap(Bitmap firstBitmap){
        this.firstBitmap = firstBitmap;
    }

    public void show(final Context captureContext) {
        dialog = new MaterialDialog.Builder(captureContext)
                .title("左右滑动，编辑图片或文字")
                .iconRes(R.drawable.ic_capture_alert)
                .customView(R.layout.dialog_capture_operation_layout, false)
                .show();
        View operationView = dialog.getCustomView();
        if(operationView != null){
            final ViewPager viewPager = (ViewPager) operationView.findViewById(R.id.dialog_capture_operation_viewpager);

            View first = LayoutInflater.from(context).inflate(R.layout.dialog_capture_text_layuout, null);
            View second = LayoutInflater.from(context).inflate(R.layout.dialog_capture_image_layout, null);
            viewList.add(first);
            viewList.add(second);

            ImageView firstImage = (ImageView) first.findViewById(R.id.dialog_capture_operation_first_image);
            TextView ocrText = (TextView) second.findViewById(R.id.dialog_capture_operation_ocr_text);

            firstImage.setImageBitmap(FileUtil.scaleBitmap(firstBitmap));
            viewPager.setAdapter(pagerAdapter);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(firstBitmap != null){
                        firstBitmap.recycle();
                    }
                    ((Activity)captureContext).finish();
                }
            });

            ocrText.setText(ImgPretreatment.doOcr(firstImage, firstBitmap));
        }
    }

    public void release(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    private PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    };

}
