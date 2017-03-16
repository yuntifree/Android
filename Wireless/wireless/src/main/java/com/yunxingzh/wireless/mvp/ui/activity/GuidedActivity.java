package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by asus_ on 2016/11/26.
 * 引导界面
 */

public class GuidedActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private TextView enter;
    private List<View> mImageViews;

    private int[] imgIdArray;//图片资源id

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guided);
        initView();
        initData();
    }

    public void initView() {
        viewPager = findView(R.id.viewpager);
        enter = findView(R.id.enter);
        enter.setOnClickListener(this);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.transparent));
        //载入图片资源ID
        imgIdArray = new int[]{R.drawable.loading1, R.drawable.loading2, R.drawable.loading3};
        //将图片装载到数组中
        mImageViews = new ArrayList<>();
        for (int i = 0; i < imgIdArray.length; i++) {
            ImageView imageView = new ImageView(this);
            mImageViews.add(imageView);
            imageView.setBackgroundResource(imgIdArray[i]);
        }
        viewPager.setAdapter(new GuidePageAdapter(mImageViews));
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == enter) {
            startActivity(new Intent(GuidedActivity.this, RegisterActivity.class));
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                enter.setVisibility(View.GONE);
                break;
            case 1:
                enter.setVisibility(View.GONE);
                break;
            case 2:
                enter.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class GuidePageAdapter extends PagerAdapter {
        private List<View> viewList;

        public GuidePageAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imgIdArray != null && imgIdArray.length > 0) {
            imgIdArray = null;
        }
    }
}

