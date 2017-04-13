package com.sulong.elecouple.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sulong.elecouple.ui.adapter.PtrLoadMoreListAdapterAction;
import com.avatarqing.lib.loadmore.GridViewWithHeaderAndFooter;
import com.avatarqing.lib.loadmore.LoadMoreContainer;
import com.avatarqing.lib.loadmore.LoadMoreContainerBase;
import com.avatarqing.lib.loadmore.LoadMoreHandler;
import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.SimpleResult1;
import com.sulong.elecouple.ui.views.AggLoaderMoreFooterView;
import com.sulong.elecouple.ui.views.AggPtrHeaderView;
import com.sulong.elecouple.utils.AggAsyncHttpResponseHandler;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.Utility;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public abstract class BasePtrLoadMoreFragment extends BaseFragment
        implements PtrHandler, LoadMoreHandler {

    protected PtrClassicFrameLayout mPtrFrameLayout;
    protected AbsListView mAbsListView;
    protected ListView mListView;
    protected GridViewWithHeaderAndFooter mGridView;
    protected LoadMoreContainerBase mLoadMoreContainer;
    protected View mEmptyView;

    protected BaseAdapter mListAdapter;
    protected PtrLoadMoreListAdapterAction mAdapterAction;
    protected int mPage = 1;
    protected boolean mIsLoadingList = false;
    protected boolean mIsRefreshListOfLatestLoading = true;
    protected boolean mHasLoadedData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView;
        if (useGridView()) {
            fragmentView = inflater.inflate(R.layout.fragment_base_ptr_load_more_grid, container, false);
        } else {
            fragmentView = inflater.inflate(R.layout.fragment_base_ptr_load_more_list, container, false);
        }
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        initViews();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDataOnActivityCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataOnResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadDataWhenVisibleToUser();
        }
    }

    @Override
    protected boolean enableLoadingView() {
        return true;
    }

    protected void loadDataOnActivityCreated() {
        if (!isInViewPager()
                || (isInViewPager() && !mHasLoadedData && getUserVisibleHint())) {
            Debug.i(getLogTag(), "loadDataOnActivityCreated");
            loadListData(true);
        }
    }

    protected void loadDataOnResume() {
        if (needRefreshDataWhenVisibleToUserEveryTime() &&
                (!isInViewPager()
                        || isInViewPager() && getUserVisibleHint())) {
            Debug.i(getLogTag(), "refresh list data when onResume");
            loadListData(true);
        }
    }

    protected void loadDataWhenVisibleToUser() {
        boolean isViewCreated = (isResumed() || getView() != null);
        if (isInViewPager() && isViewCreated) {
            if (needRefreshDataWhenVisibleToUserEveryTime()) {
                Debug.i(getLogTag(), "refresh data when visible to user every time");
                loadListData(true);
            } else if (!mHasLoadedData) {
                Debug.i(getLogTag(), "loadDataWhenVisibleToUser");
                loadListData(true);
            }
        }
    }

    protected void findViews() {
        View view = getView();
        mPtrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        mLoadMoreContainer = (LoadMoreContainerBase) view.findViewById(R.id.load_more_container);
        if (useGridView()) {
            mAbsListView = mGridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.grid_view);
        } else {
            mAbsListView = mListView = (ListView) view.findViewById(R.id.list_view);
        }
    }

    protected void initViews() {
        setupEmptyView();
        initPullToRefresh();
        initLoadMore();
        initListAdapter();
    }

    protected void initPullToRefresh() {
        // 下拉刷新
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(this);
        AggPtrHeaderView refreshHeaderView = new AggPtrHeaderView(getActivity());
        mPtrFrameLayout.setHeaderView(refreshHeaderView);
        mPtrFrameLayout.addPtrUIHandler(refreshHeaderView);
        mPtrFrameLayout.disableWhenHorizontalMove(true);
    }

    protected void initLoadMore() {
        if (!enableLoadMoreFeature()) {
            return;
        }
        // 加载更多
        AggLoaderMoreFooterView loadMoreView = new AggLoaderMoreFooterView(getActivity());
        mLoadMoreContainer.setLoadMoreView(loadMoreView);
        mLoadMoreContainer.setLoadMoreUIHandler(loadMoreView);
        mLoadMoreContainer.setLoadMoreHandler(this);
        mLoadMoreContainer.setShowLoadingForFirstPage(false);
    }

    protected void initListAdapter() {
        mListAdapter = getListAdapter();
        mAbsListView.setAdapter(mListAdapter);
        mAdapterAction = (PtrLoadMoreListAdapterAction) mListAdapter;
    }

    protected abstract BaseAdapter getListAdapter();

    protected boolean isInViewPager() {
        return false;
    }

    protected boolean needRefreshDataWhenVisibleToUserEveryTime() {
        return false;
    }

    protected boolean enableLoadMoreFeature() {
        return true;
    }

    protected boolean enableShowLoadingViewWhenRefreshList() {
        return true;
    }

    protected boolean useGridView() {
        return false;
    }

    protected void setupEmptyView() {
        View loadingEmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.general_empty_view, mAbsListView, false);
        mEmptyView = loadingEmptyView.findViewById(R.id.empty_view);

        mLoadMoreContainer.addView(loadingEmptyView);
        mAbsListView.setEmptyView(loadingEmptyView);
        mEmptyView.setVisibility(View.INVISIBLE);
    }

    protected void loadListData(boolean refresh) {
        if (mIsLoadingList) {
            Debug.i(getLogTag(), "is loading data now");
            return;
        }
        if (refresh) {
            mPage = 1;
            if (!mPtrFrameLayout.isRefreshing()) {
                if (enableShowLoadingViewWhenRefreshList()) {
                    showLoadingView();
                }
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.INVISIBLE);
                }
            }
        }
        mIsLoadingList = true;
        mHasLoadedData = true;
        mIsRefreshListOfLatestLoading = refresh;
        invokeListWebAPI();
    }

    protected abstract void invokeListWebAPI();

    protected AggAsyncHttpResponseHandler getListResponseHandler() {
        return new AggAsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                if (isDetached()) {
                    Debug.li(getLogTag(), BasePtrLoadMoreFragment.this.getClass().getSimpleName() + " has detached to activity, do nothing");
                    return;
                }

                // 打印json
                String responseStr = Utility.getStrFromByte(responseBody);

                // 解析json
                SimpleResult1 result = getListJsonParserResult(responseStr);
                boolean hasData = parseHasListData(result);

                // 通知加载完成
                onListDataRequestSuccess(statusCode, headers, responseStr, hasData);

                // 处理解析后的数据
                if (processSimpleResult(result, getActivity(), false, false)) {
                    if (hasData) {
                        mPage++;
                        mAdapterAction.appendData(parseListData(result));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                if (isDetached()) {
                    Debug.li(getLogTag(), BasePtrLoadMoreFragment.this.getClass().getSimpleName() + " has detached to activity, do nothing");
                    return;
                }
                String responseStr = Utility.getStrFromByte(responseBody);
                onListDataRequestFailure(statusCode, headers, responseStr, error);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                Debug.i(getLogTag(), "cancel request " + getRequestURI());
            }

            @Override
            protected boolean toastWhenNoNetworkErrorOccurred() {
                return false;
            }

            public String getLogTag() {
                return BasePtrLoadMoreFragment.this.getLogTag();
            }
        };
    }

    protected abstract SimpleResult1 getListJsonParserResult(String responseBody);

    protected abstract boolean parseHasListData(SimpleResult1 result);

    protected abstract List<?> parseListData(SimpleResult1 result);

    protected void onListDataRequestSuccess(boolean hasData) {
        this.onListDataRequestSuccess(-1, null, null, hasData);
    }

    protected void onListDataRequestSuccess(int statusCode, Header[] headers, String responseBody, boolean hasData) {
        // 加载标志位重置
        mIsLoadingList = false;

        // 显示空提示
        if (enableShowLoadingViewWhenRefreshList()) {
            hideLoadingView();
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            TextView emptyTv = (TextView) mEmptyView.findViewById(R.id.empty_text);
            if (emptyTv != null) {
                emptyTv.setText(getEmptyText());
            }
        }

        // 如果是第一页，清除原有数据
        if (mPage == 1) {
            mAdapterAction.clearData();
        }

        // 完成下拉刷新
        if (mPtrFrameLayout.isRefreshing()) {
            mPtrFrameLayout.refreshComplete();
        }

        // 完成加载更多
        mLoadMoreContainer.loadMoreFinish(mListAdapter.isEmpty(), hasData);
    }

    protected void onListDataRequestFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
        // 加载标志位重置
        mIsLoadingList = false;

        // 显示空提示
        if (enableShowLoadingViewWhenRefreshList()) {
            hideLoadingView();
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            TextView emptyTv = (TextView) mEmptyView.findViewById(R.id.empty_text);
            if (emptyTv != null) {
                if (error instanceof SocketTimeoutException
                        || error instanceof ConnectTimeoutException
                        || TextUtils.isEmpty(responseBody)) {
                    emptyTv.setText(R.string.no_network);
                }
            }
        }

        // 完成下拉刷新
        if (mPtrFrameLayout.isRefreshing()) {
            mPtrFrameLayout.refreshComplete();
        }

        // 完成加载更多
        String msg = "";
        if (responseBody != null) {
            msg = responseBody;
        } else if (error != null) {
            error.printStackTrace();
            Debug.le(getLogTag(), error.getMessage());
        }
        if (mIsRefreshListOfLatestLoading) {
            mLoadMoreContainer.loadMoreFinish(mListAdapter.isEmpty(), true);
        } else {
            mLoadMoreContainer.loadMoreError(statusCode, msg);
        }
    }

    protected void onListDataRequestFailure(String responseBody) {
        // 加载标志位重置
        mIsLoadingList = false;

        // 显示空提示
        if (enableShowLoadingViewWhenRefreshList()) {
            hideLoadingView();
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            TextView emptyTv = (TextView) mEmptyView.findViewById(R.id.empty_text);
            if (emptyTv != null) {
                if (TextUtils.isEmpty(responseBody)) {
                    emptyTv.setText(R.string.no_network);
                }
            }
        }

        // 完成下拉刷新
        if (mPtrFrameLayout.isRefreshing()) {
            mPtrFrameLayout.refreshComplete();
        }

        // 完成加载更多
        String msg = "";
        if (responseBody != null) {
            msg = responseBody;
        }
        if (mIsRefreshListOfLatestLoading) {
            mLoadMoreContainer.loadMoreFinish(mListAdapter.isEmpty(), true);
        } else {
            mLoadMoreContainer.loadMoreError(-1, msg);
        }
    }

    protected String getEmptyText() {
        return LocationApplication.getInstance().getString(R.string.no_data);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mAbsListView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        loadListData(true);
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        loadListData(false);
    }
}
