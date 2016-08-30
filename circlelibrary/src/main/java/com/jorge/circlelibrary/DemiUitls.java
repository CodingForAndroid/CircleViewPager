package com.jorge.circlelibrary;

import android.app.Activity;
import android.content.Context;


public final class DemiUitls {

    public static  int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static  int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Activity context){
        return context.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Activity context){
        return context.getWindowManager().getDefaultDisplay().getHeight();
    }


}
