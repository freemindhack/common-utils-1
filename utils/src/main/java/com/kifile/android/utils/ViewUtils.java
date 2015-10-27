package com.kifile.android.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * View相关工具类.
 * <p/>
 * Created by kifile on 15/10/27.
 */
public class ViewUtils {

    public static void clearParent(View view) {
        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public static void addSingleView(ViewGroup container, View child) {
        if (container == null || child == null) {
            throw new NullPointerException("Container or child cannot be null.");
        }
        if (container.getChildCount() != 1 || container.getChildAt(0) != child) {
            container.removeAllViews();
            clearParent(child);
            container.addView(child);
        }
    }
}
