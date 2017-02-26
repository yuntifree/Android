package com.yunxingzh.wireless.mvp.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;

import java.util.List;

import wireless.libs.bean.resp.JokeList;

/**
 * Created by stephen on 2017/2/26.
 */

public class EpisodeAdapter extends BaseQuickAdapter<JokeList.JokeVo> {

    private boolean isClick = true;

    public EpisodeAdapter(List<JokeList.JokeVo> data) {
        super(R.layout.list_item_episode, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final JokeList.JokeVo jokeVo) {
        baseViewHolder.setText(R.id.joke_content_tv, jokeVo.content);
        baseViewHolder.setText(R.id.joke_unlike_tv, jokeVo.bad + "人");
        baseViewHolder.setText(R.id.joke_like_tv, jokeVo.heart + "人");
        baseViewHolder.setOnClickListener(R.id.joke_unlike_lay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {//踩
                    isClick = false;
                    baseViewHolder.setText(R.id.joke_unlike_tv, jokeVo.bad + 1 + "人");
                    baseViewHolder.setTextColor(R.id.joke_unlike_tv, Color.parseColor("#ff7a2f"));
                    Glide.with(mContext).load(R.drawable.ico_unlike_yellow).into((ImageView) baseViewHolder.getView(R.id.joke_unlike_iv));
                } else {//取消踩
                    isClick = true;
                    if (jokeVo.bad > 0) {
                        baseViewHolder.setText(R.id.joke_unlike_tv, jokeVo.bad - 1 + "人");
                    } else {
                        baseViewHolder.setText(R.id.joke_unlike_tv, jokeVo.bad + "人");
                    }
                    baseViewHolder.setTextColor(R.id.joke_unlike_tv, Color.parseColor("#969696"));
                    Glide.with(mContext).load(R.drawable.ico_unlike).into((ImageView) baseViewHolder.getView(R.id.joke_unlike_iv));
                }
            }
        });

        baseViewHolder.setOnClickListener(R.id.joke_like_lay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {//点赞
                    isClick = false;
                    baseViewHolder.setText(R.id.joke_like_tv, jokeVo.heart + 1 + "人");
                    baseViewHolder.setTextColor(R.id.joke_like_tv, Color.parseColor("#ff7a2f"));
                    Glide.with(mContext).load(R.drawable.ico_like_yellow).into((ImageView) baseViewHolder.getView(R.id.joke_like_iv));
                } else {//取消赞
                    isClick = true;
                    if (jokeVo.heart > 0) {
                        baseViewHolder.setText(R.id.joke_like_tv, jokeVo.heart - 1 + "人");
                    } else {
                        baseViewHolder.setText(R.id.joke_like_tv, jokeVo.heart + "人");
                    }
                    baseViewHolder.setTextColor(R.id.joke_like_tv, Color.parseColor("#969696"));
                    Glide.with(mContext).load(R.drawable.ico_like_gray).into((ImageView) baseViewHolder.getView(R.id.joke_like_iv));
                }
            }
        });
    }
}
