package com.dc.imagebanner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/4/8 0008.
 * 水平方向的图片轮动播放效果实现类
 * extends ViewGroup
 * 主要是onMeasure、onLayout方法，height=第一个元素的高度，宽度等于所有子View的宽度总和
 *onMeasure三步：
 * 1，得到子试图数目
 * 2、得到宽和高（调用measureChildren计算子View的宽高，这一步不可少）
 * 3，设置宽、高
 * onLayout步骤
 * 遍历调用子view.layout(),参数中计算好子view的l,t,r,b值
 */

public class ImageBannerViewGroup extends ViewGroup {
    int width;//父容器宽度
    int height;//父容器高度
    int children;//子控件个数
    int childWidth;//子容器宽度
    int childHeight;//子容器高度
    //屏幕宽度和高度
    int screenWidth;//屏幕宽度

    //实现自动轮播
    private Handler autoHandler;
    private Timer timer = new Timer();
    private TimerTask task;
    boolean isAuto = false;

    public ImageBannerViewGroupListener getImageBannerViewGroupListener() {
        return imageBannerViewGroupListener;
    }

    public void setImageBannerViewGroupListener(ImageBannerViewGroupListener imageBannerViewGroupListener) {
        this.imageBannerViewGroupListener = imageBannerViewGroupListener;
    }

    ImageBannerViewGroupListener imageBannerViewGroupListener;

    public ImageBannerViewGroup(Context context) {
        super(context);
        init(context);
    }

    public ImageBannerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageBannerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        Timer timer = new Timer();

        autoHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0:
                        index ++;
                        if (index > children) index = 0;
                        scrollTo(index * childWidth, 0);
                        imageBannerViewGroupListener.selectImage(index);
                        break;
                }
            }
        };

        task = new TimerTask() {
            @Override
            public void run() {
                while(isAuto) autoHandler.sendEmptyMessage(0);
            }
        };

        timer.schedule(task, 100, 2000);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1，得到子试图数目
        children = getChildCount();
        //2, 得到宽和高
        if (children == 0){
            width = 0;
            height = 0;
        }else {
            // 测量子试图们的宽度和高度，这一步不能忽略，否则下面得到的值都为0
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            View view = getChildAt(0);
            childWidth = view.getMeasuredWidth();
            if (childWidth < screenWidth){
                childWidth = screenWidth;
            }
            childHeight = view.getMeasuredHeight();
            width = view.getMeasuredWidth() * children;
            height = view.getMeasuredHeight();
        }
        //3, 设置宽和高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            int marginLeft = 0;
            if(children > 0){
                for(int i = 0; i < children; i ++){
                    View view = getChildAt(i);
                    view.layout(marginLeft, 0, marginLeft+ childWidth, childHeight);
                    marginLeft += view.getWidth();
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    int x;
    int y;
    int index;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                stopAuto();
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int mx = (int)event.getX();
                int dx = mx - x ;
                scrollBy(-dx, 0);
                x = mx;
                break;
            case MotionEvent.ACTION_UP:
//                startAuto();
                int scrollX = getScrollX();
                index = (scrollX + childWidth/2)/childWidth;
                if (index < 0) index = 0;
                if(index > children -1) index = children-1;
                scrollTo(index * childWidth, 0);
                imageBannerViewGroupListener.selectImage(index);

                break;
        }
        return  true;
    }

    public void startAuto(){
        isAuto = true;
    }

    public void stopAuto(){
        isAuto = false;
    }

    public interface ImageBannerViewGroupListener{
        void selectImage(int index);
    }
}
