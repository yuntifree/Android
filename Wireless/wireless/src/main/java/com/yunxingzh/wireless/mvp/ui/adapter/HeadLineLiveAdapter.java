package com.yunxingzh.wireless.mvp.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;

import java.util.List;

import wireless.libs.bean.vo.LiveVo;

/**
 * Created by stephen on 2017/2/8.
 */

public class HeadLineLiveAdapter extends BaseQuickAdapter<LiveVo> {

    public HeadLineLiveAdapter(List<LiveVo> data) {
        super(R.layout.list_item_live, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LiveVo newsVo) {
//        Glide.with(mContext).load(newsVo.images.get(0)).placeholder(R.drawable.img_default).into((ImageView) baseViewHolder.getView(R.id.video_img));
//        baseViewHolder.setText(R.id.video_title, newsVo.title);
//        baseViewHolder.setText(R.id.video_time, newsVo.source + "    " + StringUtils.formatDate(newsVo.ctime));
//        baseViewHolder.setText(R.id.video_play_count, newsVo.play + "次播放");
    }
}
