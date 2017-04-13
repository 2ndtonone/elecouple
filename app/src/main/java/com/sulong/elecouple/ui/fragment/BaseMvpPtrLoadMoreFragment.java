package com.sulong.elecouple.ui.fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.sulong.elecouple.R;
import com.sulong.elecouple.mvp.presenter.interfaces.IPtrLoadMorePresenter;
import com.sulong.elecouple.mvp.view.IPtrLoadMoreView;
import com.sulong.elecouple.ui.views.AggLoaderMoreFooterView;
import com.sulong.elecouple.ui.views.AggPtrHeaderView;
import com.sulong.elecouple.utils.Debug;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public abstract class BaseMvpPtrLoadMoreFragment extends BaseFragment
        implements PtrHandler, LoadMoreHandler, IPtrLoadMoreView {

    protected PtrClassicFrameLayout mPtrFrameLayout;
    protected AbsListView mAbsListView;
    protected ListView mListView;
    protected GridViewWithHeaderAndFooter mGridView;
    protected LoadMoreContainerBase mLoadMoreContainer;
    protected View mEmptyView;

    protected BaseAdapter mListAdapter;
    protected PtrLoadMoreListAdapterAction mAdapterAction;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initMvp();
    }

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

    /**
     * 满足以下其中一个条件可以在onActivityCreated时加载列表数据：
     * <ul>
     * <li>本Fragment不在ViewPager中/li>
     * <li>本Fragment在ViewPager中、没有加载过数据、对用户可见</li>
     * </ul>
     */
    protected void loadDataOnActivityCreated() {
        if (!isInViewPager()
                || (!getPresenter().hasLoadedData() && getUserVisibleHint())) {
            Debug.i(getLogTag(), "loadDataOnActivityCreated");
            loadListData(true);
        }
    }

    /**
     * 满足以下所有条件可以在onResume时加载列表数据：
     * <ul>
     * <li>已标记需要每次页面对用户可见时加载列表数据</li>
     * <li>本Fragment不在ViewPager中，或在ViewPager中并且对用户可见</li>
     * </ul>
     */
    protected void loadDataOnResume() {
        if (needRefreshDataWhenVisibleToUserEveryTime() &&
                (!isInViewPager() || getUserVisibleHint())) {
            Debug.i(getLogTag(), "refresh list data when onResume");
            loadListData(true);
        }
    }

    protected void loadDataWhenVisibleToUser() {
        boolean isViewCreated = (isActivityCreated() || getView() != null);
        if (isInViewPager() && isViewCreated) {
            if (needRefreshDataWhenVisibleToUserEveryTime()) {
                Debug.i(getLogTag(), "refresh data when visible to user every time");
                loadListData(true);
            } else if (!getPresenter().hasLoadedData()) {
                Debug.i(getLogTag(), "loadDataWhenVisibleToUser");
                loadListData(true);
            }
        }
    }

    public void loadListData(boolean refresh) {
        getPresenter().loadListData(refresh);
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
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(this);
        AggPtrHeaderView refreshHeaderView = new AggPtrHeaderView(getActivity());
        mPtrFrameLayout.setHeaderView(refreshHeaderView);
        mPtrFrameLayout.addPtrUIHandler(refreshHeaderView);
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

    protected abstract void initMvp();

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

    protected boolean useGridView() {
        return false;
    }

    protected abstract IPtrLoadMorePresenter getPresenter();

    protected void setupEmptyView() {
        View loadingEmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.general_empty_view, mAbsListView, false);
        mEmptyView = loadingEmptyView.findViewById(R.id.empty_view);

        mLoadMoreContainer.addView(loadingEmptyView);
        mAbsListView.setEmptyView(loadingEmptyView);
        mEmptyView.setVisibility(View.INVISIBLE);
    }

    protected String getEmptyText() {
        return getStringById(R.string.no_data);
    }

    // >>>>>>>>>>>>>>> PtrHandler
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mAbsListView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        loadListData(true);
    }
    // <<<<<<<<<<<<<<<<< PtrHandler

    // >>>>>>>>>>>>>>> LoadMoreHandler
    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        loadListData(false);
    }
    // <<<<<<<<<<<<<<<<< LoadMoreHandler

    // >>>>>>>>>>>>>>> View interface

    @Override
    public void showNoNetworkError() {
        super.showNoNetworkError();
        if (!getPresenter().isLoadingFirstPage()) {
            loadMoreError(0, getStringById(R.string.no_network));
        }
    }

    @Override
    public void showWrongResponse(int code, String message) {
        super.showWrongResponse(code, message);
        if (!getPresenter().isLoadingFirstPage()) {
            loadMoreError(code, message);
        }
    }

    @Override
    public void refreshComplete() {
        if (mPtrFrameLayout.isRefreshing()) {
            mPtrFrameLayout.refreshComplete();
        }
    }

    @Override
    public void loadMoreFinish(boolean hasMore) {
        mLoadMoreContainer.loadMoreFinish(mListAdapter.isEmpty(), hasMore);
    }

    @Override
    public void loadMoreError(int errorCode, String errorMessage) {
        mLoadMoreContainer.loadMoreError(errorCode, errorMessage);
    }

    @Override
    public void clearData() {
        mAdapterAction.clearData();
    }

    @Override
    public void appendData(List data) {
        mAdapterAction.appendData(data);
    }

    @Override
    public void showIsLoadingDataNow() {
        Debug.li(getLogTag(), "is loading data now");
    }

    @Override
    public boolean isPullToRefreshing() {
        return mPtrFrameLayout.isRefreshing();
    }

    @Override
    public void showEmptyView(boolean noNetwork) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            TextView emptyTv = (TextView) mEmptyView.findViewById(R.id.empty_text);
            if (emptyTv != null) {
                emptyTv.setText(noNetwork ? getStringById(R.string.no_network) : getEmptyText());
            }
        }
    }

    @Override
    public void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }

    // <<<<<<<<<<<<<<<<< View interface
}