package com.yunxingzh.wireless.mvp.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import wireless.libs.bean.resp.JokeList;

/**
 * Created by stephen on 2017/2/26.
 */

public class EpisodeAdapter extends BaseQuickAdapter<JokeList.JokeVo> {

    public EpisodeAdapter(List<JokeList.JokeVo> data) {
        super(R.layout.list_item_episode, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final JokeList.JokeVo jokeVo) {
        //baseViewHolder.setIsRecyclable(false);
        baseViewHolder.setText(R.id.joke_content_tv, jokeVo.content);
        baseViewHolder.setText(R.id.joke_unlike_tv, jokeVo.bad + "人");
        baseViewHolder.setText(R.id.joke_like_tv, jokeVo.heart + "人");
        baseViewHolder.setOnClickListener(R.id.joke_unlike_lay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!jokeVo.isClick) {//为false表示点踩未选中
                    jokeVo.isClick = true;
                    baseViewHolder.setText(R.id.joke_unlike_tv, jokeVo.bad + 1 + "人");
                    baseViewHolder.setTextColor(R.id.joke_unlike_tv, Color.parseColor("#ff7a2f"));
                    Glide.with(mContext).load(R.drawable.ico_unlike_yellow).placeholder(R.drawable.ico_unlike).into((ImageView) baseViewHolder.getView(R.id.joke_unlike_iv));
                    EventBus.getDefault().post(new EventBusType(Constants.JOKE_CAI_FLAG, baseViewHolder.getAdapterPosition()));
                } else {
                    ToastUtil.showMiddle(mContext, "您已经评价过了");
                }
            }
        });

        baseViewHolder.setOnClickListener(R.id.joke_like_lay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!jokeVo.isClick) {//为false表示点赞未选中
                    jokeVo.isClick = true;
                    baseViewHolder.setText(R.id.joke_like_tv, jokeVo.heart + 1 + "人");
                    baseViewHolder.setTextColor(R.id.joke_like_tv, Color.parseColor("#ff7a2f"));
                    Glide.with(mContext).load(R.drawable.ico_like_yellow).placeholder(R.drawable.ico_like_gray).into((ImageView) baseViewHolder.getView(R.id.joke_like_iv));
                    EventBus.getDefault().post(new EventBusType(Constants.JOKE_ZAN_FLAG, baseViewHolder.getAdapterPosition()));
                } else {
                    ToastUtil.showMiddle(mContext, "您已经评价过了");
                }
            }
        });
    }
}
