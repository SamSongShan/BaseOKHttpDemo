package cn.com.zlct.baseokhttpdemo.base;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.zlct.baseokhttpdemo.util.Constant;
import cn.com.zlct.baseokhttpdemo.util.PhoneUtil;


/**
 * RecyclerView的多(单)布局 适配器
 * 可以实现ListView、GridView和瀑布流的效果
 *
 * 配置数据 -- 必须重写{@link #onBindHolder(RecyclerViewHolder, Object, int)}
 * 添加头部 -- {@link #addHeaderView(View)}
 *         -- 添加头部后，部分item刷新(如{@link #notifyItemChanged(int)})时
 *         -- 不可直接使用原始数据源的下标
 *         -- 因父类方法为final，不可重写，添加头部时可调用自定义方法{@link #notifyItemChange(int)}
 * 多布局实现 -- 必须重写{@link #getItemType(T)}方法
 *           -- 强制要求数据源中有一个type名称的属性，重写时返回该值
 *           -- type非0时 当前item默认宽度撑满
 *           -- 如需不撑满 需重写{@link #isItemFullSpan(int)} 根据type的值返回是否撑满
 */
public abstract class AbsRecyclerViewAdapter<T> extends RecyclerView.Adapter
        <AbsRecyclerViewAdapter.RecyclerViewHolder> {

    protected Context context;
    protected List<T> data;
    private int[] resId;//多布局的布局文件

    public static final int TYPE_EMPTY = -1;//空数据的类型
    public static final int TYPE_HEADER = -2;//第一条头部的类型
    public static final int TYPE_FOOTER = -100;//第一条尾部的类型
    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFooterViews = new ArrayList<>();
    private View mEmpty;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private boolean isFullScreen;

    public AbsRecyclerViewAdapter(Context context, int... resId) {
        this.context = context;
        this.resId = resId;
        data = new ArrayList<>();
    }

    public AbsRecyclerViewAdapter(Context context, List<T> data, int... resId) {
        this.context = context;
        this.resId = resId;
        this.data = data;
    }

    public void setData(List<T> data){
        this.data = data;
        setEmptyVisible();
        this.notifyDataSetChanged();
    }

    public void addData(List<T> data){
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public List<T> getData(){
        return data;
    }

    public void deleteItem(int position){
        this.data.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * Item点击监听 -- 添加头部后，头部的点击事件需自行处理
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    /**
     * Item长按监听 -- 添加头部后，头部的长按事件需自行处理
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return getDataCount() + getHeaderCount() + getFooterCount() + getEmptyCount();
    }

    public int getDataCount() {
        return data == null ? 0 : data.size();
    }

    public int getEmptyCount() {
        int size = getDataCount() + getHeaderCount() + getFooterCount();
        return mEmpty != null && size == 0 ? 1 : 0;
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getFooterCount() {
        return mFooterViews.size();
    }

    public void addHeaderView(View header) {
        if (header == null) {
            throw new RuntimeException("Header is null");
        }
        if (mHeaderViews.size() >= 98) {
            throw new RuntimeException("Header is too much");
        }
        mHeaderViews.add(header);
        this.notifyDataSetChanged();
    }

    public void clearHeader() {
        if (getHeaderCount() > 0) {
            mHeaderViews.clear();
            this.notifyDataSetChanged();
        }
    }

    public void addFooterView(View footer) {
        if (footer == null) {
            throw new RuntimeException("Footer is null");
        }
        mFooterViews.add(footer);
        this.notifyDataSetChanged();
    }

    public void clearFooter() {
        if (getFooterCount() > 0) {
            mFooterViews.clear();
            this.notifyDataSetChanged();
        }
    }

    public void setEmptyView(View empty) {
        if (empty == null) {
            throw new RuntimeException("Empty is null");
        }
        mEmpty = empty;
        setEmptyLayoutParams();
    }

    public void setEmptyView(int resId) {
        setEmptyView(LayoutInflater.from(context).inflate(resId, null));
    }

    public void setEmptyLayoutParams() {
        mEmpty.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT));
    }

    public void setEmptyVisible() {
        if (mEmpty == null) {
            return;
        }
        if (mEmpty.getVisibility() != View.VISIBLE) {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    public View getEmptyView() {
        return mEmpty;
    }

    /**
     * 修改空数据提示中的文字
     */
    public void setEmptyTips(int id, String tips) {
        if (mEmpty != null) {
            View v = mEmpty.findViewById(id);
            if (v instanceof TextView) {
                ((TextView) v).setText(tips);
            }
        }
    }

    public void notifyItemChange(int position) {
        super.notifyItemChanged(position + getHeaderCount());
    }

    public void notifyItemRangeChange(int positionStart, int itemCount) {
        super.notifyItemRangeChanged(positionStart + getHeaderCount(), itemCount);
    }

    public void notifyItemInsert(int position) {
        super.notifyItemInserted(position + getHeaderCount());
    }

    public void notifyItemMove(int fromPosition, int toPosition) {
        super.notifyItemMoved(fromPosition + getHeaderCount(), toPosition + getHeaderCount());
    }

    public void notifyItemRangeInsert(int positionStart, int itemCount) {
        super.notifyItemRangeInserted(positionStart + getHeaderCount(), itemCount);
    }

    public void notifyItemRemove(int position) {
        super.notifyItemRemoved(position + getHeaderCount());
    }

    public void notifyItemRangeRemove(int positionStart, int itemCount) {
        super.notifyItemRangeRemoved(positionStart + getHeaderCount(), itemCount);
    }

    /**
     * 因支持添加头部功能 该方法不能重写 也不能直接在外部调用
     * 使用多布局时 只能重写 {@link #getItemType(T)}
     */
    @Override
    public int getItemViewType(int position) {
        int headCount = getHeaderCount();
        if (position < headCount) {//头部
            return TYPE_HEADER - position;
        }
        if (getEmptyCount() > 0) {//空数据
            return TYPE_EMPTY;
        }
        int nonFootCount = headCount + getDataCount();//头部和数据的数量
        if (position >= nonFootCount) {//尾部
            return TYPE_FOOTER + nonFootCount - position;
        }
        return getItemType(data.get(position - headCount));
    }

    /**
     * 当前position所对应的Item的类型
     * 默认Item的类型为0，多布局时其他Item的类型依次递增
     */
    public int getItemType(T d) {
        return super.getItemViewType(0);
    }

    /**
     * 当前position所对应的Item 是否宽度撑满 -- 默认非0撑满 (此方法不能重写)
     */
    private boolean isFullSpan(int position) {
        int type = getItemViewType(position);
        if (type == 0) {//默认Item
            return false;
        } else if (type < 0) {//空数据、头部、尾部 默认撑满
            return true;
        } else {//其他类型
            return isItemFullSpan(type);
        }
    }

    /**
     * 多布局时 类型为type的item 是否宽度撑满 -- 默认非0撑满
     */
    protected boolean isItemFullSpan(int type) {
        return true;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return new RecyclerViewHolder(mEmpty);
        }
        if (viewType <= TYPE_FOOTER) {
            return new RecyclerViewHolder(mFooterViews.get(TYPE_FOOTER - viewType));
        }
        if (viewType <= TYPE_HEADER) {
            return new RecyclerViewHolder(mHeaderViews.get(TYPE_HEADER - viewType));
        }
        View v = LayoutInflater.from(context).inflate(resId[viewType], parent, false);
        return new RecyclerViewHolder(v);
    }

    /**
     * 因支持添加头部功能 该方法不能重写 只能重写 {@link #onBindHolder(RecyclerViewHolder, Object, int)}
     */
    @Override
    public void onBindViewHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        if (getItemViewType(position) < 0) {
            return;
        }
        int truePosition = position - getHeaderCount();
        onBindHolder(holder, data.get(truePosition), truePosition);
    }

    /**
     * 配置数据 -- holder的类型必须是 AbsRecyclerViewAdapter.RecyclerViewHolder
     * 如需给整条Item setTag()，请调用 {@link RecyclerViewHolder#setItemTag(int, Object)}
     */
    public abstract void onBindHolder(AbsRecyclerViewAdapter.RecyclerViewHolder holder, T d, int position);

    /**
     * GridLayoutManager时 设置type不为0的item 占整行位置
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isFullSpan(position) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * StaggeredGridLayoutManager时 设置type不为0的item 占整行位置
     */
    @Override
    public void onViewAttachedToWindow(AbsRecyclerViewAdapter.RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if(lp != null) {
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) lp;
                layoutParams.setFullSpan(holder.getParent().isFullSpan(holder.getLayoutPosition()));
            } else {
                /**
                 * LinearLayoutManager时 头部Item撑满
                 */

                if (isFullScreen) {
                   // lp.width = RecyclerView.LayoutParams.WRAP_CONTENT;
                    lp.width = (PhoneUtil.getPhoneWidth(context)-PhoneUtil.dp2px(context,30))/5;
                } else {
                    lp.width = RecyclerView.LayoutParams.MATCH_PARENT;
                }
                /*lp.width = RecyclerView.LayoutParams.MATCH_PARENT;*/
                holder.itemView.setLayoutParams(lp);
            }
        }
    }

    protected class RecyclerViewHolder extends RecyclerView.ViewHolder {

        Map<Integer, View> cacheMap = new HashMap();
        View layoutView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            if(onItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getItemViewType() >= 0) {
                            onItemClickListener.onItemClick(v, getLayoutPosition() - getHeaderCount());
                        }
                    }
                });
            }
            if (onItemLongClickListener != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (getItemViewType() >= 0) {
                            return onItemLongClickListener.onItemLongClick(v,
                                    getLayoutPosition() - getHeaderCount());
                        }
                        return true;
                    }
                });
            }
            this.layoutView = itemView;
        }

        private AbsRecyclerViewAdapter getParent() {
            return AbsRecyclerViewAdapter.this;
        }

        public View getView(int id){
            if(cacheMap.containsKey(id)){
                return cacheMap.get(id);
            } else {
                View view = layoutView.findViewById(id);
                cacheMap.put(id, view);
                return view;
            }
        }

        public RecyclerViewHolder setViewLayoutParams(int width, int height){
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) layoutView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            layoutView.setLayoutParams(layoutParams);
            return this;
        }

        /**
         * 给整条item设置tag
         */
        public RecyclerViewHolder setItemTag(int id, Object tag) {
            layoutView.setTag(id, tag);
            return this;
        }

        /**
         * 给item中控件设置点击事件，前提：需先设置 {@link #setOnItemClickListener(OnItemClickListener)}
         */
        public RecyclerViewHolder setOnClickListener(int id) {
            return setOnClickListenerAndTag(id, null);
        }

        public RecyclerViewHolder setOnClickListenerAndTag(int id, Object tag) {
            View v = getView(id);
            if (tag != null) {
                v.setTag(tag);
            }
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null) {
                        int position = RecyclerViewHolder.this.getLayoutPosition();
                        if (position >= getHeaderCount() + getEmptyCount()) {
                            onItemClickListener.onItemClick(v, position - getHeaderCount());
                        }
                    }
                }
            });
            return this;
        }

        public RecyclerViewHolder setViewVisible(int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }
        public RecyclerViewHolder setVisible(int visible) {
            layoutView.setVisibility(visible);
            return this;
        }
        public RecyclerViewHolder setViewSelected(int id, boolean selected) {
            getView(id).setSelected(selected);
            return this;
        }

        public RecyclerViewHolder setClickListener(int id, View.OnClickListener listener) {
            getView(id).setOnClickListener(listener);
            return this;
        }

        public RecyclerViewHolder setClickListenerAndTag(int id, View.OnClickListener listener, Object tag) {
            View v = getView(id);
            v.setTag(tag);
            v.setOnClickListener(listener);
            return this;
        }

        /**
         * 绑定TextView
         */
        public RecyclerViewHolder bindTextView(int id, String text){
            TextView tv = (TextView) getView(id);
            tv.setText(text);
            return this;
        }

        /**
         * 绑定TextView
         */
        public RecyclerViewHolder bindTextViewWithClickListener(int id, String text, View.OnClickListener listener){
            TextView tv = (TextView) getView(id);
            tv.setText(text);
            tv.setOnClickListener(listener);
            return this;
        }

        /**
         * 绑定TextView
         */
        public RecyclerViewHolder bindTextViewWithSelected(int id, String text, boolean isSelect){
            TextView tv = (TextView) getView(id);
            tv.setText(text);
            tv.setSelected(isSelect);
            return this;
        }

        /**
         * 绑定TextView 含html标签
         */
        public RecyclerViewHolder bindTextViewWithHtml(int id, String text){
            TextView tv = (TextView) getView(id);
            tv.setText(Html.fromHtml(text));
            return this;
        }

        /**
         * 绑定ImageView
         */
        public RecyclerViewHolder bindImageView(int id, int imgId){
            ImageView iv = (ImageView) getView(id);
            iv.setImageResource(imgId);
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerViewHolder bindSimpleDraweeView(int id, String url){
            bindSimpleDraweeView(id, Uri.parse(url));
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerViewHolder bindSimpleDraweeView(int id, Uri uri){
            SimpleDraweeView sdv = (SimpleDraweeView) getView(id);
            sdv.setImageURI(uri);
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerViewHolder bindSimpleDraweeView(int id, int imgId){
            SimpleDraweeView sdv = (SimpleDraweeView) getView(id);
            sdv.setImageURI(Uri.parse("res:///" + imgId));
            return this;
        }

        /**
         * 绑定SimpleDraweeView
         */
        public RecyclerViewHolder bindSimpleDraweeViewBase(int id, String url){
            return bindSimpleDraweeView(id, Constant.URL.BaseImg + url);
        }

        /**
         * 设置进度条进度
         */
        public RecyclerViewHolder bindProgressBar(int id, int num){
            ProgressBar progressBar = (ProgressBar) getView(id);
            progressBar.setProgress(num);
            return this;
        }

        /**
         * 设置星星进度条
         */
        public RecyclerViewHolder bindRatingBar(int id, float rating){
            RatingBar ratingBar = (RatingBar) getView(id);
            ratingBar.setRating(rating);
            return this;
        }
    }

    /**
     * autor: tj
     * time:2017年9月22日16:51:49
     * 实现recyclerView横上滑动不全屏
     */
    public void setFullScreen(boolean value) {
        this.isFullScreen = value;
    }
}
