package com.mobi.magicselfie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.magicselfie.base.BaseDialogFragment;
import com.mobi.magicselfie.view.BgSelectView;
import com.mobi.magicselfie.view.PictureMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by waiarl on 2019-08-01.
 */
public class BgSelectDialog extends BaseDialogFragment {
    private BgSelectView bg_select_view;

    public static BgSelectDialog newInstance(Bundle bundle) {
        BgSelectDialog dialog = new BgSelectDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_bg_select,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        setListener();
    }

    private void findViewById(View view) {
        bg_select_view=view.findViewById(R.id.bg_select_view);
    }

    private void setListener() {
        bg_select_view.setOnBgSelectViewistener(new BgSelectView.OnBgSelectViewListener() {
            @Override
            public void onCloseClick() {
                if (bgSelectViewistener != null) {
                    bgSelectViewistener.onCloseClick();
                }
            }

            @Override
            public void onSelectClick(PictureMode mode) {
                if (bgSelectViewistener != null) {
                    bgSelectViewistener.onSelectClick(mode);
                }
            }
        });
    }


    public void setOnBgSelectViewistener(BgSelectView.OnBgSelectViewListener bgSelectViewistener) {
        this.bgSelectViewistener = bgSelectViewistener;
    }
}
