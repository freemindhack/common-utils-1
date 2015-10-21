package com.kifile.android.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * 下载状态广播接收器.
 * <p/>
 * 当对下载信息感兴趣时，请手动注册监听广播事件.
 *
 * @author kifile
 */
public class DownloadStatusReceiver extends BroadcastReceiver {

    public static final String ACTION = DownloadStatusReceiver.class.getName();

    public static final String EXTRA_TASK = "extra_task";

    private Handler mHandler = new Handler();

    @Override
    public final void onReceive(Context context, final Intent intent) {
        String action = intent.getAction();
        if (ACTION.equals(action)) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // BroadcastReceiver有时间限制，做事件分发.
                    DownloadState state = intent.getParcelableExtra(EXTRA_TASK);
                    if (state == null) {
                        return;
                    }
                    String link = state.link;
                    String path = state.path;
                    switch (state.status) {
                        case DownloadState.STATUS_PENDING:
                            onPendingDownload(link, path);
                            break;
                        case DownloadState.STATUS_DOWNLOADING:
                            onDownloading(link, path, state.percent);
                            break;
                        case DownloadState.STATUS_NOT_EXIST:
                            onCancelFailed(link, path);
                            break;
                        case DownloadState.STATUS_CANCELED:
                            onCancel(link, path);
                            break;
                        case DownloadState.STATUS_SUCCESS:
                            onDownloadSuccess(link, path);
                            break;
                        case DownloadState.STATUS_FAILED:
                            onDownloadFailed(link, path, state.error);
                            break;
                        default:
                            break;
                    }
                }

            });
        }
    }

    /**
     * 等待下载.
     *
     * @param link
     * @param path
     */
    protected void onPendingDownload(String link, String path) {

    }

    /**
     * 正在下载.
     *
     * @param link
     * @param path
     * @param percent
     */
    protected void onDownloading(String link, String path, double percent) {

    }

    /**
     * 任务取消失败(不存在该任务)。
     *
     * @param link
     * @param path
     */
    protected void onCancelFailed(String link, String path) {

    }

    /**
     * 取消下载.
     *
     * @param link
     * @param path
     */
    protected void onCancel(String link, String path) {

    }

    /**
     * 下载成功.
     *
     * @param link
     * @param path
     */
    protected void onDownloadSuccess(String link, String path) {

    }

    /**
     * 下载失败.
     *
     * @param link
     * @param path
     * @param error
     */
    protected void onDownloadFailed(String link, String path, int error) {

    }

}
