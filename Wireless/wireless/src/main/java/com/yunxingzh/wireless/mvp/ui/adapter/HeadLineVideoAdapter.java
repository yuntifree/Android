package com.yunxingzh.wireless.mvp.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.utils.StringUtils;

import java.util.List;

import wireless.libs.bean.vo.NewsVo;

/**
 * Created by stephon on 2016/11/6.
 * 头条-应用
 */

public class HeadLineVideoAdapter extends BaseQuickAdapter<NewsVo.Data.NewsData> {

    public HeadLineVideoAdapter(List<NewsVo.Data.NewsData> data) {
        super(R.layout.list_item_videos, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NewsVo.Data.NewsData newsVo) {
        Glide.with(mContext).load(newsVo.getImages().get(0)).placeholder(R.drawable.img_default).into((ImageView) baseViewHolder.getView(R.id.video_img));
        baseViewHolder.setText(R.id.video_title, newsVo.getTitle());
        baseViewHolder.setText(R.id.video_time, newsVo.getSource()+"    "+ StringUtils.formatDate(newsVo.getCtime()));
        baseViewHolder.setText(R.id.video_play_count,  newsVo.getPlay()+"次播放");
    }
}
