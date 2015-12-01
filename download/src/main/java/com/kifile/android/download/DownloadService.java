package com.kifile.android.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.kifile.android.utils.WorkerThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 下载服务.
 *
 * @author kifile
 */
public class DownloadService extends Service {

    public static final String EXTRA_LINK = "extra_link";
    public static final String EXTRA_PATH = "extra_path";

    public static final int ACTION_DOWNLOAD = 0; // 下载
    public static final int ACTION_CANCEL = 1; // 取消下载

    public static final String EXTRA_ACTION = "extra_action";

    private final ReentrantReadWriteLock mLock = new ReentrantReadWriteLock();

    private List<DownloadState> mTaskMap = new ArrayList<>();

    private final IDownload.Stub mStub = new IDownload.Stub() {

        @Override
        public int getDownloadTaskStatus(String link, String path) throws RemoteException {
            DownloadState state = getStateByLinkAndPath(link, path);
            return state != null ? state.status : DownloadState.STATUS_NOT_EXIST;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        clearTaskMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void clearTaskMap() {
        mLock.writeLock().lock();
        try {
            for (DownloadState state : mTaskMap) {
                if (state.status != DownloadState.STATUS_SUCCESS) {
                    state.status = DownloadState.STATUS_CANCELED;
                }
            }
            mTaskMap.clear();
        } finally {
            mLock.writeLock().unlock();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        String link = intent.getStringExtra(EXTRA_LINK);
        String path = intent.getStringExtra(EXTRA_PATH);
        DownloadState state = new DownloadState();
        state.link = link;
        state.path = path;
        state.startId = startId;
        if (TextUtils.isEmpty(link)) {
            state.status = DownloadState.STATUS_CANCELED;
            state.error = DownloadState.ERROR_NO_LINK;
            handleDownloadState(state);
            return START_NOT_STICKY;
        }
        if (TextUtils.isEmpty(path)) {
            state.status = DownloadState.STATUS_CANCELED;
            state.error = DownloadState.ERROR_NO_PATH;
            handleDownloadState(state);
            return START_NOT_STICKY;
        }
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_DOWNLOAD);
        if (action == ACTION_CANCEL) {
            cancelDownload(link, path);
            return START_NOT_STICKY;
        }
        state.status = DownloadState.STATUS_PENDING;
        state.startId = startId;
        mLock.writeLock().lock();
        try {
            if (mTaskMap.contains(state)) {
                state.status = DownloadState.STATUS_FAILED;
                state.error = DownloadState.ERROR_ALREADY_EXIST;
                handleDownloadState(state);
            } else {
                DownloadTask task = new DownloadTask(state) {
                    @Override
                    protected void onPostExecute(DownloadState downloadState) {
                        super.onPostExecute(downloadState);
                        handleDownloadState(downloadState);
                    }

                    @Override
                    protected void onCancelled() {
                        super.onCancelled();
                    }

                    @Override
                    protected void onProgressUpdate(DownloadState... values) {
                        super.onProgressUpdate(values);
                        if (values != null && values.length == 1) {
                            handleDownloadState(values[0]);
                        }
                    }
                };
                task.executeOnExecutor(WorkerThreadPool.getInstance());
                mTaskMap.add(state);
            }
        } finally {
            mLock.writeLock().unlock();
        }
        return START_NOT_STICKY;
    }

    /**
     * 取消下载指定Link和Path的任务.
     *
     * @param link
     * @param path
     */
    private void cancelDownload(String link, String path) {
        DownloadState state = getStateByLinkAndPath(link, path);
        if (state != null) {
            state.status = DownloadState.STATUS_CANCELED;
            handleDownloadState(state);
        }
    }

    private DownloadState getStateByLinkAndPath(String link, String path) {
        mLock.readLock().lock();
        try {
            for (DownloadState state : mTaskMap) {
                if (link.equals(state.link) && path.equals(state.path)) {
                    return state;
                }
            }
            mTaskMap.clear();
        } finally {
            mLock.readLock().unlock();
        }
        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    private void handleDownloadState(DownloadState state) {
        Intent intent = new Intent(DownloadStatusReceiver.ACTION);
        intent.setPackage(getPackageName());
        intent.putExtra(DownloadStatusReceiver.EXTRA_TASK, state);
        sendBroadcast(intent);
        switch (state.status) {
            case DownloadState.STATUS_CANCELED:
            case DownloadState.STATUS_SUCCESS:
            case DownloadState.STATUS_FAILED:
                // 取消，成功，失败均代表当前下载结束，需要删除任务,并stopSelf.
                mLock.writeLock().lock();
                try {
                    mTaskMap.remove(state);
                } finally {
                    mLock.writeLock().unlock();
                }
                stopSelf(state.startId);
                break;
            default:
                break;
        }
    }
}
