package com.dc.imagebanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

//    ImageBannerViewGroup imageViewGroup;
    ImageBannerFrameLayout imageBannerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        imageViewGroup = (ImageBannerViewGroup)findViewById(R.id.imageViewGroup);
        imageBannerFrameLayout = (ImageBannerFrameLayout) findViewById(R.id.imageViewGroup);

        int [] ids = new int[]{R.drawable.iphone, R.drawable.ipad, R.drawable.meizi, R.drawable.start};

//        for(int i = 0; i < ids.length; i ++){
//            ImageView iv = new ImageView(this);
//            iv.setImageResource(ids[i]);
//            imageViewGroup.addView(iv);
//        }

        for(int i = 0; i < ids.length; i ++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[i]);
            imageBannerFrameLayout.addBitmapToImageViewGroup(bitmap);
            imageBannerFrameLayout.addDotToLinearLayout(i);
        }
    }
}
