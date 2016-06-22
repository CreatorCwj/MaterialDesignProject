package com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.materialdesign.R;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cwj on 15/12/11.
 */
public class NormalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    enum Type {
        NORMAL,
        IMAGE,
    }

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> mTitles;

    public NormalRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
//        mTitles = context.getResources().getStringArray(R.array.titles);
        mTitles = getTitles();
    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        boolean isBig = true;
        for (int i = 0; i < 15; i++) {
            if (isBig) {
                titles.add("Title" + i + "\n" + "big" + "\n" + "big");
            } else {
                titles.add("Title" + i);
            }
            isBig = !isBig;
        }
        return titles;
    }

    @Override
    public int getItemViewType(int position) {
//        int type = (position % 2 == 0) ? Type.NORMAL.ordinal() : Type.IMAGE.ordinal();
        int type = Type.NORMAL.ordinal();
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Type.NORMAL.ordinal()) {
            return new NormalViewHolder(layoutInflater.inflate(R.layout.item_view, parent, false));
        } else if (viewType == Type.IMAGE.ordinal()) {
            return new NormalImageViewHolder(layoutInflater.inflate(R.layout.item_image_view, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder != null) {
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                normalViewHolder.textView.setText(mTitles.get(position));
            } else if (holder instanceof NormalImageViewHolder) {
                NormalImageViewHolder normalImageViewHolder = (NormalImageViewHolder) holder;
                normalImageViewHolder.textView.setText(mTitles.get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    private void addData() {
        if (mTitles != null) {
            mTitles.add(1, "add new");
            notifyItemInserted(1);//进入动画(默认效果)
        }
    }

    private void delData(int position) {
        if (mTitles != null) {
            mTitles.remove(position);
            notifyItemRemoved(position);
        }
    }

    protected class NormalViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public NormalViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addData();
                    Utils.showToast(NormalRecyclerAdapter.this.context, "onClick-Normal" + getLayoutPosition());
                }
            });
        }
    }

    protected class NormalImageViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public NormalImageViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delData(getLayoutPosition());
                    Utils.showToast(NormalRecyclerAdapter.this.context, "onClick-NormalImage" + getLayoutPosition());
                }
            });
        }
    }
}
