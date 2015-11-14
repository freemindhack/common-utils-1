package com.kifile.android.components;

import android.os.Handler;

/**
 * 异步任务,内含onSuccess和,onFail接口,负责在主线程回调.
 * <p/>
 * Created by kifile on 15/10/23.
 */
public abstract class Task<SUCCESS, PROCESS, FAIL> implements Runnable {

    private final Handler mHandler;

    private final TaskCallback<SUCCESS, PROCESS, FAIL> mCallback;

    public Task(Handler handler, TaskCallback<SUCCESS, PROCESS, FAIL> callback) {
        mHandler = handler == null ? new Handler() : handler;
        mCallback = callback;
    }

    public Task(TaskCallback<SUCCESS, PROCESS, FAIL> callback) {
        this(null, callback);
    }

    /**
     * 在指定线程上执行成功操作.
     */
    public void performSuccess(final SUCCESS success) {
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
     * 在指定线程上执行失败操作.
     */
    public void performFail(final FAIL fail) {
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
     * 在指定线程上执行数据更新操作.
     *
     * @param process
     */
    public void performProcessChanged(final PROCESS process) {
        if (mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onTaskProcessChanged(process);
                }
            });
        }
    }

    /**
     * 任务回调接口.
     */
    public interface TaskCallback<SUCCESS, PROCESS, FAIL> {

        /**
         * 任务成功.
         */
        void onTaskSuccess(SUCCESS success);

        /**
         * 任务失败.
         */
        void onTaskFail(FAIL fail);

        /**
         * 任务进度更新.
         *
         * @param process
         */
        void onTaskProcessChanged(PROCESS process);

    }

}
