package com.yunxingzh.wireless.mvp.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IBaseView;
import com.yunxingzh.wirelesslibs.wireless.lib.view.dialog.LoadingDialogFragment;

/**
 * Created by Carey on 2016/5/25.
 */
public class BaseActivity extends FragmentActivity implements IBaseView {

    private InputMethodManager mInputMethodManager = null;

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    protected LoadingDialogFragment mLoadingDialog;

    protected int mLoadingCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /* @Override
    protected void onStart() {
        super.onStart();
        if (MyApplication.sApplication.isExit()) {
            finish();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void showProgress() {
        mLoadingDialog = (LoadingDialogFragment) getSupportFragmentManager().findFragmentByTag("loading_dialog");
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialogFragment.newInstance("");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(mLoadingDialog, "loading_dialog");
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.show(mLoadingDialog);
        }
        mLoadingCount++;
    }

    @Override
    public void hideProgress() {
        mLoadingCount--;
        if (mLoadingDialog != null && mLoadingCount <= 0) {
            mLoadingDialog.dismiss();
            mLoadingCount = 0;
        }
    }

    @Override
    public void showError(int error) {
        ToastUtil.showError(this, error);
    }

}
