package cn.com.zlct.baseokhttpdemo.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 所有Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getViewResId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//手机竖屏
        unbinder = ButterKnife.bind(this);
        init();
        loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
     //   super.onSaveInstanceState(outState);

    }

    protected abstract int getViewResId();

    protected void init(){}

    protected void loadData(){}

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
