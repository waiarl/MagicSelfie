package com.mobi.magicselfie.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobi.magicselfie.R;
import com.mobi.magicselfie.view.CameraView;

/**
 * Created by waiarl on 2019-07-30.
 */
public class CameraViewGroup extends ViewGroup {
    private final Context mContext;
    private CameraView cameraView;
    private ImageView img_frame;
    private int mWidth;
    private int mHeight;
    private int frameMargin;
    private int frameSize;

    public CameraViewGroup(Context context) {
        this(context, null);
    }

    public CameraViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        cameraView.measure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
        final int min = Math.min(mWidth, mHeight);
        final int size = Math.max(min - 2*frameMargin, 0);
        img_frame.measure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
    }

    private void init() {
        cameraView = new CameraView(mContext);
        img_frame = new ImageView(mContext);
        img_frame.setImageResource(R.mipmap.img_frame);
        frameMargin = dip2Px(20);

        addView(cameraView);
        addView(img_frame);
    }


    public Bitmap getBitmap() {
        final Bitmap src = cameraView.takeBitmap();
        if (src == null) {
            return null;
        }
        final Bitmap bitmap = Bitmap.createBitmap(src, (src.getWidth() - img_frame.getMeasuredWidth()) / 2, (src.getHeight() - img_frame.getMeasuredHeight()) / 2, img_frame.getMeasuredWidth(), img_frame.getMeasuredHeight());
        return bitmap;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        cameraView.layout(0, 0, mWidth, mHeight);
        img_frame.layout((mWidth - img_frame.getMeasuredWidth()) / 2, (mHeight - img_frame.getMeasuredHeight()) / 2, (mWidth + img_frame.getMeasuredWidth()) / 2, (mHeight + img_frame.getMeasuredHeight()) / 2);
    }

    int dip2Px(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    public void onResume() {
        cameraView.onResume();
    }

    public void onPause() {
        cameraView.onPause();
    }

    public void change() {
        cameraView.change();
    }
}
