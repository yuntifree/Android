package com.yunxingzh.wireless.mvp.ui.adapter;

import android.net.wifi.ScanResult;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import java.util.List;

/**
 * Created by stephon on 2016/11/12.
 */

public class WifiManagerAdapter extends BaseQuickAdapter<ScanResult> {

    public WifiManagerAdapter(List<ScanResult> data) {
        super(R.layout.list_item_wifi_manager, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ScanResult info) {
        if (!StringUtils.isEmpty(info.SSID)) {
            baseViewHolder.setText(R.id.wifi_name, info.SSID);
        }
        if (info.level <= 0 && info.level >= -50) {
            Glide.with(mContext).load(R.drawable.signal_password3).placeholder(R.drawable.signal_password0).into((ImageView) baseViewHolder.getView(R.id.wifi_signal));
        } else if (info.level < -50 && info.level >= -70) {
            Glide.with(mContext).load(R.drawable.signal_password2).placeholder(R.drawable.signal_password0).into((ImageView) baseViewHolder.getView(R.id.wifi_signal));
        } else {
            Glide.with(mContext).load(R.drawable.signal_password1).placeholder(R.drawable.signal_password0).into((ImageView) baseViewHolder.getView(R.id.wifi_signal));
        }
    }
}
