package com.dc.imagebanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/4/14 001
 * 图片轮播底部原点的实现
 * 1，借助于FrameLayout的特性，放置两个View在FrameLayout，后放的布局显示在上面
 * 2，底部圆点，素材用shape实现，分选中和正常两种状态
 * 3，图片轮播的ViewGroup核心类放在第一层，第二层放LinearLayout，第二层中水平放置圆点的ImageView
 * 4，根据轮播的点击事件，同时变换底部圆点的效果 通过接口Listener事件实现
 *
 */

public class ImageBannerFrameLayout extends FrameLayout implements ImageBannerViewGroup.ImageBannerViewGroupListener {
    ImageBannerViewGroup imageBannerViewGroup;
    LinearLayout linearLayout;
    public ImageBannerFrameLayout(@NonNull Context context) {
        super(context);

        initImageBannerViewGroup();
        initDotsImageLinearLayout();
    }

    public ImageBannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initImageBannerViewGroup();
        initDotsImageLinearLayout();
    }

    public ImageBannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initImageBannerViewGroup();
        initDotsImageLinearLayout();
    }

    public void addBitmapToImageViewGroup(Bitmap bitmap){
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bitmap);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        imageBannerViewGroup.addView(iv, lp);
    }

    public void addDotToLinearLayout(int index){
        ImageView iv = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        iv.setLayoutParams(lp);
        if (index == 0 ){
            iv.setImageResource(R.drawable.dot_select);
        } else {
            iv.setImageResource(R.drawable.dot_normal);
        }
        linearLayout.addView(iv);
    }

    public void initImageBannerViewGroup(){
        imageBannerViewGroup = new ImageBannerViewGroup(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageBannerViewGroup.setLayoutParams(lp);
        addView(imageBannerViewGroup);

        imageBannerViewGroup.setImageBannerViewGroupListener(this);
    }

    public void initDotsImageLinearLayout(){
        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 40);
        lp.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(lp);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        linearLayout.setBackgroundColor(Color.RED);

        addView(linearLayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            linearLayout.setAlpha(0.5f);
        } else {
            linearLayout.getBackground().setAlpha(100);
        }

//        imageBannerViewGroup.imageBannerViewGroupListener.selectImage(0);
        imageBannerViewGroup.performClick();

    }

    @Override
    public void selectImage(int index) {
        int count = linearLayout.getChildCount();
        for(int i = 0; i <count; i ++){
            ImageView iv = (ImageView) linearLayout.getChildAt(i);
            if (i == index)
                iv.setImageResource(R.drawable.dot_select);
            else
                iv.setImageResource(R.drawable.dot_normal);
        }
    }
}
