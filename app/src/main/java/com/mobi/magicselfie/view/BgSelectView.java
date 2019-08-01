package com.mobi.magicselfie.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.mobi.magicselfie.R;
import com.mobi.magicselfie.base.Constant;
import com.mobi.magicselfie.base.Utils;
import com.mobi.magicselfie.recycler.BaseRecyclerViewAdapter;
import com.mobi.magicselfie.recycler.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by waiarl on 2019-07-31.
 */
public class BgSelectView extends RelativeLayout implements Constant {
    private final Context mContext;
    private int mHeight;
    private int mWidth;
    private RecyclerView recyclerView;
    public View img_close;
    public View img_select;
    public List<PictureMode> list = new ArrayList<>();
    private LinearLayoutManager manager;
    private BgSelectAdapter adapter;
    private OnBgSelectViewListener bgSelectViewistener;

    public BgSelectView(Context context) {
        this(context, null);
    }

    public BgSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BgSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        setListener();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = mHeight * 1130 / 820;
        setMeasuredDimension(mWidth, mHeight);
        requestLayout();
    }

    private void init() {
        inflate(mContext, R.layout.view_bg_select, this);
        recyclerView = findViewById(R.id.recyclerView);
        img_close = findViewById(R.id.img_close);
        img_select = findViewById(R.id.img_select);

        initAdapter();

    }

    private void initAdapter() {
        list.clear();
        list.addAll(PICTURE_MODES);
        manager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        adapter = new BgSelectAdapter(list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        final int divider = Utils.dip2Px(15);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.set(0, 0, divider, 0);
            }
        });
    }

    private void setListener() {
        adapter.setOnRecyclerViewItemClickListener(new BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onItemSelected(position);
            }
        });
        img_close.setOnClickListener(v -> {
            if (bgSelectViewistener != null) {
                bgSelectViewistener.onCloseClick();
            }
        });
        img_select.setOnClickListener(v ->onSelected());
    }

    private void onSelected() {
        PictureMode mode=list.get(0);
        for(PictureMode m:list){
            if(m.selected){
                mode=m;
                break;
            }
        }
        if(bgSelectViewistener!=null){
            bgSelectViewistener.onSelectClick(mode);
        }
    }

    private void onItemSelected(int position) {
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            list.get(i).selected = i == position;
        }
        adapter.notifyDataSetChanged();
    }


    private static class BgSelectAdapter extends BaseRecyclerViewAdapter<PictureMode> {
        public BgSelectAdapter(List<PictureMode> list) {
            super(R.layout.adapter_bg_select, list);
        }

        @Override
        protected void convert(BaseViewHolder holder, PictureMode item) {
            holder.setImageResource(R.id.img, item.selected ? item.selectBg : item.unSelectBg);

        }

    }

    public BgSelectView setOnBgSelectViewistener(OnBgSelectViewListener bgSelectViewistener) {
        this.bgSelectViewistener = bgSelectViewistener;
        return this;
    }

    public interface OnBgSelectViewListener {
        void onCloseClick();

        void onSelectClick(PictureMode pictureMode);
    }


}
