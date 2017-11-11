package cn.com.zlct.baseokhttpdemo.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Administrator on 2017/9/25.
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int mDividerWidth;

    public GridDividerItemDecoration(int height, @ColorInt int color) {
        mDividerWidth = height;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    public static final int[] ATTRS_B = new int[]{android.R.attr.listDividerAlertDialog};
    public static final int[] ATTRS_W = new int[]{android.R.attr.childDivider};
    private Drawable mDivider;
    private int headerCount;
    private boolean isDrawAllLine;

    private boolean isDraw;


    public GridDividerItemDecoration(Context context) {
        this(context, ATTRS);
    }

    /**
     * 使用宽分隔线 ATTRS_B 时使用
     */
    public GridDividerItemDecoration(Context context, int[] divider) {
        final TypedArray a = context.obtainStyledAttributes(divider);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 头部不添加分隔线时调用 -- 默认全部添加
     */
    public GridDividerItemDecoration(Context context, int headerCount) {
        this(context);
        this.headerCount = headerCount;
    }

    /**
     * 画所有分隔线 不考虑最后一列、最后一行 特殊布局时使用
     */
    public GridDividerItemDecoration(Context context, boolean isDrawAllLine) {
        this(context);
        this.isDrawAllLine = isDrawAllLine;
    }

    public GridDividerItemDecoration(Context context, int headerCount, int[] divider) {
        this(context, headerCount);
        final TypedArray a = context.obtainStyledAttributes(divider);
        mDivider = a.getDrawable(0);
        a.recycle();

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            if ((i % getSpanCount(parent)) == 0) {
                mDivider.setBounds(0, top, mDivider.getIntrinsicWidth(), bottom + mDivider.getIntrinsicHeight());
                mDivider.draw(c);
            }
        }
    }

    private boolean isLastColumn(RecyclerView parent, int position, int spanCount, int childCount) {
        if (isDrawAllLine) {
            return false;
        }
        if (position < headerCount) {
            return true;
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((position - headerCount + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            } else {
                return false;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((position - headerCount + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                    return true;
                } else {
                    return false;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (position >= childCount) {// 如果是最后一列，则不需要绘制右边
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isLastRaw(RecyclerView parent, int position, int spanCount, int childCount) {
        if (isDrawAllLine) {
            return false;
        }
        if (position < headerCount) {
            return true;
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (position >= childCount)// 如果是最后一行，则不需要绘制底部
                return false;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (position >= childCount) {
                    return true;
                }
            } else {// StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((position + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, int position, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (position >= headerCount) {
            if (isLastRaw(parent, position, spanCount, childCount)) {// 如果是最后一行，则不需要绘制底部
                outRect.set(mDivider.getIntrinsicWidth(), 0, mDivider.getIntrinsicWidth(), 0);

            } else if (isLastColumn(parent, position, spanCount, childCount)) {// 如果是最后一列，则不需要绘制右边
                outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(), mDivider.getIntrinsicWidth(), 0);
            } else {
                outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(), 0, 0);
            }

        }
    }

}
