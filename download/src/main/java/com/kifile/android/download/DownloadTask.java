package com.kifile.android.download;

import com.kifile.android.components.Task;
import com.kifile.android.utils.FileUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 下载任务.
 *
 * @author kifile
 */
public class DownloadTask extends Task<DownloadState, DownloadState, DownloadState> {

    private static final String SUFFIX = ".tmp";

    private DownloadState mState;

    public DownloadTask(DownloadState state, TaskCallback<DownloadState, DownloadState,
            DownloadState> callback) {
        super(callback);
        mState = state;
    }

    @Override
    public void run() {
        if (mState.status != DownloadState.STATUS_CANCELED) {
            mState.status = DownloadState.STATUS_DOWNLOADING;
            mState.percent = 0;
            performProcessChanged(mState);
            double percent;
            InputStream inputstream = null;
            FileOutputStream stream = null;
            try {
                Request request = new Request.Builder().url(mState.link).build();
                Response response = HttpUtils.getResponse(request);
                if (response.isSuccessful()) {
                    // 链接成功开始下载
                    long length = response.body().contentLength();
                    inputstream = response.body().byteStream();
                    byte[] buffer = new byte[1024 * 4];
                    long download = 0;
                    int bufferSize;
                    String path = mState.path;
                    String tmpPath = path + SUFFIX;
                    // 使用临时文件进行下载,防止下载中出错,导致文件无法正常读取.
                    FileUtils.ensureFileExist(tmpPath);
                    stream = new FileOutputStream(tmpPath);
                    while (mState.status != DownloadState.STATUS_CANCELED) {
                        if ((bufferSize = inputstream.read(buffer)) != -1) {
                            if (length != -1) {
                                download += bufferSize;
                                percent = (download == 0 ? 0 : (download * 100.0 / length));
                            } else {
                                percent = -1;
                            }
                            if (mState.percent != percent) {
                                mState.percent = percent;
                                performProcessChanged(mState);
                            }
                            stream.write(buffer, 0, bufferSize);
                        } else {
                            FileUtils.renameFile(tmpPath, path);
                            mState.status = DownloadState.STATUS_SUCCESS;
                            mState.percent = 100;
                            performProcessChanged(mState);
                            break;
                        }
                    }
                    if (mState.startId != DownloadState.STATUS_SUCCESS) {
                        FileUtils.delete(tmpPath);
                    }
                }
            } catch (Exception e) {
                mState.status = DownloadState.STATUS_FAILED;
            } finally {
                if (inputstream != null) {
                    try {
                        inputstream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (mState.status != DownloadState.STATUS_SUCCESS) {
                mState.status = DownloadState.STATUS_FAILED;
                performFail(mState);
            } else {
                performSuccess(mState);
            }
        }

    }

}
