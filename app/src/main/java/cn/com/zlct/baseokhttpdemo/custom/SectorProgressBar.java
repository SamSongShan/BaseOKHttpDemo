package cn.com.zlct.baseokhttpdemo.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cn.com.zlct.baseokhttpdemo.R;


/**
 * 扇形 进度条
 */
public class SectorProgressBar extends View {

    private Paint mPaintCircle;
    private Paint mPaintRect;
    private int max;
    private int shape;
    private int progress;

    public SectorProgressBar(Context context) {
        this(context, null);
    }

    public SectorProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs);
        init();
    }

    /**
     * 解析自定义属性
     */
    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.sectorProgressBar);
        max = typedArray.getInteger(R.styleable.sectorProgressBar_max, 100);
        shape = typedArray.getInt(R.styleable.sectorProgressBar_shape, 0);
        typedArray.recycle();
    }

    private void init() {
        progress = 0;
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.GRAY);
        mPaintCircle.setAlpha(160);
        mPaintRect = new Paint();
        mPaintRect.setAntiAlias(true);
        mPaintRect.setColor(Color.RED);
        mPaintRect.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        //wrap_content
        setMeasuredDimension(sizeWidth, sizeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (shape){
            case 0://画圆底
                canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, mPaintCircle);
                break;
            case 1://画方底
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaintCircle);
                break;
        }
        if (progress > 0) {
            int surplus = (int) ((getWidth() / 2) * 0.42);
            RectF rectF = new RectF(-surplus, -surplus, getWidth() + surplus, getHeight() + surplus);
            canvas.drawArc(rectF, -90, progress, true, mPaintRect);
        }
    }

    public void setProgress(int gress) {
        if (gress * 360 / max > progress) {
            progress = gress * 360 / max;
            postInvalidate();
        }
    }

    public void initProgress() {
        progress = 0;
        postInvalidate();
    }
}
