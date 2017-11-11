package cn.com.zlct.baseokhttpdemo.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ScrollView嵌套GridView
 */
public class GridViewInScrollView extends GridView {

    public GridViewInScrollView(Context context) {
        super(context);
    }

    public GridViewInScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewInScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
