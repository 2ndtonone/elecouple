package com.sulong.elecouple.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.avatarqing.lib.loadmore.GridViewWithHeaderAndFooter;
import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.SimpleListResult;
import com.sulong.elecouple.entity.SimpleResult1;

import java.util.List;

/**
 * Created by Liu Qing on 2015/10/13.
 */
public abstract class BasePtrLoadMoreGridFragment extends BasePtrLoadMoreFragment {

    protected GridViewWithHeaderAndFooter mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_ptr_load_more_grid, container, false);
    }

    @Override
    protected void findViews() {
        super.findViews();
        mAbsListView = (AbsListView) getView().findViewById(R.id.grid_view);
        mGridView = (GridViewWithHeaderAndFooter) mAbsListView;
    }

    @Override
    protected boolean parseHasListData(SimpleResult1 result) {
        SimpleListResult parsedResult = (SimpleListResult) result;
        boolean hasData = parsedResult != null
                && parsedResult.data != null
                && !parsedResult.data.isEmpty();
        return hasData;
    }

    @Override
    protected List<?> parseListData(SimpleResult1 result) {
        SimpleListResult parsedResult = (SimpleListResult) result;
        return parsedResult.data;
    }
}
