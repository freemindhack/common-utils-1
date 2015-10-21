package com.kifile.android.download;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 下载任务状态.
 *
 * @author kifile
 */
public class DownloadState implements Parcelable {

    /**
     * 下载状态
     */
    public static final int STATUS_NOT_EXIST = -1; // 任务不存在
    public static final int STATUS_PENDING = 0; // 等待下载
    public static final int STATUS_DOWNLOADING = 1; // 下载中
    public static final int STATUS_SUCCESS = 2; // 下载成功
    public static final int STATUS_FAILED = 3; // 下载失败
    public static final int STATUS_CANCELED = 4; // 取消下载

    /**
     * 错误码
     */
    public static final int ERROR_NORMAL = 0; // 正常.
    public static final int ERROR_NO_LINK = 1; // 没有下载地址.
    public static final int ERROR_NO_PATH = 2; // 没有保存路径.
    public static final int ERROR_CANCEL = 3; // 用户取消.
    public static final int ERROR_ALREADY_EXIST = 4; // 已经存在任务队列中.
    public static final int ERROR_OTHER = 4; // 其他错误.

    /**
     * 当前状态.
     *
     * @see #STATUS_PENDING
     * @see #STATUS_DOWNLOADING
     * @see #STATUS_SUCCESS
     * @see #STATUS_FAILED
     */
    public int status;

    /**
     * 任务错误.
     */
    public int error;

    /**
     * 下载进度.
     */
    public double percent;

    /**
     * Link地址
     */
    public String link;

    /**
     * 下载路径
     */
    public String path;

    /**
     * 开启任务的ServiceId
     */
    public int startId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeInt(error);
        dest.writeDouble(percent);
        dest.writeString(link);
        dest.writeString(path);
        dest.writeInt(startId);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DownloadState) {
            DownloadState state = (DownloadState) o;
            return link != null && link.equals(state.link) && path != null && path.equals(state.path);
        }
        return false;
    }

    public static final Parcelable.Creator<DownloadState> CREATOR = new Parcelable.Creator<DownloadState>() {

        public DownloadState createFromParcel(Parcel in) {
            DownloadState task = new DownloadState();
            task.status = in.readInt();
            task.error = in.readInt();
            task.percent = in.readDouble();
            task.link = in.readString();
            task.path = in.readString();
            return task;
        }

        public DownloadState[] newArray(int size) {
            return new DownloadState[size];
        }
    };

}
