package cn.com.zlct.baseokhttpdemo.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.zlct.baseokhttpdemo.util.StatusBarUtils;

/**
 * 所有Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getViewResId());
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//手机竖屏

        StatusBarUtils.setStatusBarLightMode(this, Color.WHITE);
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
