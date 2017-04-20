package com.sulong.elecouple.ui.fragment;

import android.widget.BaseAdapter;

import com.sulong.elecouple.entity.SimpleListResult;
import com.sulong.elecouple.entity.SimpleResult1;
import com.sulong.elecouple.ui.adapter.SearchAdapter;
import com.sulong.elecouple.utils.AggAsyncHttpResponseHandler;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.JsonParser;
import com.sulong.elecouple.utils.Utility;
import com.sulong.elecouple.web.WebAPI;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ydh on 2017/4/17.
 */

public class SearchFragment  extends BasePtrLoadMoreListFragment{

    @Override
    protected BaseAdapter getListAdapter() {
        return new SearchAdapter();
    }

    @Override
    protected void invokeListWebAPI() {
        WebAPI.getSearchList(new AggAsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                // 打印json
                String responseStr = Utility.getStrFromByte(responseBody);
                Debug.i(getLogTag(), "json -> " + responseStr);

                // 解析json
                SimpleListResult result = JsonParser.getInstance().fromJson(responseBody, SimpleListResult.class);
                boolean hasData = result != null && result.data != null && !result.data.isEmpty();

                // 通知加载完成
                onListDataRequestSuccess(statusCode, headers, responseStr, hasData);

                // 处理解析后的数据
                if (processSimpleResult(result, getActivity(), true, false)) {
                    if (hasData) {
                        mPage++;
                        mAdapterAction.appendData(result.data);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                String responseStr = Utility.getStrFromByte(responseBody);
                onListDataRequestFailure(statusCode, headers, responseStr, error);
            }
        });
    }

    @Override
    protected SimpleResult1 getListJsonParserResult(String responseBody) {
        return null;
    }

    @Override
    protected boolean isInViewPager() {
        return true;
    }

    public void refreshDate() {
        loadListData(true);
    }
}
