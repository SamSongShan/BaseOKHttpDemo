package cn.com.zlct.baseokhttpdemo.util;

import android.app.DialogFragment;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import cn.com.zlct.baseokhttpdemo.custom.TipsAuthDialog;


/**
 * 缓存工具类
 */
public class CacheUtil {

    private static final String TEXT_CACHE_DIR = Environment.getExternalStorageDirectory() +
            "/Android/data/com.zlct.meihepayment/cache";//缓存路径

    /**
     * 获取缓存
     */
    public static String getUrlCache(String url){
        return getUrlCache(url, 30 * 24 * 60 * 60 * 1000L);
    }

    /**
     * 获取缓存 2周内不重复访问服务器
     */
    public static String getUrlCache2Weeks(String url){
        return getUrlCache(url, 14 * 24 * 60 * 60 * 1000L);
    }

    /**
     * 获取缓存 1分钟内不重复访问服务器
     */
    public static String  getUrlCache1M(String url){
        return getUrlCache(url, 60 * 1000L);
    }

    /**
     * 获取缓存
     */
    private static String getUrlCache(String url, long l){
        if (url == null) {
            return null;
        }
        String result = null;
        try {
            File file = new File(TEXT_CACHE_DIR, getFilePath(url));
//            Log.e("log", "getUrl: " + file.getPath());
            if (file.exists() && file.isFile() && (System.currentTimeMillis() - file.lastModified()) < l) {
                result = FileUtil.readTextFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置缓存
     */
    public static DialogFragment setUrlCache(FragmentActivity context, String url, String data){
        File cacheDir = new File(TEXT_CACHE_DIR);
//            Log.e("log", "setUrlCache: "+cacheDir);
        if (!cacheDir.exists() && FileUtil.isSdCardMounted()) {
            cacheDir.mkdirs();
        }
        if (!cacheDir.exists()) {
            TipsAuthDialog tipsAuthDialog = TipsAuthDialog.newInstance(Constant.Strings.PermissionFileTips);
            tipsAuthDialog.show(context.getFragmentManager(), "tips");
            return tipsAuthDialog;
        } else {
            File file = new File(TEXT_CACHE_DIR, getFilePath(url));
            try {
                FileUtil.writeTextFile(file, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取缓存保存的时间
     */
    public static long getUrlCacheModifyTime(String url) {
        try {
            File file = new File(TEXT_CACHE_DIR, getFilePath(url));
            if (file.exists() && file.isFile()) {
                return file.lastModified();
            }
            return new SimpleDateFormat("yyyyMMdd").parse("20160101").getTime();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    /**
     * 转换文件路径
     */
    public static String getFilePath(String url){
        String encode = Base64Util.encode(url);
        return encode.substring(encode.length() / 2, encode.length()).replace("=", "0").trim();
    }

    public static void deleteUrlCache(String url) {
        FileUtil.delFile(new File(TEXT_CACHE_DIR, getFilePath(url)));
    }

    /**
     * 清空缓存
     */
    public static void clearCache(){
        FileUtil.delFile(new File(TEXT_CACHE_DIR));
    }

    /**
     * 获取缓存大小
     */
    public static long cacheSize(){
        return FileUtil.getFileSize(new File(TEXT_CACHE_DIR));
    }
}
