package com.blm.qiubopay.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public  class MarginItemDecoration extends RecyclerView.ItemDecoration{
    int spacing;
    public MarginItemDecoration(Context context, int spacing) {
        this.spacing=spacing;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom=spacing;
    }
}
