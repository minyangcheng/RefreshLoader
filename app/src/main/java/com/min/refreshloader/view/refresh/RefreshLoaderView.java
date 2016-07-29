package com.min.refreshloader.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.min.refreshloader.R;

/**
 * Created by minyangcheng on 2016/7/29.
 */
public class RefreshLoaderView extends FrameLayout {

    public RefreshLoaderView(Context context) {
        this(context, null);
    }

    public RefreshLoaderView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public RefreshLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_refresh_loader,this,true);
    }

}
