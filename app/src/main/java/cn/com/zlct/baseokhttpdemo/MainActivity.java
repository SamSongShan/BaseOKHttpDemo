package cn.com.zlct.baseokhttpdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.zlct.baseokhttpdemo.base.BaseActivity;
import cn.com.zlct.baseokhttpdemo.custom.DownloadDialog;
import cn.com.zlct.baseokhttpdemo.util.OkHttpUtil;
import cn.com.zlct.baseokhttpdemo.util.PhoneUtil;
import cn.com.zlct.baseokhttpdemo.util.ToastUtil;

import static android.R.attr.versionName;


public class MainActivity extends BaseActivity implements View.OnClickListener, OkHttpUtil.OnProgressListener {

    private DownloadDialog downloadDialog;
    private String filePath;
    private Handler handler = new Handler();
    private boolean isForce = false;//是否强制升级

    private boolean isExit = false;//退出标识


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    @Override
    protected int getViewResId() {
        return R.layout.activity_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkPermissions();
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

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//给目标应用一个临时的授权
                Uri uriForFile = FileProvider.getUriForFile(this, PhoneUtil.getAppProcessName(this) + ".FileProvider", apkFile);
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");

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

    @Override
    public void onBackPressed() {
        if (isExit) {
            System.exit(0);
        } else {
            ToastUtil.initToast(this, "再按一次退出" + PhoneUtil.getAppName(this));
            isExit = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 5000);//5秒内再按后退键真正退出
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))//存储
            permissionsNeeded.add("存储");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))//相机
            permissionsNeeded.add("相机");
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))//麦克风
            permissionsNeeded.add("麦克风");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))//位置
            permissionsNeeded.add("位置");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))//联系人
            permissionsNeeded.add("联系人");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))//手机
            permissionsNeeded.add("手机");
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))//短信
            permissionsNeeded.add("短信");
        if (!addPermission(permissionsList, Manifest.permission.READ_CALENDAR))//日历
            permissionsNeeded.add("日历");
        if (!addPermission(permissionsList, Manifest.permission.BODY_SENSORS))//传感器
            permissionsNeeded.add("传感器");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // 需要去申请权限
                String message = "你需要申请 " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    message = message + ", " + permissionsNeeded.get(i);

                }
                showMessageOKCancel(message + "等权限",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        //可以操作
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
    //如果所有权限被授权，依然回调onRequestPermissionsResult，我用hashmap让代码整洁便于阅读。

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // 初始化所有权限
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);//存储
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);//相机
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);//麦克风
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);//位置
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);//联系人
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);//手机
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);//短信
                perms.put(Manifest.permission.READ_CALENDAR, PackageManager.PERMISSION_GRANTED);//日历
                perms.put(Manifest.permission.BODY_SENSORS, PackageManager.PERMISSION_GRANTED);//传感器
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // 授权反馈成功
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // 授权成功

                } else {
                    // 授权失败

                    Toast.makeText(MainActivity.this, "部分权限被拒绝，使用过程中可能会出现未知错误", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }


}
