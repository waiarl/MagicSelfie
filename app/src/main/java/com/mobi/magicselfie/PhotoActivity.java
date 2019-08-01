package com.mobi.magicselfie;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.mobi.magicselfie.base.BaseActivity;
import com.mobi.magicselfie.base.BaseObserver;
import com.mobi.magicselfie.base.MyApp;
import com.mobi.magicselfie.base.StringUtils;
import com.mobi.magicselfie.base.ToastUtils;
import com.mobi.magicselfie.base.Utils;
import com.mobi.magicselfie.view.BgSelectView;
import com.mobi.magicselfie.view.PictureMode;
import com.mobi.magicselfie.view.PictureView;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by waiarl on 2019-07-31.
 */
public class PhotoActivity extends BaseActivity {
    private ImageView img_photo;
    private PictureView picture_view;
    private View img_back;
    private View img_save;
    private View img_select;
    private BgSelectDialog selectDialog;
    private ImageView img_mark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        findViewById();
        setListener();
        initViewState();
    }

    private void initViewState() {
        final PictureMode mode = getSelectMode();
        picture_view.initView(mode);

    }

    private PictureMode getSelectMode() {
        PictureMode mode = PICTURE_MODES.get(0);
        for (PictureMode m : PICTURE_MODES) {
            if (m.selected) {
                mode = m;
                break;
            }
        }
        return mode;
    }

    private void findViewById() {
        picture_view = findViewById(R.id.picture_view);
        img_back = findViewById(R.id.img_back);
        img_save = findViewById(R.id.img_save);
        img_select = findViewById(R.id.img_select);
        img_mark = findViewById(R.id.img_mark);
    }

    private void setListener() {
        img_back.setOnClickListener(v -> onBackPressed());
        img_save.setOnClickListener(v -> savePicture());
        img_select.setOnClickListener(v -> showSelectDialog());
        img_mark.setOnClickListener(v -> changeMark());
    }

    private void changeMark() {
        boolean mark = !picture_view.hasMark();
        picture_view.setHasMark(mark);
        final int res = mark ? R.mipmap.img_mark_s : R.mipmap.img_mark_n;
        img_mark.setImageResource(res);
    }

    private void showSelectDialog() {
        if (selectDialog != null && selectDialog.isShowing()) {
            return;
        }
        selectDialog = BgSelectDialog.newInstance(null);
        selectDialog.showAllowingStateLoss(getSupportFragmentManager());
        selectDialog.setOnBgSelectViewistener(new BgSelectView.OnBgSelectViewListener() {
            @Override
            public void onCloseClick() {
                selectDialog.dismissAllowingStateLoss();
            }

            @Override
            public void onSelectClick(PictureMode mode) {
                selectDialog.dismissAllowingStateLoss();
                changeBg(mode);
            }
        });
    }

    private void changeBg(PictureMode mode) {
        picture_view.initView(mode);
    }


    private void savePicture() {
        if (!Utils.hasReadAndWritePermission()) {
            ToastUtils.showToast(R.string.need_write_permission);
            return;
        }
        final Bitmap bitmap = picture_view.getSaveBitmap();
        if (bitmap == null) {
            ToastUtils.showToast(R.string.save_failed);
        }
        Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                        final String fileName = StringUtils.getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg";
                        MediaStore.Images.Media.insertImage(MyApp.getInstance().getContentResolver(), bitmap, fileName, fileName);
                        emitter.onNext(true);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        ToastUtils.showToast(R.string.save_success);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        picture_view.onDestroy();
        super.onDestroy();
    }
}
