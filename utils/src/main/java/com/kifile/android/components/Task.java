package com.kifile.android.components;

import android.os.Handler;

/**
 * 异步任务,内含onSuccess和,onFail接口,负责在主线程回调.
 * <p/>
 * Created by kifile on 15/10/23.
 */
public abstract class Task<SUCCESS, FAIL> implements Runnable {

    private final Handler mHandler;

    private final TaskCallback<SUCCESS, FAIL> mCallback;

    public Task(Handler handler, TaskCallback<SUCCESS, FAIL> callback) {
        mHandler = handler == null ? new Handler() : handler;
        mCallback = callback;
    }

    public Task(TaskCallback<SUCCESS, FAIL> callback) {
        this(null, callback);
    }

    /**
     * 在Ui线程上执行成功操作.
     */
    public void onSuccess(final SUCCESS success) {
        if (mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onTaskSuccess(success);
                }
            });
        }
    }

    /**
     * 在Ui线程上执行失败操作.
     */
    public void onFail(final FAIL fail) {
        if (mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onTaskFail(fail);
                }
            });
        }

    }

    /**
     * 任务回调接口.
     */
    public interface TaskCallback<SUCCESS, FAIL> {

        /**
         * 任务成功.
         */
        void onTaskSuccess(SUCCESS success);

        /**
         * 任务失败.
         */
        void onTaskFail(FAIL fail);

    }

}
