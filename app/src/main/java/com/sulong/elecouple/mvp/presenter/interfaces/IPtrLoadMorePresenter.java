package com.sulong.elecouple.mvp.presenter.interfaces;

/**
 * Created by ydh on 2016/7/27.
 */
public interface IPtrLoadMorePresenter {
    void loadListData(boolean refresh);

    boolean isLoadingFirstPage();

    boolean hasLoadedData();
}