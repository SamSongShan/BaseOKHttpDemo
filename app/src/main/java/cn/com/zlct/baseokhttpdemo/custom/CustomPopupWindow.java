package cn.com.zlct.baseokhttpdemo.custom;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by 11355 on 2017/7/20.
 * 封装PopupWindow
 */

public class CustomPopupWindow {
    private PopupWindow mPopupWindow;
    private View contentview;
    private Context mContext;
    private  Bitmap ierceptionScreen;

    public CustomPopupWindow(Builder builder) {
        mContext = builder.context;
        contentview = LayoutInflater.from(mContext).inflate(builder.contentviewid, null);

        mPopupWindow =
                new PopupWindow(contentview, builder.width, builder.height, builder.fouse);

        //需要跟 setBackGroundDrawable 结合
        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (builder.isBlur){

            if (ierceptionScreen == null) {
                ierceptionScreen = getIerceptionScreen(builder);
            }

            contentview.setBackground(new BitmapDrawable(ierceptionScreen));
            // 获取壁纸管理器
           /* WallpaperManager wallpaperManager = WallpaperManager.getInstance(builder.context);
            // 获取当前壁纸
            Drawable wallpaperDrawable = wallpaperManager.getDrawable();
            // 将Drawable,转成Bitmap
            Bitmap bmp = ((BitmapDrawable) wallpaperDrawable).getBitmap();*/
            /*Bitmap bitmap = getBitmapFromDrawable(builder.context.getResources().getDrawable(R.drawable.shape_solid_black));
            contentview.setBackground(new BitmapDrawable(FastBlur.fastBlur(bitmap,builder.radius)));*/

        }

        mPopupWindow.setAnimationStyle(builder.animstyle);
        mPopupWindow.setOnDismissListener(builder.onDismissListener);

    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            //颜色Drawable
            if (drawable instanceof ColorDrawable) {
                //宽为2, 高为2 ??
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            //将 drawable 的内容绘制到 bitmap的canvas 上面去.
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /*
        截取屏幕
        * */
    @Nullable
    private Bitmap getIerceptionScreen(Builder builder) {
        // View是你需要截图的View
        View view = builder.activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        builder.activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = builder.activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = builder.activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap bitmap = Bitmap.createBitmap(b, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        bitmap = FastBlur.fastBlur(bitmap, builder.radius);
        if (bitmap != null) {
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * popup 消失
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 根据id获取view
     *
     * @param viewid
     * @return
     */
    public View getItemView(int viewid) {
        if (mPopupWindow != null) {
            return this.contentview.findViewById(viewid);
        }
        return null;
    }

    /**
     * 根据父布局，显示位置
     *
     * @param rootviewid
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public CustomPopupWindow showAtLocation(int rootviewid, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            View rootview = LayoutInflater.from(mContext).inflate(rootviewid, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= 24) {

                    DisplayMetrics dm = new DisplayMetrics();
                    WindowManager windowMgr = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
                    windowMgr.getDefaultDisplay().getRealMetrics(dm);
                    // 获取高度

                    // 获取宽度
                    int width = dm.widthPixels;

                    Rect visibleFrame = new Rect();
                    rootview.getGlobalVisibleRect(visibleFrame);
                    int height = dm.heightPixels-visibleFrame.bottom;
                    //int height = rootview.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                    mPopupWindow.setHeight(height);
                    mPopupWindow.showAsDropDown(rootview, x, y, gravity);
                } else {
                    mPopupWindow.showAsDropDown(rootview, x, y, gravity);
                }
            } else {
                mPopupWindow.showAtLocation(rootview, x, y, gravity);

            }        }
        return this;
    }

    /**
     * 根据id获取view ，并显示在该view的位置
     *
     * @param targetviewId
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    public CustomPopupWindow showAsLaction(int targetviewId, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {
            View targetview = LayoutInflater.from(mContext).inflate(targetviewId, null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= 24) {
                    Rect visibleFrame = new Rect();
                    targetview.getGlobalVisibleRect(visibleFrame);
                    int height = targetview.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                    mPopupWindow.setHeight(height);
                    mPopupWindow.showAsDropDown(targetview, offx, offy, gravity);
                } else {
                    mPopupWindow.showAsDropDown(targetview, offx, offy, gravity);
                }
            } else {
                mPopupWindow.showAtLocation(targetview, offx, offy, gravity);

            }


        }

        return this;
    }

    /**
     * 显示在 targetview 的不同位置
     *
     * @param targetview
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    public CustomPopupWindow showAsLaction(View targetview, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= 24) {
                    DisplayMetrics dm = new DisplayMetrics();
                    WindowManager windowMgr = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
                    windowMgr.getDefaultDisplay().getRealMetrics(dm);
                    // 获取高度

                    // 获取宽度
                    int width = dm.widthPixels;

                    Rect visibleFrame = new Rect();
                    targetview.getGlobalVisibleRect(visibleFrame);
                    int height = dm.heightPixels-visibleFrame.bottom;
                    //int height = targetview.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                    mPopupWindow.setHeight(height);
                    mPopupWindow.showAsDropDown(targetview, offx, offy, gravity);
                } else {
                    mPopupWindow.showAsDropDown(targetview, offx, offy, gravity);
                }
            } else {
                mPopupWindow.showAtLocation(targetview, offx, offy, gravity);

            }
        }
        return this;
    }



    /**
     * 根据id设置焦点监听
     *
     * @param viewid
     * @param listener
     */
    public void setOnFocusListener(int viewid, View.OnFocusChangeListener listener) {
        View view = getItemView(viewid);
        view.setOnFocusChangeListener(listener);
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }


    /**
     * builder 类
     */
    public static class Builder {
        private int contentviewid;
        private int width;
        private int height;
        private boolean fouse;
        private boolean outsidecancel;
        private int animstyle;
        private Context context;
        private Activity activity;
        private PopupWindow.OnDismissListener onDismissListener;
        private boolean isBlur;
        private int radius;


        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }


        public Builder setContentView(int contentviewid) {
            this.contentviewid = contentviewid;
            return this;
        }

        public Builder setwidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setheight(int height) {
            this.height = height;
            return this;
        }

        public Builder setFouse(boolean fouse) {
            this.fouse = fouse;
            return this;
        }

        public Builder setOutSideCancel(boolean outsidecancel) {
            this.outsidecancel = outsidecancel;
            return this;
        }

        public Builder setAnimationStyle(int animstyle) {
            this.animstyle = animstyle;
            return this;
        }

        public Builder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }
        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }
        //是否高斯模糊
        public Builder isBlur(boolean isBlur) {
            this.isBlur = isBlur;

            return this;
        }

        public CustomPopupWindow builder() {
            return new CustomPopupWindow(this);
        }
    }

    //0.0f和1.0f之间，0.0f完全不暗，1.0f全暗
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        // context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);

    }

}
