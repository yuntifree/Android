package com.yunxingzh.wireless.mvp.view;

public interface IBaseView {
    void showProgress();
    void hideProgress();
    void showError(int error);
    void showErrorMsg(String errorMsg);
}