package com.gogh.floattouchkey.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gogh.floattouchkey.R;

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

    private Context context;
    private MaterialDialog dialog;
    private Bitmap firstBitmap;
    private Bitmap secondBitmap;

    private List<View> viewList = new ArrayList<>();

    public CaptureDialog(Context context) {
        this.context = context;
    }

    public void setFirstBitmap(Bitmap firstBitmap){
        this.firstBitmap = firstBitmap;
    }

    public void setSecondBitmap(Bitmap secondBitmap){
        this.secondBitmap = secondBitmap;
    }

    public void show() {
        dialog = new MaterialDialog.Builder(context)
                .title("左右滑动，编辑图片或文字")
                .iconRes(android.R.drawable.ic_dialog_info)
                .backgroundColorRes(R.color.colorPrimary)
                .customView(R.layout.dialog_capture_operation_layout, false)
                .show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                ((Activity)context).finish();
            }
        });

        View operationView = dialog.getCustomView();
        if(operationView != null){
            final ViewPager viewPager = (ViewPager) operationView.findViewById(R.id.dialog_capture_operation_viewpager);

            View first = LayoutInflater.from(context).inflate(R.layout.dialog_capture_text_layuout, null);
            View second = LayoutInflater.from(context).inflate(R.layout.dialog_capture_image_layout, null);
            viewList.add(first);
            viewList.add(second);


            ImageView firstImage = (ImageView) first.findViewById(R.id.dialog_capture_operation_first_image);
            ImageView secondImage = (ImageView) second.findViewById(R.id.dialog_capture_operation_second_image);

            firstImage.setImageBitmap(firstBitmap);
            secondImage.setImageBitmap(secondBitmap);

            viewPager.setAdapter(pagerAdapter);
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
