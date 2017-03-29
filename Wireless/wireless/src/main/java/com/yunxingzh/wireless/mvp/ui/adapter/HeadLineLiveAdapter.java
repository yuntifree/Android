package com.yunxingzh.wireless.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

import wireless.libs.bean.vo.LiveVo;

/**
 * Created by stephen on 2017/2/8.
 */

public class HeadLineLiveAdapter extends BaseQuickAdapter<LiveVo,BaseViewHolder> {

    private onLiveItemClickListener onLiveItemClickListener;

    public HeadLineLiveAdapter(List<LiveVo> data) {
        super(R.layout.list_item_live, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final LiveVo liveVo) {
        Glide.with(mContext).load(liveVo.img).placeholder(R.drawable.img_default).into((ImageView) baseViewHolder.getView(R.id.live_bg_iv));

        if (StringUtils.isEmpty(liveVo.nickname)) {
            baseViewHolder.setText(R.id.live_nick_name_tv, "主播");
        } else {
            baseViewHolder.setText(R.id.live_nick_name_tv, liveVo.nickname + "");
        }
        Glide.with(mContext).load(liveVo.avatar).into((ImageView) baseViewHolder.getView(R.id.live_img_small_iv));
        if (liveVo.watches > 10000) {
            baseViewHolder.setText(R.id.live_watchs_tv, new DecimalFormat("0").format(liveVo.watches / 10000) + "万");
        } else {
            baseViewHolder.setText(R.id.live_watchs_tv, liveVo.watches + "");
        }
        baseViewHolder.setText(R.id.live_location_tv, liveVo.location + "");
        baseViewHolder.setOnClickListener(R.id.live_item_lay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLiveItemClickListener.onItemClick(liveVo);
            }
        });
    }

    public interface onLiveItemClickListener{
        void onItemClick(LiveVo liveVo);
    }

    public void setOnLiveItemClickListener(onLiveItemClickListener onLiveItemClickListener) {
        this.onLiveItemClickListener = onLiveItemClickListener;
    }
}
