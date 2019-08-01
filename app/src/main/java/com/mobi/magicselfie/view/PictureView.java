package com.mobi.magicselfie.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.mobi.magicselfie.R;
import com.mobi.magicselfie.base.BitmapUtils;
import com.mobi.magicselfie.base.MyApp;
import com.mobi.magicselfie.base.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by waiarl on 2019-07-31.
 */
public class PictureView extends View {
    private final Context mContext;
    private PictureMode pictureMode;
    private Bitmap bitmap;
    private int mWidth;
    private int mHeight;
    private Bitmap bgBitmap;
    private Bitmap headBitmap;
    private boolean hasMark = true;
    private TextPaint mMarkPaint;
    private String markText;
    private int markMargin;

    public PictureView(Context context) {
        this(context, null);
    }

    public PictureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private void init() {
        mMarkPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mMarkPaint.setColor(Color.WHITE);
        mMarkPaint.setTextSize(Utils.dip2Px(30));
        mMarkPaint.setShadowLayer(Utils.dip2Px(4), 0, 0, Color.WHITE);

        markText = mContext.getString(R.string.app_name);
        markMargin = Utils.dip2Px(20);
    }

    public PictureView initView(PictureMode pictureMode) {
        if (this.pictureMode == pictureMode) {
            return this;
        }
        onDestroy();
        this.pictureMode = pictureMode;
        invalidate();
        return this;
    }

    public PictureView setHasMark(boolean has) {
        this.hasMark = has;
        return this;
    }

    public boolean hasMark(){
        return hasMark;
    }

    public Bitmap getSaveBitmap() {
        if (bitmap == null) {
            return null;
        }
        final Bitmap saveBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(saveBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        if (hasMark) {
            drawMark(canvas);
        }
        return saveBitmap;
    }

    private void drawMark(Canvas canvas) {
        StaticLayout markLayout = new StaticLayout(markText, mMarkPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
        final float width = markLayout.getLineWidth(0);
        final float height = markLayout.getLineBottom(0);
        final float l = mWidth - width - markMargin;
        final float t = mHeight - height - markMargin;
        canvas.save();
        canvas.translate(l, t);
        markLayout.draw(canvas);
        canvas.restore();
    }


    /**************************************************************/
    @Override
    protected void onDraw(Canvas canvas) {
        if (pictureMode == null || !MyApp.getInstance().hasBitmap()) {
            return;
        }
        drawPicture(canvas);
    }

    private void drawPicture(Canvas canvas) {
        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            if (bgBitmap == null || bgBitmap.isRecycled()) {

                final Bitmap bm = BitmapUtils.createBitmap(0, 0, pictureMode.path);
                if (bm == null) {
                    return;
                }

                final int bgWidth = bm.getWidth();
                final int bgHeight = bm.getHeight();
                final float sw = mWidth / (float) bgWidth;
                final float sh = mHeight / (float) bgHeight;
                final float sc = Math.max(sw, sh);
                final Matrix matrix = new Matrix();
                matrix.postScale(sc, sc);
                bgBitmap = Bitmap.createBitmap(bm, 0, 0, bgWidth, bgHeight, matrix, false);

                final float l = (mWidth - bgBitmap.getWidth()) / 2f;
                final float t = (mHeight - bgBitmap.getHeight()) / 2f;
                final RectF rectF = new RectF(pictureMode.headRectF);
                scaleRect(rectF, sc);
                final Bitmap hbm = MyApp.getInstance().getCurrentBitmap();
                final int hw = hbm.getWidth();
                final int hh = hbm.getHeight();
                final float hsc = Math.max(rectF.width() / hw, rectF.height() / hh);
                final Matrix hm = new Matrix();
                hm.postScale(hsc, hsc);
                headBitmap = Bitmap.createBitmap(hbm, 0, 0, hw, hh, hm, false);


                Canvas c = new Canvas(bitmap);
                c.save();
                c.translate(l, t);
                c.drawBitmap(headBitmap, rectF.centerX() - headBitmap.getWidth() / 2f, rectF.centerY() - headBitmap.getHeight() / 2f, null);
                c.drawBitmap(bgBitmap, 0, 0, null);
                c.restore();
            }
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }


    private void scaleRect(@NonNull RectF rectF, float scale) {
        if (scale != 1) {
            rectF.left = rectF.left * scale;
            rectF.top = rectF.top * scale;
            rectF.right = rectF.right * scale;
            rectF.bottom = rectF.bottom * scale;
        }
    }

    public void onDestroy() {
        if (bitmap != null) {
            bitmap.recycle();
        }
        bitmap = null;
        if (bgBitmap != null) {
            bgBitmap.recycle();
        }
        bgBitmap = null;

        if (headBitmap != null) {
        }
        headBitmap = null;
    }

}
