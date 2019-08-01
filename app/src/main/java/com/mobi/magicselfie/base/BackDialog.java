package com.mobi.magicselfie.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mobi.magicselfie.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by waiarl on 2019/5/16.
 */
public class BackDialog extends BaseDialogFragment {
    private TextView txt_cancel;
    private TextView txt_back;
    private OnBackEventListener listener;
    private RelativeLayout rel_native;

    public static BackDialog newInstance(Bundle bundle) {
        BackDialog dialog = new BackDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        setListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        AddManager.getInstance().showNativeAD( rel_native, null);
    }

    private void findViewById(View view) {
        txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_back = view.findViewById(R.id.txt_back);
        rel_native = view.findViewById(R.id.rel_native);
    }

    private void setListener() {
        txt_cancel.setOnClickListener((v) -> {
            dismissAllowingStateLoss();
            if (listener != null) {
                listener.onCancelClick(v);
            }
        });
        txt_back.setOnClickListener((v) -> {
            dismissAllowingStateLoss();
            if (listener != null) {
                listener.onBackClick(v);
            }
        });
    }

    public BackDialog setOnBackEventListener(OnBackEventListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnBackEventListener {
        void onBackClick(View v);

        void onCancelClick(View v);
    }

}
