package com.mobi.magicselfie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.mobi.magicselfie.base.BaseActivity;
import com.mobi.magicselfie.base.ToastUtils;
import com.mobi.magicselfie.base.Utils;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by waiarl on 2019-07-31.
 */
public class MainActivity extends BaseActivity {
    private ImageView img_start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        setListener();
        initState();
    }

    private void findViewById() {
        img_start = findViewById(R.id.img_start);
    }

    private void setListener() {
        img_start.setOnClickListener(v -> gotoCamera());
    }

    private void initState() {

    }

    private void gotoCamera() {
        if (!Utils.hasCameraPermission()) {
            ToastUtils.showToast(R.string.camera_permission);
            return;
        }
        final Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }
}
