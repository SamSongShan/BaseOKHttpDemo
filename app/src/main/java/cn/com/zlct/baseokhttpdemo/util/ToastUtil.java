package cn.com.zlct.baseokhttpdemo.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast的工具类
 */
public class ToastUtil {

    public static void initToast(Context context, String string){
        initToast(context, string, Toast.LENGTH_SHORT);
    }

    public static void initToast(Context context, String string, int duration){
        Toast toast = Toast.makeText(context, string, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
