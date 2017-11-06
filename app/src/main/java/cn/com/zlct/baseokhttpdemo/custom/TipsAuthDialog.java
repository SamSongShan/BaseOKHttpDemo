package cn.com.zlct.baseokhttpdemo.custom;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.com.zlct.baseokhttpdemo.R;


/**
 * 手机权限提示对话框
 */
public class TipsAuthDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvLoading;

    public static TipsAuthDialog newInstance(String str) {
        TipsAuthDialog dialog = new TipsAuthDialog();
        Bundle bundle = new Bundle();
        bundle.putString("str", str);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tips, container);
        tvLoading = (TextView) view.findViewById(R.id.tv_tipsBody);
        Button btnCancel = (Button) view.findViewById(R.id.btn_tipsCancel);
        Button btnSetting = (Button) view.findViewById(R.id.btn_tipsSetting);
        tvLoading.setText(getArguments().getString("str"));
        btnCancel.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        //点击对话框外不可取消
        getDialog().setCanceledOnTouchOutside(false);
        //取消标题显示
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tipsCancel:
                dismiss();
                break;
            case R.id.btn_tipsSetting://设置
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                startActivity(intent);
                break;
        }
    }
}
