package com.yunxingzh.wireless.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import wireless.libs.bean.resp.JokeList;

/**
 * Created by stephen on 2017/2/26.
 */

public class EpisodeAdapter extends BaseAdapter {

    private Context context;
    private List<JokeList.JokeVo> jokeVos;
    private LayoutInflater inflater;

    public EpisodeAdapter(Context context, List<JokeList.JokeVo> jokeVos) {
        this.context = context;
        this.jokeVos = jokeVos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return jokeVos != null ? jokeVos.size() : null;
    }

    @Override
    public Object getItem(int position) {
        return jokeVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_episode, null);
            holder.mJokeLikeIv = (ImageView) convertView.findViewById(R.id.joke_like_iv);
            holder.mJokeUnlikeIv = (ImageView) convertView.findViewById(R.id.joke_unlike_iv);
            holder.mJokeLikeTv = (TextView) convertView.findViewById(R.id.joke_like_tv);
            holder.mJokeUnlikeTv = (TextView) convertView.findViewById(R.id.joke_unlike_tv);
            holder.mJokeContentTv = (TextView) convertView.findViewById(R.id.joke_content_tv);
            holder.mJokeLikeLay = (LinearLayout) convertView.findViewById(R.id.joke_like_lay);
            holder.mJokeUnlikeLay = (LinearLayout) convertView.findViewById(R.id.joke_unlike_lay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final JokeList.JokeVo data = jokeVos.get(position);
        holder.mJokeContentTv.setText(data.content);
        if (!data.isLikeClick) {//赞：未点击false
            holder.mJokeLikeTv.setText(data.heart + "人");
            holder.mJokeLikeTv.setTextColor(Color.parseColor("#969696"));
            Glide.with(context).load(R.drawable.ico_like_gray).placeholder(R.drawable.ico_like_gray).into(holder.mJokeLikeIv);
        } else {
            holder.mJokeLikeTv.setText(data.heart + "人");
            holder.mJokeLikeTv.setTextColor(Color.parseColor("#ff7a2f"));
            Glide.with(context).load(R.drawable.ico_like_yellow).placeholder(R.drawable.ico_like_yellow).into(holder.mJokeLikeIv);
        }

        if (!data.isUnlikeClick) {
            holder.mJokeUnlikeTv.setText(data.bad + "人");
            holder.mJokeUnlikeTv.setTextColor(Color.parseColor("#969696"));
            Glide.with(context).load(R.drawable.ico_unlike).placeholder(R.drawable.ico_unlike).into(holder.mJokeUnlikeIv);
        } else {
            holder.mJokeUnlikeTv.setText(data.bad + "人");
            holder.mJokeUnlikeTv.setTextColor(Color.parseColor("#ff7a2f"));
            Glide.with(context).load(R.drawable.ico_unlike_yellow).placeholder(R.drawable.ico_unlike_yellow).into(holder.mJokeUnlikeIv);
        }

        holder.mJokeLikeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.animation(v);
                if (data.isLikeClick || data.isUnlikeClick) {
                    ToastUtil.showMiddle(context, "您已经评价过了");
                } else {
                    data.isLikeClick = true;
                    data.heart++;
                    holder.mJokeLikeTv.setText(data.heart + "人");
                    holder.mJokeLikeTv.setTextColor(Color.parseColor("#ff7a2f"));
                    Glide.with(context).load(R.drawable.ico_like_yellow).placeholder(R.drawable.ico_like_gray).into(holder.mJokeLikeIv);
                    EventBus.getDefault().post(new EventBusType(Constants.JOKE_ZAN_FLAG, position));
                }
        }
    }
    );

    holder.mJokeUnlikeLay.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick (View v){
            AppUtils.animation(v);
            if (data.isLikeClick || data.isUnlikeClick) {
                ToastUtil.showMiddle(context, "您已经评价过了");
           } else {
                data.isUnlikeClick = true;
                data.bad++;
                holder.mJokeUnlikeTv.setText(data.bad + "人");
                holder.mJokeUnlikeTv.setTextColor(Color.parseColor("#ff7a2f"));
                Glide.with(context).load(R.drawable.ico_unlike_yellow).placeholder(R.drawable.ico_unlike).into(holder.mJokeUnlikeIv);
                EventBus.getDefault().post(new EventBusType(Constants.JOKE_CAI_FLAG, position));
        }
    }
    }
    );
    return convertView;
}

class ViewHolder {
    public ImageView mJokeUnlikeIv, mJokeLikeIv;
    private LinearLayout mJokeUnlikeLay, mJokeLikeLay;
    public TextView mJokeUnlikeTv, mJokeLikeTv, mJokeContentTv;
}
}
