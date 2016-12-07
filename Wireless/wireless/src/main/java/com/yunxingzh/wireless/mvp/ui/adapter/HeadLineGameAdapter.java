package com.yunxingzh.wireless.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;

import java.util.List;

import wireless.libs.bean.vo.HotInfo;

/**
 * Created by stephon on 2016/11/6.
 * 头条-应用
 */

public class HeadLineGameAdapter extends BaseQuickAdapter<HotInfo> {

    public HeadLineGameAdapter(List<HotInfo> data) {
        super(R.layout.list_item_videos, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HotInfo newsVo) {

       // Glide.with(mContext).load(newsVo.getImage()).placeholder(R.drawable.img_default).into((ImageView) baseViewHolder.getView(R.id.item_image));
//        baseViewHolder.setText(R.id.item_title, newsVo.getTitle());
//        baseViewHolder.setText(R.id.item_date, newsVo.getTruetime());
//        baseViewHolder.setText(R.id.item_hit,  newsVo.getHit());
    }
}
