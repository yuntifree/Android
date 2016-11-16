package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IServicePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.ServicePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.ui.utils.WordWrapView;
import com.yunxingzh.wireless.mvp.view.IServiceView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.ServiceVo;

import java.util.List;

/**
 * Created by stephon_ on 2016/11/1.
 * 服务
 */

@SuppressWarnings("ResourceType")
public class ServiceFragment extends BaseFragment implements IServiceView {

    private TextView mServiceTitle;
    private ImageView mServiceImg;
    private LinearLayout mServiceParentGroup, mServiceItem, mItemTop;
    private IServicePresenter iServicePresenter;
    private List<ServiceVo.DataVo.ServiceData> dataVoList;
    private List<ServiceVo.DataVo.ServiceData.ServiceChildData> childDatas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mServiceParentGroup = findView(view, R.id.service_parent_group);
    }

    public void initData() {
        iServicePresenter = new ServicePresenterImpl(this);
        iServicePresenter.getService();
    }

    @Override
    public void getServiceSuccess(ServiceVo serviceVo) {
        dataVoList = serviceVo.getData().getServices();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER | Gravity.LEFT;
        lp.setMargins(20, 20, 20, 20);

        LinearLayout.LayoutParams parentLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 10;

        for (int i = 0; i < dataVoList.size(); i++) {
            mServiceItem = new LinearLayout(getActivity());//item最外层layout
            mServiceItem.setBackgroundColor(getResources().getColor(R.color.white));
            mServiceItem.setOrientation(LinearLayout.VERTICAL);

            mItemTop = new LinearLayout(getActivity());//item顶部layout
            mItemTop.setOrientation(LinearLayout.HORIZONTAL);

            mServiceImg = new ImageView(getActivity());
            Glide.with(getActivity()).load(dataVoList.get(i).getIcon()).placeholder(R.drawable.ic_close).into(mServiceImg);

            mServiceTitle = new TextView(getActivity());
            mServiceTitle.setTextColor(getResources().getColor(R.color.gray_3c3c3c));
            mServiceTitle.setTextSize(14);
            mServiceTitle.setText(dataVoList.get(i).getTitle());

            mItemTop.addView(mServiceImg, lp);
            mItemTop.addView(mServiceTitle, lp);

            int size = dataVoList.get(i).getItems().size();
            childDatas = dataVoList.get(i).getItems();

            WordWrapView childLay = new WordWrapView(getActivity());
            for (int j = 0; j < size; j++) {
                final TextView views = new TextView(getActivity());
                views.setText(childDatas.get(j).getTitle());
                views.setTextSize(14);
                views.setTextColor(getResources().getColor(R.color.gray_5a5a5a));
                views.setTag(childDatas.get(j));
                childLay.addView(views,lp);
                views.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServiceVo.DataVo.ServiceData.ServiceChildData dv = (ServiceVo.DataVo.ServiceData.ServiceChildData) v.getTag();
                        Intent intent = new Intent(getActivity(),WebViewActivity.class);
                        intent.putExtra(Constants.URL,dv.getDst());
                        intent.putExtra(Constants.TITLE,dv.getTitle());
                        startActivity(intent);
                    }
                });
            }
            mServiceItem.addView(mItemTop);
            mServiceItem.addView(childLay);
            mServiceParentGroup.addView(mServiceItem,parentLp);
        }
    }
}