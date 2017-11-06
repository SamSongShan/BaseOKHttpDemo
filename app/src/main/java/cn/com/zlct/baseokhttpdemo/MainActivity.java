package cn.com.zlct.baseokhttpdemo;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

import cn.com.zlct.baseokhttpdemo.custom.DownloadDialog;
import cn.com.zlct.baseokhttpdemo.util.OkHttpUtil;
import cn.com.zlct.baseokhttpdemo.util.PhoneUtil;

import static android.R.attr.versionName;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OkHttpUtil.OnProgressListener {

    private DownloadDialog downloadDialog;
    private String filePath;
    private Handler handler = new Handler();
    private boolean isForce=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void downLoadApp() {
        downloadDialog = DownloadDialog.newInstance(PhoneUtil.getAppName(this) + "versionName", isForce);
        downloadDialog.show(getFragmentManager(), "download");
        filePath = Environment.getExternalStorageDirectory() + "/Download/" + PhoneUtil.getAppName(this) +
                "_" + versionName + ".apk";
        Log.e("loge", "Download: " + filePath);
        OkHttpUtil.fileDownload("url", filePath, this, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {//下载完成
                TextView btnInstall = downloadDialog.getBtnInstall();
                btnInstall.setSelected(true);
                btnInstall.setText("安装");
                btnInstall.setClickable(true);
                btnInstall.setOnClickListener(MainActivity.this);
                jumpInstall();
            }

            @Override
            public void onFailure(String url, String error) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        jumpInstall();
    }

    /**
     * 跳转到安装页面
     */
    private void jumpInstall() {
        File apkFile = new File(filePath);
        if (apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");

            } else {
                PackageManager pm = this.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
                ApplicationInfo appInfo = null;
                if (info != null) {
                    appInfo = info.applicationInfo;
                    String packageName = appInfo.packageName;
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//给目标应用一个临时的授权
                    Uri uriForFile = FileProvider.getUriForFile(this, packageName + ".FileProvider", apkFile);
                    intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                }

            }
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());

        }
    }

    @Override
    public void onProgress(final int rate) {
        ProgressBar pb = downloadDialog.getProgressBar();
        final TextView btnInstall = downloadDialog.getBtnInstall();
        if (pb != null) {
            pb.setProgress(rate);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (btnInstall != null) {
                    btnInstall.setText("下载中(" + rate + "%)");
                    if (rate == 100) {
                        btnInstall.setText("安装");
                    }
                }
            }
        });
    }
}
