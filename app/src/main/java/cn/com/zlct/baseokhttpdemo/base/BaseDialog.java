package cn.com.zlct.baseokhttpdemo.base;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.view.View;

/**
 * 所有DialogFragment的基类
 */
public abstract class BaseDialog extends DialogFragment {

    protected OnItemClickListener onItemClickListener;


    public void show(FragmentManager manager) {
        this.show(manager, "dialog");
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v);
    }
}
