package com.kifile.android.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * View涉及的工具类.
 *
 * @author kifile
 */
public class ViewUtils {

    /**
     * 清理view，保证没有包含在ViewGroup内
     *
     * @param view
     */
    public static void clearParent(View view) {
        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    /**
     * 添加单个View到ViewGroup中.
     *
     * @param parent
     * @param child
     */
    public static void addSingleViewToGroup(ViewGroup parent, View child) {
        if (parent == null || child == null) {
            return;
        }
        if (!(parent.getChildCount() == 1 && (parent.getChildAt(0)) == child)) {
            parent.removeAllViews();
            clearParent(child);
            parent.addView(child);
        }
    }
}
