package cn.com.zlct.baseokhttpdemo.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.com.zlct.baseokhttpdemo.R;


/**
 * 下载进度对话框
 */
public class DownloadDialog extends DialogFragment implements DialogInterface.OnKeyListener {

    private TextView tvTitle;
    private ProgressBar pb;
    private TextView btnInstall;

    public static DownloadDialog newInstance(String str, boolean tag) {
        DownloadDialog downloadDialog = new DownloadDialog();
        Bundle bundle = new Bundle();
        bundle.putString("str", str);
        bundle.putBoolean("tag", tag);
        downloadDialog.setArguments(bundle);
        return downloadDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_download, null);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        tvTitle = (TextView) view.findViewById(R.id.tv_downloadTitle);
        pb = (ProgressBar) view.findViewById(R.id.pb_download);
        btnInstall = (TextView) view.findViewById(R.id.btn_installApp);
        tvTitle.setText(getArguments().getString("str"));
        //点击对话框外不可取消
        alertDialog.setCanceledOnTouchOutside(false);
        if (getArguments().getBoolean("tag")) {
            alertDialog.setOnKeyListener(this);
        }
        return alertDialog;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;
        } else {
            return false;
        }
    }

    public ProgressBar getProgressBar() {
        return pb;
    }

    public TextView getBtnInstall() {
        return btnInstall;
    }
}
