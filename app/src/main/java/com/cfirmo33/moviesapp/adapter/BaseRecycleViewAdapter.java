package com.cfirmo33.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 06/07/17.
 */

public abstract class BaseRecycleViewAdapter<C, H extends BaseRecycleViewAdapter> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<C> list;
    private Context context;
    private OnClickListener<C> onClickListener;

    public BaseRecycleViewAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public List<C> getList() {
        if(list == null){
            list = new ArrayList<>();
        }
        return list;
    }

    public void setList(List<C> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void add(C r) {
        getList().add(r);
        notifyItemInserted(getList().size() - 1);
    }

    public void addAll(List<C> movieResults) {
        if(movieResults != null){
            for (C item : movieResults) {
                add(item);
            }
        }
    }

    public void remove(C r) {
        int position = getList().indexOf(r);
        if (position > -1) {
            getList().remove(position);
            notifyItemRemoved(position);
        }
    }

    public C getItem(int position) {
        return getList().get(position);
    }

    public interface OnClickListener<T> {
        void onClickListener(int position, T item);
    }

    protected OnClickListener<C> getOnClickListener() {
        if (onClickListener == null) {
            onClickListener = (position, item) -> {
            };
        }
        return onClickListener;
    }

    public H setOnClickListener(OnClickListener<C> onClickListener) {
        this.onClickListener = onClickListener;
        return (H)this;
    }
}
