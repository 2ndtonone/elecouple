package com.sulong.elecouple.ui.adapter;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu Qing on 2015/10/13.
 */
public abstract class BasePtrLoadMoreListAdapter<T> extends BaseAdapter implements PtrLoadMoreListAdapterAction<T> {

    protected List<T> mDataList = new ArrayList<>();

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position >= mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void appendData(List<T> data) {
        if (data != null && !data.isEmpty()) {
            mDataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mDataList;
    }

    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }
}
