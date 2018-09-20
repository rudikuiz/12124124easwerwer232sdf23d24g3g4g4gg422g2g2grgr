package com.piramidsoft.wablastadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;

public class FullscreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        ImageView imageView = (ImageView) findViewById(R.id.imgPhoto);

        String img = getIntent().getStringExtra("url");
        Glide.with(FullscreenImageActivity.this).load(img).into(imageView);
        imageView.setOnTouchListener(new ImageMatrixTouchHandler(imageView.getContext()));
    }
}
