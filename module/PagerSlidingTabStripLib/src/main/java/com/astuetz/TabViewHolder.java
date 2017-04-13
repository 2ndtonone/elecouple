package com.astuetz;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by wujian on 2016/11/8.
 */

public class TabViewHolder {

    public static <T extends View> T get(View view, int id) {
        @SuppressWarnings("unchecked")
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
