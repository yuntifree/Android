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
import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.IDefHeadPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.DefHeadPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IDefHeadView;
import com.yunxingzh.wireless.utils.AppUtils;

import java.util.List;

import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.bean.resp.NickNameList;
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
            MobclickAgent.onEvent(this, "sys_profile_photo_cancel");
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

            int size = maleVos.size();
            int num = ((size % 3) > 0) ? 1 : 0;
            int lines = size / 3 + num;//得到行数

            for (int i = 0; i < lines; i++) {//遍历行数
                LinearLayout lineLay = new LinearLayout(this);//每一行的容器
                for (int j = 0; j < 3; j++) {
                    LinearLayout maleItems = new LinearLayout(this);
                    maleItems.setOrientation(LinearLayout.VERTICAL);
                    int positon = i * 3 + j;//得到item当前position
                    if (positon >= size) {//一行不足3个时填充空view
                        TextView nullView = new TextView(this);
                        maleItems.addView(nullView, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 15, 0, 0));
                        lineLay.addView(maleItems, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 15, 0, 0));
                    } else {
                        ImageView headImg = new ImageView(this);
                        Glide.with(this).load(maleVos.get(positon).headurl).into(headImg);

                        TextView name = new TextView(this);
                        name.setTextColor(getResources().getColor(R.color.gray_aaaaaa));
                        name.setTextSize(12);
                        name.setText(maleVos.get(positon).desc);

                        if (width <= 720 && height <= 1280) {
                            maleItems.addView(headImg, getLayoutParams(0, Gravity.CENTER, 130, 130, 0, 0, 0, 0));
                        } else {
                            maleItems.addView(headImg, getLayoutParams(0, Gravity.CENTER, 220, 220, 0, 0, 0, 0));
                        }
                        maleItems.addView(name, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 15, 0, 0));

                        maleItems.setTag(maleVos.get(positon));
                        maleItems.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppUtils.animation(v);
                                DefHeadMaleVo maleVo = (DefHeadMaleVo) v.getTag();
                                //友盟上报
                                switch (maleVo.desc) {
                                    case "小正太":
                                        MobclickAgent.onEvent(DefaultHeadImgActivity.this, "sys_profile_photo_select_1");
                                        break;
                                    case "假小子":
                                        MobclickAgent.onEvent(DefaultHeadImgActivity.this, "sys_profile_photo_select_2");
                                        break;
                                    case "型男范":
                                        MobclickAgent.onEvent(DefaultHeadImgActivity.this, "sys_profile_photo_select_3");
                                        break;
                                }
                                Intent mIntent = new Intent();
                                mIntent.putExtra("headUrl", maleVo.headurl);
                                DefaultHeadImgActivity.this.setResult(Activity.RESULT_OK, mIntent);
                                finish();
                            }
                        });
                        if (i == 0) {//如果是第一行就无需添加顶部高度
                            lineLay.addView(maleItems, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
                        } else {
                            lineLay.addView(maleItems, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 20, 0, 0));
                        }
                    }
                }
                mHeadMaleParentLay.addView(lineLay, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
            }

            int femaleSize = femaleVos.size();
            int femaleNum = ((femaleSize % 3) > 0) ? 1 : 0;
            int femaleLines = femaleSize / 3 + femaleNum;//得到行数

            for (int i = 0; i < femaleLines; i++) {//遍历行数
                LinearLayout lineLay = new LinearLayout(this);//每一行的容器
                for (int j = 0; j < 3; j++) {
                    LinearLayout femaleItems = new LinearLayout(this);
                    femaleItems.setOrientation(LinearLayout.VERTICAL);
                    int positon = i * 3 + j;//得到item当前position
                    if (positon >= size) {//一行不足3个时填充空view
                        TextView nullView = new TextView(this);
                        femaleItems.addView(nullView, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 15, 0, 0));
                        lineLay.addView(femaleItems, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 15, 0, 0));
                    } else {
                        ImageView headImg = new ImageView(this);
                        Glide.with(this).load(femaleVos.get(positon).headurl).into(headImg);

                        TextView name = new TextView(this);
                        name.setTextColor(getResources().getColor(R.color.gray_aaaaaa));
                        name.setTextSize(12);
                        name.setText(femaleVos.get(positon).desc);

                        if (width <= 720 && height <= 1280) {
                            femaleItems.addView(headImg, getLayoutParams(0, Gravity.CENTER, 130, 130, 0, 0, 0, 0));
                        } else {
                            femaleItems.addView(headImg, getLayoutParams(0, Gravity.CENTER, 220, 220, 0, 0, 0, 0));
                        }
                        femaleItems.addView(name, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 15, 0, 0));

                        femaleItems.setTag(femaleVos.get(positon));
                        femaleItems.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppUtils.animation(v);
                                DefHeadFemaleVo femaleVo = (DefHeadFemaleVo) v.getTag();
                                //友盟上报
                                switch (femaleVo.desc) {
                                    case "小萝莉":
                                        MobclickAgent.onEvent(DefaultHeadImgActivity.this, "sys_profile_photo_select_4");
                                        break;
                                    case "女汉子":
                                        MobclickAgent.onEvent(DefaultHeadImgActivity.this, "sys_profile_photo_select_5");
                                        break;
                                    case "御姐范":
                                        MobclickAgent.onEvent(DefaultHeadImgActivity.this, "sys_profile_photo_select_6");
                                        break;
                                }

                                Intent mIntent = new Intent();
                                mIntent.putExtra("headUrl", femaleVo.headurl);
                                DefaultHeadImgActivity.this.setResult(Activity.RESULT_OK, mIntent);
                                finish();
                            }
                        });

                        if (i == 0) {//如果是第一行就无需添加顶部高度
                            lineLay.addView(femaleItems, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
                        } else {
                            lineLay.addView(femaleItems, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 20, 0, 0));
                        }
                    }
                }
                mHeadFemaleParentLay.addView(lineLay, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
            }
        }
    }

    @Override
    public void getRandNickSuccess(NickNameList nickNameList) {
    }

    @Override
    public void updateUserInfoSuccess() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iDefHeadPresenter != null) {
            iDefHeadPresenter.onDestroy();
        }
        if (mHeadFemaleParentLay != null) {
            mHeadFemaleParentLay.removeAllViews();
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
