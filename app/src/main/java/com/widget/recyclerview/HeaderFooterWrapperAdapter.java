package com.widget.recyclerview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cwj on 16/8/26.
 * RecyclerView.Adapter包装类
 * todo:notify时方法的优化
 */
class HeaderFooterWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_HEADER_TYPE = -20000;
    private static final int BASE_FOOTER_TYPE = -10000;

    private SparseArrayCompat<View> headerInfos = new SparseArrayCompat<>();
    private SparseArrayCompat<View> footerInfos = new SparseArrayCompat<>();

    private RecyclerView.Adapter<RecyclerView.ViewHolder> innerAdapter;

    public HeaderFooterWrapperAdapter(@Nullable List<View> headerViews, @Nullable List<View> footerViews, @NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> innerAdapter) {
        this.innerAdapter = innerAdapter;
        initHeaderFooter(headerViews, headerInfos, BASE_HEADER_TYPE);
        initHeaderFooter(footerViews, footerInfos, BASE_FOOTER_TYPE);
    }

    private void initHeaderFooter(List<View> views, SparseArrayCompat<View> infos, int baseType) {
        for (int i = 0; views != null && i < views.size(); i++) {
            infos.put(baseType + i, views.get(i));
        }
    }

    int getHeaderCounts() {
        return headerInfos.size();
    }

    int getFooterCounts() {
        return footerInfos.size();
    }

    private int getRealItemCount() {
        return innerAdapter.getItemCount();
    }

    boolean isHeaderView(int pos) {
        return pos < getHeaderCounts();
    }

    boolean isFooterView(int pos) {
        return pos >= getHeaderCounts() + getRealItemCount();
    }

    void addHeaderView(View headerView) {
        int currentMaxKey = BASE_HEADER_TYPE - 1;//当前header最大的key(viewType)
        if (headerInfos.size() > 0) {
            currentMaxKey = headerInfos.keyAt(headerInfos.size() - 1);
        }
        headerInfos.put(currentMaxKey + 1, headerView);
        innerAdapter.notifyItemInserted(headerInfos.size() - 1);//header的最后一个刷新
    }

    void addFooterView(View footerView) {
        int currentMaxKey = BASE_FOOTER_TYPE - 1;//当前footer最大的key(viewType)
        if (footerInfos.size() > 0) {
            currentMaxKey = footerInfos.keyAt(footerInfos.size() - 1);
        }
        footerInfos.put(currentMaxKey + 1, footerView);
        innerAdapter.notifyItemInserted(getItemCount() - 1);//footer的最后一个刷新
    }

    void removeHeaderView(View headerView) {
        int index = headerInfos.indexOfValue(headerView);
        if (index >= 0) {//存在此view
            headerInfos.removeAt(index);
            innerAdapter.notifyItemRemoved(index);//移除该header
        }
    }

    void removeFooterView(View footerView) {
        int index = footerInfos.indexOfValue(footerView);
        if (index >= 0) {//存在此view
            footerInfos.removeAt(index);
            innerAdapter.notifyItemRemoved(getHeaderCounts() + getRealItemCount() + index - 1);//移除该footer
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("viewType:", "" + viewType);
        if (headerInfos.indexOfKey(viewType) >= 0) {//存在该key对应的header
            //创建header的viewHolder
            return new RecyclerView.ViewHolder(headerInfos.get(viewType)) {
            };
        } else if (footerInfos.indexOfKey(viewType) >= 0) {//存在该key对应的footer
            //创建footer的viewHolder
            return new RecyclerView.ViewHolder(footerInfos.get(viewType)) {
            };
        }
        return innerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        Log.i("viewTypePos:", "" + position);
        if (isHeaderView(position)) {
            return headerInfos.keyAt(position);
        } else if (isFooterView(position)) {
            return footerInfos.keyAt(position - getHeaderCounts() - getRealItemCount());
        }
        return innerAdapter.getItemViewType(position - getHeaderCounts());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position) || isFooterView(position)) {
            return;
        }
        innerAdapter.onBindViewHolder(holder, position - getHeaderCounts());
    }

    @Override
    public int getItemCount() {
        return getHeaderCounts() + getRealItemCount() + getFooterCounts();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {//grid模式
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderView(position) || isFooterView(position)) {//header和footer占用一整行
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;//默认大小,1
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());//重新设置一下span以便刷新lookup的改变
        }
        innerAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        Log.i("getLayoutPosition:", "" + pos);
        if (isHeaderView(pos) || isFooterView(pos)) {//header和footer占用一整行,需要使用LayoutParams设置
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) params;
                p.setFullSpan(true);
                holder.itemView.setLayoutParams(p);
            }
        } else {
            innerAdapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public long getItemId(int position) {
        if (isHeaderView(position) || isFooterView(position)) {
            return -1;
        }
        return innerAdapter.getItemId(position - getHeaderCounts());
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        if (isHeaderView(pos) || isFooterView(pos)) {
            return;
        }
        innerAdapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        if (isHeaderView(pos) || isFooterView(pos)) {
            return super.onFailedToRecycleView(holder);
        }
        return innerAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        if (isHeaderView(pos) || isFooterView(pos)) {
            return;
        }
        innerAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        innerAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        innerAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        innerAdapter.onDetachedFromRecyclerView(recyclerView);
    }

}
