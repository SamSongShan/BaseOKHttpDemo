package cn.com.zlct.baseokhttpdemo.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 文件处理工具类
 */
public class FileUtil {

    /**
     * 读取文件
     */
    public static String readTextFile(File file) throws IOException {
        String text = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            text = readTextInputStream(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return text;
    }

    /**
     * 从流中读取文件
     */
    public static String readTextInputStream(InputStream is) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 将文本内容写入文件
     */
    public static void writeTextFile(File file, String str) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            fos.write(str.getBytes("UTF-8"));
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 删除文件
     */
    public static void delFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    delFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    /**
     * 删除自动保存到本地的图片
     */
    public static void delLocalPic(long l) throws Exception {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Pictures");
        File[] files = dir.listFiles(new FilenameFilter() {//过滤jpg文件
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".jpg");
            }
        });
        for (int i = files.length - 1; i >= 0; i++) {
            String fileName = files[i].getName().split("\\.")[0];
            if (Long.parseLong(fileName) > l - 10 * 1000) {//删除10s内生成的图片
                files[i].delete();
            }
        }
    }

    /**
     * 获取文件或文件夹的大小
     */
    public static long getFileSize(File file){
        if (file.exists()) {//文件存在
            if (file.isFile()) {//是文件
                return file.length();
            } else if (file.isDirectory()) {//是目录
                long size = 0;
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) { //遍历所有文件
                    size += getFileSize(files[i]);//迭代
                }
                return size;
            }
            return 0;
        } else {
            return 0;
        }
    }

    /**
     * sd卡挂载且可用
     */
    public static boolean isSdCardMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
