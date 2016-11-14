package com.yunxingzh.wireless.mvp.ui.adapter;

import android.net.wifi.ScanResult;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;

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
        baseViewHolder.setText(R.id.wifi_name, info.SSID);
    }
}
