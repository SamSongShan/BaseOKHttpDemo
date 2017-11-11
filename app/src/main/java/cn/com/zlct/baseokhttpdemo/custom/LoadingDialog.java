package cn.com.zlct.baseokhttpdemo.custom;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.zlct.baseokhttpdemo.R;
import cn.com.zlct.baseokhttpdemo.base.BaseDialog;

/**
 * 加载中对话框
 */
public class LoadingDialog extends BaseDialog {

    private JumpLoading jlLoading;
    private TextView tvLoading;

    public static LoadingDialog newInstance(String str) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putString("str", str);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_loading, container);
        tvLoading = (TextView) v.findViewById(R.id.tv_loading);
        jlLoading = (JumpLoading) v.findViewById(R.id.jl_loading);
        tvLoading.setText(getArguments().getString("str"));
        int phoneWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
        jlLoading.setLayoutParams(new LinearLayout.LayoutParams(phoneWidth / 3, phoneWidth / 8));
        jlLoading.startAnim();

        getDialog().setCanceledOnTouchOutside(false);//对话框外不可取消
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.shape_solid_trans);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题显示
        return v;
    }

    @Override
    public void show(FragmentManager manager) {
        if (jlLoading != null) {
            jlLoading.startAnim();
        }
        super.show(manager);
    }

    public void setText(String text) {
        if (tvLoading != null) {
            tvLoading.setText(text);
        }
    }

    @Override
    public void dismiss() {
        if (jlLoading != null) {
            jlLoading.stopAnim();
        }
        super.dismiss();
    }
}
