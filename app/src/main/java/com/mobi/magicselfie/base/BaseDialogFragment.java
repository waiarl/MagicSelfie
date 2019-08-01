package com.mobi.magicselfie.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.mobi.magicselfie.R;
import com.mobi.magicselfie.view.BgSelectView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * Created by waiarl on 2018/7/11.
 */

public class BaseDialogFragment extends DialogFragment implements Constant {
    protected String TAG = getClass().getSimpleName() + "_TAG";//很多系统Log与sdkLog直接用的类名，容易搞混，所以添加个后缀

    public static final String BUNDLE_KEY_GRAVITY = "BUNDLE_KEY_GRAVITY";
    public static final String BUNDLE_KEY_WIDTH_MATCH = "BUNDLE_KEY_WIDTH_MATCH";
    public static final String BUNDLE_KEY_HEIGHT_MATCH = "BUNDLE_KEY_HEIGHT_MATCH";
    public static final String BUNDLE_KEY_FULLSCREEN = "BUNDLE_KEY_FULLSCREEN";//是否沉浸式
    public static final String BUNDLE_KEY_FROM = "BUNDLE_KEY_FROM";

    protected int gravity = Gravity.CENTER;
    protected boolean isFullScreen = false;
    protected BgSelectView.OnBgSelectViewListener bgSelectViewistener;
    private boolean isWidthMatch = false;//是否横向铺满
    protected Context mContext;
    private DialogInterface.OnDismissListener dismissListener;
    private DialogInterface.OnCancelListener cancelListener;
    private boolean isHeightMatch;
    private boolean isJustDialogDismiss;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle bundle = getArguments();
        if (bundle != null) {
            gravity = bundle.getInt(BUNDLE_KEY_GRAVITY, Gravity.CENTER);
            isWidthMatch = bundle.getBoolean(BUNDLE_KEY_WIDTH_MATCH, false);
            isHeightMatch = bundle.getBoolean(BUNDLE_KEY_HEIGHT_MATCH, false);
        }
        int theme = getThemeByGravity(gravity);
        final Dialog dialog = new Dialog(getActivity(), theme);
        final Window window = dialog.getWindow();
        window.setGravity(gravity);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //如果要全屏
        final WindowManager.LayoutParams params = window.getAttributes();
        if (isWidthMatch) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        if (isHeightMatch) {
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
        }
        window.setAttributes(params);
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (cancelListener != null) {
                    cancelListener.onCancel(dialog);
                }
            }
        });
        return dialog;
    }

    protected int getThemeByGravity(int gravity) {
       /* if (gravity == Gravity.BOTTOM) {
            return R.style.fragment_dialog_bottom_style;
        }*/
        return R.style.Dialog_Activity;
    }

    public BaseDialogFragment setDismissListener(DialogInterface.OnDismissListener listener) {
        this.dismissListener = listener;
        return this;
    }

    public BaseDialogFragment setOnCancelListener(DialogInterface.OnCancelListener listener) {
        this.cancelListener = listener;
        return this;
    }

    public int getColor(int res) {
        return ContextCompat.getColor(getContext(), res);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Bundle bundle = getArguments();
    }

    public boolean isShowing() {
        return isAdded() && getDialog() != null && getDialog().isShowing();
    }

    public void show(FragmentManager manager) {
        try {
            super.show(manager, getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void showAllowingStateLoss(FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, getClass().getSimpleName());
        ft.commitAllowingStateLoss();
    }

    public void onSave() {
        if (getDialog() != null) {
            isJustDialogDismiss = true;
            getDialog().dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (isJustDialogDismiss) {

        } else {
            super.onDismiss(dialog);
            if (dismissListener != null) {
                dismissListener.onDismiss(dialog);
            }
        }
        isJustDialogDismiss = false;
    }

    @Override
    public void dismiss() {
        isJustDialogDismiss = false;
        boolean hasDialog = getDialog() != null && getDialog().isShowing();
        super.dismiss();
        if (!hasDialog && dismissListener != null) {//说明前面调用过onSave方法,此时，是不走onDismiss方法的
            dismissListener.onDismiss(getDialog());

        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        isJustDialogDismiss = false;
        boolean hasDialog = getDialog() != null && getDialog().isShowing();
        super.dismissAllowingStateLoss();
        if (!hasDialog && dismissListener != null) {//说明前面调用过onSave方法,此时，是不走onDismiss方法的
            dismissListener.onDismiss(getDialog());

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
