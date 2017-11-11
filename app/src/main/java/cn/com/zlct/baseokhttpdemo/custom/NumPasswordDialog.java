package cn.com.zlct.baseokhttpdemo.custom;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.com.zlct.baseokhttpdemo.R;
import cn.com.zlct.baseokhttpdemo.base.BaseDialog;

/**
 * 数字密码输入框
 */
public class NumPasswordDialog extends BaseDialog implements View.OnClickListener, AdapterView.OnItemClickListener {

    private InputPassword ip;
    private InputPasswordListener ipListener;

    public static NumPasswordDialog newInstance(String title) {
        NumPasswordDialog dialog = new NumPasswordDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //取消标题显示
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        View v = inflater.inflate(R.layout.dialog_num_password,
                ((ViewGroup) window.findViewById(android.R.id.content)), false);
        //点击对话框外不可取消
        getDialog().setCanceledOnTouchOutside(false);
        //必须设置，否则不能全屏
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //宽度撑满
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        //进入退出动画
        window.setWindowAnimations(R.style.animTranslateBottom);

        ImageButton ibClose = (ImageButton) v.findViewById(R.id.ib_passwordDialogClose);
        TextView tvTitle = (TextView) v.findViewById(R.id.ib_passwordDialogTitle);
        ip = (InputPassword) v.findViewById(R.id.ip_passwordDialog);
        GridView gridView = (GridView) v.findViewById(R.id.gv_passwordDialog);

        ibClose.setOnClickListener(this);
        tvTitle.setText(getArguments().getString("title"));
        gridView.setAdapter(new NumberAdapter());
        gridView.setOnItemClickListener(this);
        ip.setOnFinishListener(new InputPassword.OnFinishListener() {
            @Override
            public void setOnPasswordFinished(String pwd) {
                if (ipListener != null) {
                    dismiss();
                    ipListener.finishPwd(pwd);
                }
            }
        });
        return v;
    }

    class NumberAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_password_num, viewGroup, false);
            TextView tv = (TextView) v.findViewById(R.id.tv_numItemNumber);
            if (i == 9) {
                tv.setVisibility(View.GONE);
                v.findViewById(R.id.iv_numItemDelete).setVisibility(View.GONE);
            } else if (i == 10) {
                v.findViewById(R.id.iv_numItemDelete).setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                tv.setText("0");
            } else if (i == 11) {
                v.findViewById(R.id.iv_numItemDelete).setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);
            } else {
                tv.setText((i + 1) + "");
                v.findViewById(R.id.iv_numItemDelete).setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            }
            return v;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 11) {
            ip.deletePwd();
        } else if (i == 10) {
            ip.addPwd("0");
        } else if (i == 9) {

        } else {
            ip.addPwd(String.valueOf(++i));
        }
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    public void setListener(InputPasswordListener ipListener) {
        this.ipListener = ipListener;
    }

    public interface InputPasswordListener {
        void finishPwd(String pwd);
    }
}
