package com.sulong.elecouple.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.SimpleListResult;
import com.sulong.elecouple.entity.SimpleResult1;
import com.sulong.elecouple.ui.adapter.FindTabListAdapter;
import com.sulong.elecouple.utils.AggAsyncHttpResponseHandler;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.JsonParser;
import com.sulong.elecouple.utils.Utility;
import com.sulong.elecouple.web.WebAPI;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by ydh on 2017/4/19.
 */

@SuppressLint("ValidFragment")
public class FindTabListFragment extends BasePtrLoadMoreListFragment {

    @Override
    protected BaseAdapter getListAdapter() {
        return new FindTabListAdapter();
    }

    @Override
    protected boolean enableLoadMoreFeature() {
        return false;
    }

    @Override
    protected boolean enableShowLoadingViewWhenRefreshList() {
        return false;
    }

    @Override
    protected void invokeListWebAPI() {
        ArrayList data = new ArrayList();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        mAdapterAction.appendData(data);
//        WebAPI.getFindTabList(new AggAsyncHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                super.onSuccess(statusCode, headers, responseBody);
//                // 打印json
//                String responseStr = Utility.getStrFromByte(responseBody);
//                Debug.i(getLogTag(), "json -> " + responseStr);
//
//                // 解析json
//                SimpleListResult result = JsonParser.getInstance().fromJson(responseBody, SimpleListResult.class);
//                boolean hasData = result != null && result.data != null && !result.data.isEmpty();
//
//                // 通知加载完成
//                onListDataRequestSuccess(statusCode, headers, responseStr, hasData);
//
//                // 处理解析后的数据
//                if (processSimpleResult(result, getActivity(), true, false)) {
//                    if (hasData) {
//                        mPage++;
//                        mAdapterAction.appendData(result.data);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                super.onFailure(statusCode, headers, responseBody, error);
//                String responseStr = Utility.getStrFromByte(responseBody);
//                onListDataRequestFailure(statusCode, headers, responseStr, error);
//            }
//        });
    }

    @Override
    protected boolean isInViewPager() {
        return true;
    }

    @Override
    protected SimpleResult1 getListJsonParserResult(String responseBody) {
        return null;
    }
}
