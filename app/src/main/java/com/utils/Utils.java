package com.utils;

import android.content.Context;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * Created by cwj on 15/12/3.
 */
public class Utils {

    public static void showToast(Context context, String content) {
        if (context == null || content == null)
            return;
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 将dp单位转为px单位
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());

    }
}
