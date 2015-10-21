// IDownload.aidl
package com.kifile.android.download;

// 下载服务相关接口

interface IDownload {

    /**
     * 查询指定link地址的下载任务状态.
     * 由于用户可能在不同的地址下载同一个Link，所以需要同时通过link和path，判断是否是同一个任务.
     */
    int getDownloadTaskStatus(String link, String path);

}
