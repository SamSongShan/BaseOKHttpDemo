package cn.com.zlct.baseokhttpdemo.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.util.Pools;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 竖直方向的TabLayout
 */
public class VerticalTabLayout extends ScrollView {

    private LinearLayout llVerScroll;//主布局
    private final ArrayList<Tab> mTabs = new ArrayList<>();//所有Tab的集合
    private Tab mSelectedTab;//选中的Tab
    private boolean flagAuto;//自动选中Tab时的标识
    private OnTabSelectedListener mOnTabSelectedListener;//回调监听
    private static final Pools.Pool<Tab> sTabPool = new Pools.SynchronizedPool<>(16);

    public VerticalTabLayout(Context context) {
        super(context);
        init();
    }

    public VerticalTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(false);//隐藏滚动条
        llVerScroll = new LinearLayout(getContext());
        llVerScroll.setOrientation(LinearLayout.VERTICAL);
        llVerScroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //ScrollView只能包含一个子控件
        addView(llVerScroll);
    }

    /**
     * 设置回调监听
     */
    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mOnTabSelectedListener = onTabSelectedListener;
    }

    /**
     * 清除回调监听
     */
    public void clearOnTabSelectedListener() {
        mOnTabSelectedListener = null;
    }

    /**
     * Tab选中状态改变时的回调方法
     */
    public interface OnTabSelectedListener {

        /**
         * Tab选中时回调
         */
        void onTabSelected(Tab tab);

        /**
         * Tab未选中时回调
         */
        void onTabUnselected(Tab tab);

        /**
         * Tab再次被选中时回调
         */
        void onTabReselected(Tab tab);
    }

    /**
     * 添加Tab
     */
    public void addTab(Tab tab) {
        addTab(tab, mTabs.isEmpty());
    }

    /**
     * 添加Tab
     */
    public void addTab(Tab tab, boolean setSelected) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        configureTab(tab, mTabs.size());
        addTabView(tab, setSelected);
    }

    public Tab newTab(){
        Tab tab = sTabPool.acquire();
        if (tab == null) {
            tab = new Tab(this);
        }
        tab.mParent = this;
        flagAuto = true;
        return tab;
    }

    /**
     * 把Tab包含的CustomView加入到VerticalTabLayout的子控件llVerScroll中
     */
    private void addTabView(Tab tab, boolean setSelected) {
        llVerScroll.addView(tab.mCustomView);
        if (setSelected) {
            tab.select();
        }
    }

    /**
     * 把Tab加入到VerticalTabLayout的mTabs中
     */
    private void configureTab(Tab tab, int position) {
        tab.setPosition(position);
        mTabs.add(position, tab);
        final int count = mTabs.size();
        for (int i = position + 1; i < count; i++) {
            mTabs.get(i).setPosition(i);
        }
    }

    public int getTabCount() {
        return mTabs.size();
    }

    public Tab getTabAt(int index) {
        return mTabs.get(index);
    }

    public int getSelectedTabPosition() {
        return mSelectedTab != null ? mSelectedTab.getPosition() : -1;
    }

    public void removeTabAt(int position) {
        final int selectedTabPosition = mSelectedTab != null ? mSelectedTab.getPosition() : 0;
        final Tab removedTab = mTabs.remove(position);
        if (removedTab != null) {
            removedTab.reset();
            sTabPool.release(removedTab);
        }
        final int newTabCount = mTabs.size();
        for (int i = position; i < newTabCount; i++) {
            mTabs.get(i).setPosition(i);
        }
        if (selectedTabPosition == position) {
            selectTab(mTabs.isEmpty() ? null : mTabs.get(Math.max(0, position - 1)));
        }
    }

    /**
     * 移除从start位置开始的Tab
     */
    public void removeTabStart(int start) {
        if (start <= mTabs.size()) {
            for (int i = mTabs.size() - 1; i >= start; i--) {
                Tab removedTab = mTabs.remove(i);
                if (removedTab != null) {
                    removedTab.reset();
                    sTabPool.release(removedTab);
                }
            }
        }
    }

    /**
     * 选中position位置的Tab
     */
    public void setCurrentTab(int position){
        selectTab(mTabs.get(position));
    }

    /**
     * 选中Tab
     */
    public void selectTab(Tab tab) {
        if (mSelectedTab != null && mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onTabUnselected(mSelectedTab);
        }
        if (tab != null) {
            if (tab == mSelectedTab) {
                mSelectedTab.setSelect(true);
                if (mOnTabSelectedListener != null) {
                    mOnTabSelectedListener.onTabReselected(mSelectedTab);
                }
            } else {
                if (mSelectedTab != null) {
                    mSelectedTab.setSelect(false);
                }
                mSelectedTab = tab;
                mSelectedTab.setSelect(true);
                if (mSelectedTab.getPosition() > 0 && flagAuto) {//非第一个
                    verticalScroll();//向下滑动
                }
                if (mOnTabSelectedListener != null) {
                    mOnTabSelectedListener.onTabSelected(mSelectedTab);
                }
            }
        }
    }

    private void verticalScroll() {
        Rect scrollBounds = new Rect();
        this.getHitRect(scrollBounds);
        if (!mSelectedTab.getCustomView().getLocalVisibleRect(scrollBounds)) {
            final int tabHeight = getWidgetHeight(mSelectedTab.getCustomView());//选中tab的高度
            this.post(new Runnable() {
                @Override
                public void run() {
                    VerticalTabLayout.this.scrollBy(0, tabHeight * mSelectedTab.getPosition());
                }
            });
            flagAuto = false;
        }
    }

    /**
     * 获取控件高度
     */
    public static int getWidgetHeight(View view){
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * VerticalTabLayout的Tab
     */
    public static final class Tab {

        private View mCustomView;
        private int mPosition = -1;
        private VerticalTabLayout mParent;

        Tab(VerticalTabLayout parent) {
            mParent = parent;
        }

        /**
         * 获取当前Tab使用的View
         */
        public View getCustomView() {
            return mCustomView;
        }

        /**
         * 设置当前Tab使用的View
         */
        public Tab setCustomView(View view) {
            mCustomView = view;
            mCustomView.setClickable(true);
            mCustomView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    select();
                }
            });
            return this;
        }

        public Tab setCustomView(int resId) {
            return setCustomView(LayoutInflater.from(mParent.getContext()).inflate(resId, null, false));
        }

        public Tab setText(CharSequence text) {
            if (mCustomView == null) {
                mCustomView = createCustomView();
            }
            if (mCustomView instanceof TextView) {
                ((TextView) mCustomView).setText(text);
            }
            mCustomView.setClickable(true);
            mCustomView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    select();
                }
            });
            return this;
        }

        public Tab setText(int resId) {
            return setText(mParent.getResources().getText(resId));
        }

        private TextView createCustomView() {
            TextView textView = new TextView(mParent.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lp);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(20, 10, 20, 10);
            return textView;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public void select() {
            if (mParent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            mParent.selectTab(this);
        }

        /**
         * 设置Tab中View的选中状态
         */
        public void setSelect(boolean selected){
            mCustomView.setSelected(selected);
        }

        /**
         * 清空当前Tab的数据
         */
        private void reset() {
            mParent.llVerScroll.removeView(mCustomView);
            mParent = null;
            mPosition = -1;
            mCustomView = null;
        }
    }
}
