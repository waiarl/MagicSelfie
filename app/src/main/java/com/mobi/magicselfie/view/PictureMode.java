package com.mobi.magicselfie.view;

import android.graphics.RectF;

import com.mobi.magicselfie.base.BaseMode;

import androidx.annotation.DrawableRes;

/**
 * Created by waiarl on 2019-07-31.
 */
public class PictureMode extends BaseMode {
    public int id;
    public String path;
    public RectF headRectF = new RectF();
    public boolean selected=false;
    public int selectBg;
    public int unSelectBg;

    public PictureMode(int id, String path, RectF headRectF) {
        this.id = id;
        this.path = path;
        this.headRectF.set(headRectF);
    }
    public PictureMode(int id, String path, RectF headRectF,@DrawableRes int selectBg,@DrawableRes int unSelectBg,boolean selected) {
        this.id = id;
        this.path = path;
        this.headRectF.set(headRectF);
        this.selected=selected;
        this.selectBg=selectBg;
        this.unSelectBg=unSelectBg;
    }
}
