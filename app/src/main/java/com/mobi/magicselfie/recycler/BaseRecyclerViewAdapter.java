package com.mobi.magicselfie.recycler;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<T> mData;
    protected LayoutInflater mLayoutInflater;
    protected int mLayoutResId;
    protected Context mContext;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public BaseRecyclerViewAdapter(int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    public BaseRecyclerViewAdapter(List<T> data) {
        this(0, data);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        baseViewHolder = onCreateDefViewHolder(parent, viewType);
        initItemClickListener(baseViewHolder);
        return baseViewHolder;
    }

    private void initItemClickListener(BaseViewHolder baseViewHolder) {
        if (onRecyclerViewItemClickListener != null) {
            baseViewHolder.convertView.setOnClickListener((v) -> onRecyclerViewItemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition()));
        }
    }


    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return new BaseViewHolder(getItemView(layoutResId, parent));
    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        convert((BaseViewHolder) holder, mData.get(position));
    }

    protected abstract void convert(BaseViewHolder holder, T item);

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public List<T> getData() {
        return mData;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public T getItem(int position){
        return getData().get(position);
    }

}
