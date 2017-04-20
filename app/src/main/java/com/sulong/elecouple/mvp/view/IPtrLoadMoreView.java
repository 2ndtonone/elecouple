package com.sulong.elecouple.mvp.view;

import java.util.List;

/**
 * Created by ydh on 2016/7/27.
 */
public interface IPtrLoadMoreView extends IBaseViewWithWebRequest, IBaseViewWithLoading {

    void refreshComplete();

    void loadMoreFinish(boolean hasMore);

    void loadMoreError(int errorCode, String errorMessage);

    void clearData();

    void appendData(List<?> data);

    void showIsLoadingDataNow();

    boolean isPullToRefreshing();

    void hideEmptyView();

    void showEmptyView(boolean noNetwork);

}