package cn.com.zlct.baseokhttpdemo.util;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.com.zlct.baseokhttpdemo.R;
import cn.com.zlct.baseokhttpdemo.custom.CircleTextView;


/**
 * ToolBar工具类
 */
public class ToolBarUtil {

    /**
     * 初始化ToolBar -- 无字
     * @param title 中间的文字
     * @param clickListener 左侧后退按钮回调监听
     */
    public static void initToolBar(Toolbar toolBar, String title, View.OnClickListener clickListener) {
//        toolBar.setBackgroundColor(ContextCompat.getColor(toolBar.getContext(), R.color.colorAccent));
        init(toolBar, title, clickListener);
    }

    /**
     * 初始化ToolBar -- text
     * @param title 中间的文字
     * @param clickListener 左侧后退按钮回调监听
     * @param next 右侧的文字
     * @param clickListenerNext 右侧按钮回调监听
     */
    public static void initToolBar(Toolbar toolBar, String title, View.OnClickListener clickListener, String next,
                                   View.OnClickListener clickListenerNext) {
//        toolBar.setBackgroundColor(ContextCompat.getColor(toolBar.getContext(), R.color.colorAccent));
        init(toolBar, title, clickListener, next, clickListenerNext);
    }

    /**
     * 初始化ToolBar -- icon
     * @param title 中间的文字
     * @param clickListener 左侧后退按钮回调监听
     * @param resId 右侧的图标
     * @param clickListenerNext 右侧按钮回调监听
     */
    public static CircleTextView initToolBar(Toolbar toolBar, String title, View.OnClickListener clickListener, int resId,
                                             View.OnClickListener clickListenerNext) {
//        toolBar.setBackgroundColor(ContextCompat.getColor(toolBar.getContext(), R.color.colorAccent));
        init(toolBar, title, clickListener, resId, clickListenerNext);
        CircleTextView circleView = (CircleTextView) toolBar.findViewById(R.id.toolbar_iconCartDot);
        circleView.setBackgroundColor(Color.WHITE);
        return circleView;
    }

    /**
     * 初始化ToolBar -- 搜索
     */
    public static void initToolBar(Toolbar toolBar, View.OnClickListener clickListener) {
//        toolBar.setBackgroundColor(ContextCompat.getColor(toolBar.getContext(), R.color.colorAccent));
        View toolbarBack =  toolBar.findViewById(R.id.toolbar_searchBack);
        View toolbarSearch =  toolBar.findViewById(R.id.toolbar_searchGo);
        toolbarBack.setOnClickListener(clickListener);
        toolbarSearch.setOnClickListener(clickListener);
    }

    /**
     * 公共初始化方法 -- 无字
     */
    public static void init(Toolbar toolBar, String title, View.OnClickListener clickListener) {
        ImageButton toolbarBack;
        TextView toolbarTitle;
        if (toolBar.getId() == R.id.toolbar_text) {
            toolbarBack = (ImageButton) toolBar.findViewById(R.id.toolbar_back);
            toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        } else {
            toolbarBack = (ImageButton) toolBar.findViewById(R.id.toolbar_iconBack);
            toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbar_iconTitle);
        }
        toolbarBack.setOnClickListener(clickListener);
        toolbarTitle.setText(title);
    }

    /**
     * 公共初始化方法 -- text
     */
    private static void init(Toolbar toolBar, String title, View.OnClickListener clickListener, String next,
                             View.OnClickListener clickListenerNext) {
        ImageButton toolbarBack = (ImageButton) toolBar.findViewById(R.id.toolbar_back);
        TextView toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        TextView toolbarNext = (TextView) toolBar.findViewById(R.id.toolbar_next);
        toolbarBack.setOnClickListener(clickListener);
        toolbarTitle.setText(title);
        toolbarNext.setVisibility(View.VISIBLE);
        toolbarNext.setText(next);
        toolbarNext.setOnClickListener(clickListenerNext);
    }

    /**
     * 公共初始化方法 -- icon
     */
    private static void init(Toolbar toolBar, String title, View.OnClickListener clickListener, int resId,
                             View.OnClickListener clickListenerNext) {
        ImageButton toolbarBack = (ImageButton) toolBar.findViewById(R.id.toolbar_iconBack);
        TextView toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbar_iconTitle);
        ImageButton toolbarNext = (ImageButton) toolBar.findViewById(R.id.toolbar_iconNext);
        toolbarBack.setOnClickListener(clickListener);
        toolbarTitle.setText(title);
        toolbarNext.setVisibility(View.VISIBLE);
        if (resId != -1) {
            toolbarNext.setImageResource(resId);
        }
        toolbarNext.setOnClickListener(clickListenerNext);
    }
}
