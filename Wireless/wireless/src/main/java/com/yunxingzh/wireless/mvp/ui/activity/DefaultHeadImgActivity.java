package com.yunxingzh.wireless.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.IDefHeadPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.DefHeadPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IDefHeadView;

import java.util.List;

import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.bean.vo.DefHeadFemaleVo;
import wireless.libs.bean.vo.DefHeadMaleVo;

/**
 * Created by stephen on 2017/2/25.
 * 默认头像
 */

public class DefaultHeadImgActivity extends BaseActivity implements IDefHeadView, View.OnClickListener {

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv;
    private IDefHeadPresenter iDefHeadPresenter;
    private LinearLayout mHeadMaleParentLay, mHeadFemaleParentLay;
    private WindowManager wm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_head_img);
        initView();
        initData();
    }

    public void initView() {
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText("选择头像");
        mHeadMaleParentLay = findView(R.id.head_male_parent_lay);
        mHeadFemaleParentLay = findView(R.id.head_female_parent_lay);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        iDefHeadPresenter = new DefHeadPresenterImpl(this);
        iDefHeadPresenter.getDefHead();
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        }
    }

    @Override
    public void getDefHeadSuccess(DefHeadList defHeadList) {
        if (defHeadList != null) {
            List<DefHeadMaleVo> maleVos = defHeadList.male;
            List<DefHeadFemaleVo> femaleVos = defHeadList.female;

            wm = getWindowManager();
            int width = wm.getDefaultDisplay().getWidth();//720,1536
            int height = wm.getDefaultDisplay().getHeight();//1280,2560

            for (int i = 0; i < maleVos.size(); i++) {
                LinearLayout maleItems = new LinearLayout(this);
                maleItems.setOrientation(LinearLayout.VERTICAL);

                ImageView headImg = new ImageView(this);
                Glide.with(this).load(maleVos.get(i).headurl).into(headImg);

                TextView name = new TextView(this);
                name.setTextColor(getResources().getColor(R.color.gray_aaaaaa));
                name.setTextSize(12);
                name.setText(maleVos.get(i).desc);

                TextView age = new TextView(this);
                age.setTextColor(getResources().getColor(R.color.gray_aaaaaa));
                age.setTextSize(10);
                age.setText(maleVos.get(i).age);

                if (width <= 720 && height <= 1280) {
                    maleItems.addView(headImg, getLayoutParams(0, Gravity.CENTER, 130, 130, 0, 0, 0, 0));
                } else {
                    maleItems.addView(headImg, getLayoutParams(0, Gravity.CENTER, 250, 250, 0, 0, 0, 0));
                }
                maleItems.addView(name, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 15, 0, 0));
                maleItems.addView(age, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));

                maleItems.setTag(maleVos.get(i));
                maleItems.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DefHeadMaleVo maleVo = (DefHeadMaleVo) v.getTag();
                        Intent mIntent = new Intent();
                        mIntent.putExtra("headUrl", maleVo.headurl);
                        DefaultHeadImgActivity.this.setResult(Activity.RESULT_OK, mIntent);
                        finish();
                    }
                });
                mHeadMaleParentLay.addView(maleItems, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
            }

            for (int i = 0; i < femaleVos.size(); i++) {
                LinearLayout femaleItems = new LinearLayout(this);
                femaleItems.setOrientation(LinearLayout.VERTICAL);

                ImageView headImg = new ImageView(this);
                Glide.with(this).load(femaleVos.get(i).headurl).into(headImg);

                TextView name = new TextView(this);
                name.setTextColor(getResources().getColor(R.color.gray_aaaaaa));
                name.setTextSize(12);
                name.setText(femaleVos.get(i).desc);

                TextView age = new TextView(this);
                age.setTextColor(getResources().getColor(R.color.gray_aaaaaa));
                age.setTextSize(10);
                age.setText(femaleVos.get(i).age);

                femaleItems.addView(headImg, getLayoutParams(0, Gravity.CENTER, 130, 130, 0, 0, 0, 0));
                femaleItems.addView(name, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 15, 0, 0));
                femaleItems.addView(age, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));

                femaleItems.setTag(femaleVos.get(i));
                femaleItems.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DefHeadFemaleVo femaleVo = (DefHeadFemaleVo) v.getTag();
                        Intent mIntent = new Intent();
                        mIntent.putExtra("headUrl", femaleVo.headurl);
                        DefaultHeadImgActivity.this.setResult(Activity.RESULT_OK, mIntent);
                        finish();
                    }
                });
                mHeadFemaleParentLay.addView(femaleItems, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iDefHeadPresenter != null) {
            iDefHeadPresenter.onDestroy();
        }
    }

    public LinearLayout.LayoutParams getLayoutParams(int weight, int isGravity, int width, int height, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.gravity = isGravity;
        lp.weight = weight;
        lp.setMargins(left, top, right, bottom);
        return lp;
    }
}
