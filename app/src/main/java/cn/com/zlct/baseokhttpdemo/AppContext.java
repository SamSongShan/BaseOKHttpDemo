package cn.com.zlct.baseokhttpdemo;

import android.app.Application;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.wenming.library.LogReport;
import com.wenming.library.save.imp.CrashWriter;

import cn.com.zlct.baseokhttpdemo.util.ConfigConstants;
import cn.com.zlct.baseokhttpdemo.util.OkHttpUtil;


public class AppContext extends Application {

    private int cartCount = 0;
    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        instance = this;
        OkHttpUtil.initOkHttp();
        Fresco.initialize(this, ConfigConstants.getImagePipelineConfig(this));
        /*JPushInterface.setDebugMode(true);
        JPushInterface.init(this);*/
        ActiveAndroid.initialize(this);
        initCrashReport();
        ZXingLibrary.initDisplayOpinion(this);


    }

    private void initCrashReport() {
        LogReport.getInstance()
                //定义路径为：Android/data/[PackageName]/cLog
                .setLogDir(getApplicationContext(), Environment.getExternalStorageDirectory() +
                        "/Android/data/" + this.getPackageName() + "/c")
                .setLogSaver(new CrashWriter(getApplicationContext()))
                .init(getApplicationContext());
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    /**
     * app退出时调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public static AppContext getInstance() {
        // 这里不用判断instance是否为空
        return instance;
    }
}
