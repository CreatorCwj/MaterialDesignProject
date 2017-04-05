package com.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * Created by cwj on 15/12/3.
 */
public class Utils {

    private static final String TASK_TAG = "TaskInfo";

    public static void showTaskInfo(Activity activity) {
        Log.i(TASK_TAG, activity.toString() + "  taskId:" + activity.getTaskId());
    }

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

    /**
     * 将sp单位转为px单位
     */
    public static int sp2px(Context context, float spValue) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());

    }

    public static void logger(String tag, String prefix, String text) {
        Log.i(tag, prefix + " - " + text);
    }
}
