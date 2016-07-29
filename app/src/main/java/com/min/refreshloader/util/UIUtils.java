package com.min.refreshloader.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by minyangcheng on 2016/7/29.
 */
public class UIUtils {

    public static void toast(Context context,String mess){
        Toast.makeText(context,mess,Toast.LENGTH_SHORT).show();
    }

}
