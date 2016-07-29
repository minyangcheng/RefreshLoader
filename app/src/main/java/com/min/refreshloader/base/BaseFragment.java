package com.min.refreshloader.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by minyangcheng on 2016/7/29.
 */
public class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
}
