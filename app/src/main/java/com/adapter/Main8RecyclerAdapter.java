package com.adapter;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.manager.BaseManager;
import com.manager.BulletinManager;
import com.manager.DashboardManager;

/**
 * Created by cwj on 16/8/31.
 */
final public class Main8RecyclerAdapter extends RecyclerView.Adapter<Main8RecyclerAdapter.ViewHolder> {

    private static final int BLOCKS_COUNT = 2;

    @IntDef({BULLETIN, DASHBOARD})
    public @interface Blocks {
    }

    public static final int BULLETIN = 0;
    public static final int DASHBOARD = 1;

    private Object[] datas = new Object[BLOCKS_COUNT];

    public void update(@Blocks int blocks, Object data) {
        datas[blocks] = data;
        notifyItemChanged(blocks);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        BaseManager baseManager;
        switch (viewType) {
            case BULLETIN:
                baseManager = new BulletinManager(inflater, parent);
                break;
            case DASHBOARD:
                baseManager = new DashboardManager(inflater, parent);
                break;
            default:
                throw new IllegalArgumentException("item type must be one of BLOCKS");
        }
        return new ViewHolder(baseManager);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updView(datas[position]);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return BLOCKS_COUNT;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private BaseManager baseManager;

        public ViewHolder(BaseManager baseManager) {
            super(baseManager.getView());
            this.baseManager = baseManager;
        }

        public void updView(Object data) {
            baseManager.updView(data);
        }

    }
}
