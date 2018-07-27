package cn.com.zlct.baseokhttpdemo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by 11355 on 2017/12/2.
 */

public class LogUtils {
    private static boolean isLog = false;

    public static void setDebug(boolean b) {
        isLog = b;
    }

    public static void e(String tag, String msg) {
        if (isLog) {
            Log.e(tag, "onResponse: " + msg);
        }
    }

    public static void e(String msg) {
        if (isLog) {
            Log.e("loge", msg);
        }
    }

    //检测 key绑定的sha1和包名是否相符
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US

                        );
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            Log.d("sHA1", "sHA1--------------------------------------------hexstring : " + hexString.toString());
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
