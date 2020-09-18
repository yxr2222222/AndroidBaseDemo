package com.yxr.base.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ciba
 * @description 文件IO操作工具类
 * @date 2020/9/17
 */
public class FileUtil {

    /**
     * 快速关闭Closeable
     *
     * @param closeables
     */
    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null && closeables.length > 0) {
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存内容到文件
     *
     * @param path    文件完整路径
     * @param content 存储内容
     */
    public static boolean saveContentToFile(String path, String content) {
        return saveContentToFile(path, content, false);
    }

    /**
     * 保存内容到文件
     *
     * @param path     文件完整路径
     * @param content  存储内容
     * @param isAppend 是否拼接
     * @return 是否成功
     */
    public static boolean saveContentToFile(String path, String content, boolean isAppend) {
        boolean isSuccess = true;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path), isAppend);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            closeQuietly(fos);
        }
        return isSuccess;
    }

    /**
     * 删除某个文件下面的所有名称包含conditionPrefix的文件
     *
     * @param root            文件
     * @param conditionPrefix
     */
    public static void deleteDirectoryFiles(File root, String conditionPrefix) {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (file.getName().startsWith(conditionPrefix)) {
                    file.delete();
                }
            }
        }
    }


    /**
     * 外部存储器是否可用
     *
     * @return 外部存储器是否可用
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 从Asset中获取数据
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return
     */
    public static String readFromAsset(Context context, String fileName) {
        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(baos);
        }
        return result;
    }

}
