package com.kifile.android.utils;

import java.io.File;
import java.io.IOException;

/**
 * 文件操作工具类.
 *
 * @author kifile
 */
public class FileUtils {

    /**
     * 保证文件存在，不存在则创建.
     *
     * @param path
     *
     * @throws IOException
     */
    public static File ensureFileExist(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() && !file.isFile()) {
            String parent = file.getParent();
            ensurePathExist(parent);
            file.createNewFile();
        }
        return file;
    }

    /**
     * 确保文件夹存在，不存在则创建
     *
     * @param path
     */
    public static File ensurePathExist(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 删除该目录下所有文件，
     *
     * @param delpath
     *
     * @return
     */
    public static boolean delete(String delpath) {
        return delete(new File(delpath));
    }

    /**
     * 删除该目录下所有文件，
     *
     * @param file
     *
     * @return
     */
    public static boolean delete(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f);
            }
            // 在FAT32类型的储存设备上，必须重命名之后删除，否则下次下载到该位置会报错 see:http://stackoverflow
            // .com/questions/11539657/open-failed-ebusy-device-or-resource-busy
            File newFile = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(newFile);
            return newFile.delete();
        } else {
            // 在FAT32类型的储存设备上，必须重命名之后删除，否则下次下载到该位置会报错 see:http://stackoverflow
            // .com/questions/11539657/open-failed-ebusy-device-or-resource-busy
            File newFile = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(newFile);
            return newFile.delete();
        }
    }
}
