package com.kifile.android.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * 与进程相关的工具类.
 * <p/>
 * Created by zhouyi on 15/6/4.
 */
public class ProcessUtils {

    /**
     * 返回当前进程名称。
     *
     * @param context
     *
     * @return
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

    /**
     * 判断进程是否是指定进程.
     *
     * @param context
     * @param process 进程名. null 表示主进程.
     *
     * @return
     */
    public static boolean isProcess(Context context, String process) {
        String processName = getProcessName(context);
        String packageName = context.getPackageName();
        if (!TextUtils.isEmpty(processName) && processName.startsWith(packageName)) {
            if (process == null) {
                return packageName.equals(processName);
            } else {
                return (packageName + ":" + process).equals(processName);
            }
        }
        return false;
    }
}
