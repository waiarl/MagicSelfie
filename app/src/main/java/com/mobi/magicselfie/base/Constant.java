package com.mobi.magicselfie.base;

import android.Manifest;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;

import com.mobi.magicselfie.R;
import com.mobi.magicselfie.view.PictureMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.IntDef;

import static com.mobi.magicselfie.base.Constant.PICTURE.CI_KE_XIN_TIAO;
import static com.mobi.magicselfie.base.Constant.PICTURE.DIE_ZHONG_DIE;
import static com.mobi.magicselfie.base.Constant.PICTURE.GANG_TIE_XIA;
import static com.mobi.magicselfie.base.Constant.PICTURE.YU_HANG_YUAN;

/**
 * Created by waiarl on 2019/5/23.
 */
public interface Constant {
    Random mRandom = new Random();
    LinearInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();


    int REQUEST_CODE_ADD = 10001;
    int RESULT_CODE_ADD = 20001;

    String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA};
    String[] READ_AND_WRITE_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    String[] PICTURE_PATH = {"cikexintiao.png",
            "diezhongdie.png",
            "gangtiexia.png",
            "yuhangyuan.png"};

    List<PictureMode> PICTURE_MODES = new ArrayList<PictureMode>() {
        {
            add(new PictureMode(CI_KE_XIN_TIAO, PICTURE_PATH[0], new RectF(1190, 246, 1408, 467), R.mipmap.img_cike_s, R.mipmap.img_cike_n, true));
            add(new PictureMode(DIE_ZHONG_DIE, PICTURE_PATH[1], new RectF(894, 60, 1037, 234), R.mipmap.img_diezhongdie_s, R.mipmap.img_diezhongdie_n, false));
            add(new PictureMode(GANG_TIE_XIA, PICTURE_PATH[2], new RectF(645, 116, 1336, 1070), R.mipmap.img_gangtiexia_s, R.mipmap.img_gangtiexia_n, false));
            add(new PictureMode(YU_HANG_YUAN, PICTURE_PATH[3], new RectF(1242, 173, 1349, 340), R.mipmap.img_yuhangyuan_s, R.mipmap.img_yuhangyuan_n, false));
        }

    };

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CI_KE_XIN_TIAO, DIE_ZHONG_DIE, GANG_TIE_XIA, YU_HANG_YUAN})
    @interface PICTURE {
        int CI_KE_XIN_TIAO = 0;
        int DIE_ZHONG_DIE = 1;
        int GANG_TIE_XIA = 2;
        int YU_HANG_YUAN = 3;
    }

}
