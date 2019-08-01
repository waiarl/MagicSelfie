package com.mobi.magicselfie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.mobi.magicselfie.base.BaseActivity;
import com.mobi.magicselfie.base.MyApp;
import com.mobi.magicselfie.view.CameraViewGroup;

import androidx.annotation.Nullable;

/**
 * Created by waiarl on 2019-07-30.
 */
public class CameraActivity extends BaseActivity {
    private CameraViewGroup camera_view_group;
    private ImageView img_change;
    private ImageView img_close;
    private ImageView img_take;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        findViewById();
        setListener();
    }

    private void findViewById() {
        camera_view_group=findViewById(R.id.camera_view_group);
        img_change=findViewById(R.id.img_change);
        img_close=findViewById(R.id.img_close);
        img_take=findViewById(R.id.img_take);
    }

    private void setListener() {
       img_change.setOnClickListener(v->change());
       img_close.setOnClickListener(v->onBackPressed());
       img_take.setOnClickListener(v->take());
    }

    private void change() {
        camera_view_group.change();
    }

    private void take() {
        Bitmap bitmap=camera_view_group.getBitmap();
        MyApp.getInstance().setCurrentBitmap(bitmap);
        final Intent intent=new Intent(this,PhotoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera_view_group.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera_view_group.onPause();
    }
}
