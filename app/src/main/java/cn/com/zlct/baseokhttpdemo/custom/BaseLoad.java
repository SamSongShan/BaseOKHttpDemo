package cn.com.zlct.baseokhttpdemo.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 加载中控件 基类
 */
public abstract class BaseLoad extends View {

    public ValueAnimator valueAnimator;

    public BaseLoad(Context context) {
        this(context, null);
    }

    public BaseLoad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLoad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitPaint();
    }

    protected abstract void InitPaint();

    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 500);
    }

    public void startAnim(int time) {
        stopAnim();
        startViewAnim(0f, 1f, time);
    }

    public void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();

            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
            if (OnStopAnim() == 0) {
                valueAnimator.setRepeatCount(0);
                valueAnimator.cancel();
                valueAnimator.end();
            }
        }
    }

    private ValueAnimator startViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(SetAnimRepeatCount());

        if (ValueAnimator.RESTART == SetAnimRepeatMode()) {
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        } else if (ValueAnimator.REVERSE == SetAnimRepeatMode()) {
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        }

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                OnAnimationUpdate(valueAnimator);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                OnAnimationRepeat(animation);
            }
        });

        if (!valueAnimator.isRunning()) {
            AnimIsRunning();
            valueAnimator.start();
        }
        return valueAnimator;
    }

    protected abstract void OnAnimationUpdate(ValueAnimator valueAnimator);

    protected abstract void OnAnimationRepeat(Animator animation);

    protected abstract int OnStopAnim();

    protected abstract int SetAnimRepeatMode();

    protected abstract int SetAnimRepeatCount();

    protected abstract void AnimIsRunning();
}
