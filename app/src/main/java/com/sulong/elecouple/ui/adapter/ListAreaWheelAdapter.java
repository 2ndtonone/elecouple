package com.sulong.elecouple.ui.adapter;

import android.content.Context;


import com.sulong.elecouple.entity.AreaItem;

import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class ListAreaWheelAdapter extends AbstractWheelTextAdapter {
    public List<? extends AreaItem> items;

    public ListAreaWheelAdapter(Context context) {
        super(context);
    }

    public ListAreaWheelAdapter(Context context, List<? extends AreaItem> listItem) {
        super(context);
        this.items = listItem;
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    @Override
    public boolean isNeedLoop() {
        return false;
    }

    @Override
    public int getInitLoopBase() {
        return 0;
    }

    @Override
    public int getBASE_BUNDLE() {
        return 0;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            AreaItem item = items.get(index);
            return item.area_name;
        }
        return null;
    }

}
