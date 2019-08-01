package com.mobi.magicselfie.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static com.mobi.magicselfie.view.CameraView.CAMERA_STATE.STATE_PICTURE_TAKEN;
import static com.mobi.magicselfie.view.CameraView.CAMERA_STATE.STATE_PREVIEW;
import static com.mobi.magicselfie.view.CameraView.CAMERA_STATE.STATE_WAITING_LOCK;
import static com.mobi.magicselfie.view.CameraView.CAMERA_STATE.STATE_WAITING_NON_PRECAPTURE;
import static com.mobi.magicselfie.view.CameraView.CAMERA_STATE.STATE_WAITING_PRECAPTURE;

/**
 * Created by waiarl on 2019-07-30.
 */
public class CameraView extends TextureView implements TextureView.SurfaceTextureListener {
    private final Context mContext;
    private boolean front = true;
    private int mWidth;
    private int mHeight;
    private Size mPreviewSize;
    private Size mCaptureSize;
    private String mCameraId;
    private ImageReader mImageReader;
    private HandlerThread mCameraThread;
    private Handler mCameraHandler;
    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mStateCallback;
    private CaptureRequest.Builder mCaptureRequestBuild;
    private CaptureRequest mCaptureRequest;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraCaptureSession.CaptureCallback mCaptureCallback;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        setListener();
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mWidth = width;
        mHeight = height;


        openCamera(width,height);
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**************************************************************************************************************************/

    public void onResume() {
        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
        if (isAvailable()) {
           openCamera(mWidth,mHeight);

        } else {
            setSurfaceTextureListener(this);

        }
    }

    public void onPause() {
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        stopBackgroundThread();
    }
    private void stopBackgroundThread() {
        mCameraThread.quitSafely();
        try {
            mCameraThread.join();
            mCameraThread = null;
            mCameraThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Bitmap takeBitmap() {
        final Bitmap bitmap = getBitmap();
        final Matrix matrix=getTransform(null);
        final Bitmap bitmap1= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
        return bitmap1;
    }

    /**************************************************************************************************************************/
    private void init() {
        setSurfaceTextureListener(this);
        onResume();
    }

    private void setListener() {
        mStateCallback = new CameraDevice.StateCallback() {

            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                mCameraDevice = camera;
                startPreview();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                camera.close();
                mCameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                camera.close();
                mCameraDevice = null;
            }
        };
        mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                super.onCaptureProgressed(session, request, partialResult);
                capture(session, request, partialResult);
            }

            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                capture(session, request, result);
            }
        };
    }

    private void capture(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {

    }

    private void startPreview() {
        if (mPreviewSize == null) {
            return;
        }
        SurfaceTexture surfaceTexture = getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        final Surface previewSurface = new Surface(surfaceTexture);

        try {
            mCaptureRequestBuild = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuild.addTarget(previewSurface);
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mCaptureRequest = mCaptureRequestBuild.build();
                    mCameraCaptureSession = session;
                    try {
                        mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, mCaptureCallback, mCameraHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, mCameraHandler);
        } catch (CameraAccessException e) {


        }
    }


    @SuppressLint("MissingPermission")
    private void openCamera(int width, int height) {
        setUpCamera(width, height);
        configureTransform(width, height);

        if (TextUtils.isEmpty(mCameraId)) {
            return;
        }
        final CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        if (hasCameraPermission()) {
            try {
                manager.openCamera(mCameraId, mStateCallback, mCameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasCameraPermission() {
        final int check = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        boolean result = check == PackageManager.PERMISSION_GRANTED;
        return result;
    }

    private void setUpCamera(int width, int height) {
        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing == null) {
                    continue;
                }
                if (!(front && facing == CameraCharacteristics.LENS_FACING_FRONT) && !(!front && facing == CameraCharacteristics.LENS_FACING_BACK)) {
                    continue;
                }
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;
                mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                mCaptureSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new SizeComparator());

                setUpImageReader();
                mCameraId = cameraId;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void configureTransform(int width, int height) {
        Activity activity = (Activity) mContext;
        if ( null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, width, height);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) width / mPreviewSize.getHeight(),
                    (float) width / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        setTransform(matrix);
    }


    private void setUpImageReader() {
    }

    private Size getOptimalSize(Size[] outputSizes, int width, int height) {
        final List<Size> sizeList = new ArrayList<>();
        for (Size option : outputSizes) {
            if (width > height) {
                if (option.getWidth() >= width && option.getHeight() >= height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() >= height && option.getHeight() >= width) {
                    sizeList.add(option);
                }
            }
        }
        Size result = outputSizes[0];
        if (sizeList.size() > 0) {
            result = Collections.min(sizeList, new SizeComparator());
        }
        return result;
    }

    public void change() {
        front=!front;
        onPause();
        onResume();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_PREVIEW,
            STATE_WAITING_LOCK,
            STATE_WAITING_PRECAPTURE,
            STATE_WAITING_NON_PRECAPTURE,
            STATE_PICTURE_TAKEN})
    public @interface CAMERA_STATE {
        int STATE_PREVIEW = 0;
        int STATE_WAITING_LOCK = 1;
        int STATE_WAITING_PRECAPTURE = 2;
        int STATE_WAITING_NON_PRECAPTURE = 3;
        int STATE_PICTURE_TAKEN = 4;
    }

    static class SizeComparator implements Comparator<Size> {

        @Override
        public int compare(Size o1, Size o2) {
            return Long.signum(o1.getWidth() * o1.getHeight() - o2.getWidth() * o2.getHeight());
        }
    }
}
