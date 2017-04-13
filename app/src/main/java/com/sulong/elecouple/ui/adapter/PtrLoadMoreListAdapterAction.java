package com.sulong.elecouple.ui.adapter;

import java.util.List;

/**
 * Created by Liu Qing on 2015/10/13.
 */
public interface PtrLoadMoreListAdapterAction<T> {

    void appendData(List<T> data);

    void clearData();

    List<T> getData();

}
