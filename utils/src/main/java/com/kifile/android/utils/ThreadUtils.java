package com.kifile.android.utils;

import android.os.Looper;

/**
 * The utils of Thread.
 *
 * @author kifile
 */
public class ThreadUtils {

    public static void checkMainThread() {
        if (!isMain()) {
            throw new RuntimeException(new IllegalAccessError("Mush run on main thread."));
        }
    }

    /**
     * @return If currentThread is main thread, return true.
     */
    public static boolean isMain() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
