package com.yunxingzh.wirelesslibs.wireless.lib.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yunxingzh.wirelesslibs.R;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AreaUtils;
import com.yunxingzh.wirelesslibs.widget.OnWheelChangedListener;
import com.yunxingzh.wirelesslibs.widget.WheelView;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AreaUtils;

import com.yunxingzh.wirelesslibs.widget.OnWheelChangedListener;
import com.yunxingzh.wirelesslibs.widget.WheelView;

/**
 * Created by carey on 2016/6/25 0025.
 */
public class AreaSelectDialogFragment extends DialogFragment implements View.OnClickListener, OnWheelChangedListener {

    private static final String ARG_PROVINCE = "arg_province";
    private static final String ARG_CITY = "arg_city";
    private static final String ARG_AREA = "arg_area";

    private WheelView mProvince;
    private WheelView mCity;
    private WheelView mArea;
    private Button mDetermineBtn;
    private Button mCancelBtn;

    private int mCurrentProvince;
    private int mCurrentCity;
    private int mCurrentArea;

    private AreaUtils mAreaUtils;

    private View mRootView;

    /**
     * 创建区域选择dialog
     *
     * @param province 省Id 0显示默认
     * @param city     市Id 0显示默认
     * @param area     区Id 0显示默认 -1不显示区域
     * @return
     */
    public static AreaSelectDialogFragment newInstance(int province, int city, int area) {
        AreaSelectDialogFragment fragment = new AreaSelectDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROVINCE, province == 0 ? 440000 : province);
        args.putInt(ARG_CITY, city == 0 ? 441900 : city);
        args.putInt(ARG_AREA, area == 0 ? 441901 : area);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_No_Border);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = View.inflate(getActivity(), R.layout.area_picker_dialog, null);
            mCurrentProvince = getArguments().getInt(ARG_PROVINCE);
            mCurrentCity = getArguments().getInt(ARG_CITY);
            mCurrentArea = getArguments().getInt(ARG_AREA);

            mProvince = (WheelView) mRootView.findViewById(R.id.id_province);
            mCity = (WheelView) mRootView.findViewById(R.id.id_city);
            mArea = (WheelView) mRootView.findViewById(R.id.id_area);
            if (mCurrentArea == -1) {
                mArea.setVisibility(View.GONE);
            }
            mDetermineBtn = (Button) mRootView.findViewById(R.id.determine_btn);
            mDetermineBtn.setOnClickListener(this);
            mCancelBtn = (Button) mRootView.findViewById(R.id.cancel_btn);
            mCancelBtn.setOnClickListener(this);

            mAreaUtils = AreaUtils.getInstance(getActivity());
            mProvince.setViewAdapter(new AreaWheelAdapter(getActivity(), mAreaUtils.getProvinceList()));
            mProvince.setVisibleItems(5);
            mProvince.setCurrentItem(mAreaUtils.getProvinceItem(mCurrentProvince));

            updateCities();
            mCity.setCurrentItem(mAreaUtils.getCityItem(mCurrentCity));
            mArea.setCurrentItem(mAreaUtils.getAreaItem(mCurrentArea));

            mProvince.addChangingListener(this);
            mCity.addChangingListener(this);
            mArea.addChangingListener(this);
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        AreaUtils.Data data = mAreaUtils.getCurrentProvince(mProvince.getCurrentItem());
        mCity.setViewAdapter(new AreaWheelAdapter(getActivity(), mAreaUtils.getCityListById(data.getId())));
        mCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        AreaUtils.Data data = mAreaUtils.getCurrentCity(mCity.getCurrentItem());
        mArea.setViewAdapter(new AreaWheelAdapter(getActivity(), mAreaUtils.getAreaListById(data.getId())));
        mArea.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        if (v == mDetermineBtn) {
            if (mListener != null) {
                AreaUtils.Data provinceData = mAreaUtils.getCurrentProvince(mProvince.getCurrentItem());
                AreaUtils.Data cityData = mAreaUtils.getCurrentCity(mCity.getCurrentItem());
                AreaUtils.Data areaData = mAreaUtils.getCurrentArea(mArea.getCurrentItem());

                mCurrentProvince = provinceData.getId();
                mCurrentCity = cityData.getId();
                mCurrentArea = areaData.getId();

                mListener.onAreaSelected(provinceData.getId(), cityData.getId(), areaData.getId(),
                        provinceData.getSname(), cityData.getSname(), areaData.getSname());
            }
            dismiss();
        } else if (v == mCancelBtn) {
            dismiss();
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {
            updateCities();
        } else if (wheel == mCity) {
            updateAreas();
        }
    }

    private OnAreaSelectListener mListener;

    public void setOnAreaSelectListener(OnAreaSelectListener listener) {
        mListener = listener;
    }

    public interface OnAreaSelectListener {
        void onAreaSelected(int pId, int cId, int aId, String pName, String cName, String aName);
    }
}
