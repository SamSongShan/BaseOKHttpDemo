package cn.com.zlct.baseokhttpdemo.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.com.zlct.baseokhttpdemo.R;


/**
 * 组合导航控件
 */
public class NavView extends FrameLayout{

    private LinearLayout llChecked, llUnChecked;
    private int checkedRes, unCheckedRes, count, padding;//padding单位是像素
    private LinearLayout.LayoutParams checkedLP, unCheckedLP;
    private ImageView imageView;//被选中的图片
    private int index;//当前被选中的下标

    public NavView(Context context) {
        super(context);
        init();
    }

    public NavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.custom_nv, this, true);
        llChecked = (LinearLayout) findViewById(R.id.ll_checked);
        llUnChecked = (LinearLayout) findViewById(R.id.ll_unchecked);

        checkedLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        unCheckedLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        loadView();
    }

    /**
     * 解析自定义属性
     */
    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.navView);
        checkedRes = typedArray.getResourceId(R.styleable.navView_checkedImg, 0);
        unCheckedRes = typedArray.getResourceId(R.styleable.navView_uncheckedImg, 0);
        count = typedArray.getInteger(R.styleable.navView_count, 0);
        padding = typedArray.getInteger(R.styleable.navView_padding, 12);
        typedArray.recycle();
    }

    /**
     * 加载控件
     */
    private void loadView(){
        if(count > 1){
            llUnChecked.removeAllViews();
            llChecked.removeAllViews();
            for(int i = 0; i < count; i++){
                ImageView unchecked = new ImageView(getContext());
                unchecked.setImageResource(unCheckedRes);
                unchecked.setLayoutParams(unCheckedLP);
                unchecked.setPadding(padding, 0, padding, 0);
                llUnChecked.addView(unchecked);
            }

            imageView = new ImageView(getContext());
            imageView.setImageResource(checkedRes);
            imageView.setLayoutParams(checkedLP);
            imageView.setPadding(padding, 0, padding, 0);
            llChecked.addView(imageView);
        }
    }

    /**
     * 结合ViewPager使用的方法 -- 设置位移
     */
    public void setNavAddress(int position, float pyl){
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.leftMargin = (int) (imageView.getWidth() * (position + pyl));
        imageView.setLayoutParams(layoutParams);
        index = position;
    }

    public void setCurrentItem(int position){
        setNavAddress(position, 0);
    }

    /**
     * 设置图标的数量
     */
    public void setCount(int count){
        this.count = count;
        loadView();
    }

    public int getCount(){
        return this.count;
    }

    /**
     * 选中上一个
     */
    public void above(){
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.leftMargin = (imageView.getWidth() * ((index-- + 1)));
        imageView.setLayoutParams(layoutParams);
    }

    /**
     * 选中下一个
     */
    public void next(){
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.leftMargin = (imageView.getWidth() * ((index++ + 1)));
        imageView.setLayoutParams(layoutParams);
    }
}
