package cn.com.zlct.baseokhttpdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.zlct.baseokhttpdemo.model.MsgEntity;

/**
 * 所有Fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;

    protected OnMsgListener msgListener;
    protected OnCartUpListener cartListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getViewResId(), container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getData(getArguments());
        loadData();
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract int getViewResId();

    protected void init(View v){}

    protected void getData(Bundle arguments){}

    protected  void loadData(){}

    public void setOnMsgListener(OnMsgListener msgListener){
        this.msgListener = msgListener;
    }

    public interface OnMsgListener{
        void onDeliverMsg(MsgEntity msg);
    }

    public void setOnCartUpListener(OnCartUpListener cartListener) {
        this.cartListener = cartListener;
    }

    public interface OnCartUpListener {
        void onCartAnim(View v, String url);
        void onCartUp(int count);
    }

    public void upUI(String info){}

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
