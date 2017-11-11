package cn.com.zlct.baseokhttpdemo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Stack;

/**
 * 数字密码输入框
 */
public class InputPassword extends android.support.v7.widget.AppCompatTextView {

    private int passwordLength = 6;
    private int borderWidth = 4;
    private int borderRadius = 8; //边框四个角的角度px
    private int borderColor = 0xffcccccc;
    private int passwordWidth = 14;
    private int passwordColor = Color.BLACK;
    private int defaultSplitLineWidth = 3;

    private Paint borderPaint;
    private Paint passwordPaint;

    private int textLength;
    private int defaultContentMargin = 2;
    private OnFinishListener onFinishListener;
    private Stack<String> pwdQ = new Stack<>();

    public InputPassword(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public InputPassword(Context context, AttributeSet attr) {
        super(context, attr);
        init(context, attr);
    }

    private void init(Context context, AttributeSet attr) {
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.FILL);
        passwordPaint = new Paint();
        passwordPaint.setAntiAlias(true);
        passwordPaint.setColor(passwordColor);
        passwordPaint.setStrokeWidth(passwordWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        //画边框
        RectF rect = new RectF(0, 0, width, height);
        borderPaint.setColor(borderColor);
        canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint);
        //画内容区域
        RectF rectContent = new RectF(rect.left + defaultContentMargin, rect.top + defaultContentMargin,
                rect.right - defaultContentMargin, rect.bottom - defaultContentMargin);
        borderPaint.setColor(Color.parseColor("#EEEEEE"));
        canvas.drawRoundRect(rectContent, borderRadius, borderRadius, borderPaint);

        //画分割线:分割线数量比密码数少1
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        for (int i = 1; i < passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        //画密码内容
        float px, py = height / 2;
        float halfWidth = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            px = width * i / passwordLength + halfWidth;
            canvas.drawCircle(px, py, passwordWidth, passwordPaint);
        }
    }

    public void addPwd(String tPwd) {
        textLength = pwdQ.size();
        if (textLength < 6) {
            pwdQ.add(tPwd);
            textLength = pwdQ.size();

            if (textLength == 6) {
                StringBuffer sb = new StringBuffer();
                for (String s : pwdQ) {
                    sb.append(s);
                }
                onFinishListener.setOnPasswordFinished(sb.toString());
            }
            postInvalidate();
        }
    }

    public void reSetView(){
        if (null!= pwdQ) {
            pwdQ.clear();
        }
        textLength =0;
        invalidate();
    }

    public void deletePwd() {
        if (!pwdQ.isEmpty()) {
            pwdQ.pop();
            textLength = pwdQ.size();
            if (textLength < 7) {
                invalidate();
            }
        }
    }

    public void reset() {
        if (!pwdQ.isEmpty()) {
            pwdQ.clear();
        }
        postInvalidate();
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface OnFinishListener {
         void setOnPasswordFinished(String pwd);
    }
}

