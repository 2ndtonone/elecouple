package com.sulong.elecouple.mvp.presenter.interfaces;

/**
 * Created by lq on 2016/7/27.
 */
public interface IPtrLoadMorePresenter {
    void loadListData(boolean refresh);

    boolean isLoadingFirstPage();

    boolean hasLoadedData();
}