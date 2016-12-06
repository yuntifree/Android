package com.yunxingzh.wireless.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;

import java.util.List;

import wireless.libs.bean.vo.NewsVo;

/**
 * Created by stephon on 2016/11/6.
 * 头条-应用
 */

public class HeadLineAppAdapter extends BaseQuickAdapter<NewsVo.Data> {

    public HeadLineAppAdapter(List<NewsVo.Data> data) {
        super(R.layout.list_item_videos, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NewsVo.Data newsVo) {
       // Glide.with(mContext).load(newsVo.getImage()).placeholder(R.drawable.img_default).into((ImageView) baseViewHolder.getView(R.id.item_image));
//        baseViewHolder.setText(R.id.item_title, newsVo.getTitle());
//        baseViewHolder.setText(R.id.item_date, newsVo.getTruetime());
//        baseViewHolder.setText(R.id.item_hit,  newsVo.getHit());
    }
}
