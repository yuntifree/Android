package com.yunxingzh.wireless.mvp.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import wireless.libs.bean.vo.HotInfo;

/**
 * Created by stephon on 2016/11/6.
 * 头条-应用
 */

public class HeadLineVideoAdapter extends BaseQuickAdapter<HotInfo> {

    private EventBus eventBus;

    public HeadLineVideoAdapter(List<HotInfo> data) {
        super(R.layout.list_item_videos, data);
       /// eventBus.register(mContext);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HotInfo newsVo) {
        Glide.with(mContext).load(newsVo.images.get(0)).placeholder(R.drawable.img_default).into((ImageView) baseViewHolder.getView(R.id.video_img));
        baseViewHolder.setText(R.id.video_title, newsVo.title);
        baseViewHolder.setText(R.id.video_time, newsVo.source + "    " + StringUtils.formatDate(newsVo.ctime));
        baseViewHolder.setText(R.id.video_play_count, newsVo.play + "次播放");
    }
}
