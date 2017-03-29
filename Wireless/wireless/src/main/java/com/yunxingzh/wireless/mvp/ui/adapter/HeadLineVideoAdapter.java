package com.yunxingzh.wireless.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.utils.StringUtils;

import java.util.List;

import wireless.libs.bean.vo.HotInfo;

/**
 * Created by stephon on 2016/11/6.
 * 头条-应用
 */

public class HeadLineVideoAdapter extends BaseAdapter {

    private Context context;
    private List<HotInfo> infos;
    private LayoutInflater inflater;

    public HeadLineVideoAdapter(Context context, List<HotInfo> infos) {
        this.context = context;
        this.infos = infos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return infos != null ? infos.size() : null;
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
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
            convertView = inflater.inflate(R.layout.list_item_videos, null);
            holder.mVideoImg = (ImageView) convertView.findViewById(R.id.video_img);
            holder.mVideoTitle = (TextView) convertView.findViewById(R.id.video_title);
            holder.mVideoTime = (TextView) convertView.findViewById(R.id.video_time);
            holder.mVideoPlayCount = (TextView) convertView.findViewById(R.id.video_play_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HotInfo hotInfo = infos.get(position);
        holder.mVideoTitle.setText(hotInfo.title);
        holder.mVideoTime.setText(hotInfo.source + "    " + StringUtils.formatDate(hotInfo.ctime));
        holder.mVideoPlayCount.setText(hotInfo.play + "次播放");
        Glide.with(context).load(hotInfo.images.get(0)).placeholder(R.drawable.img_default).into(holder.mVideoImg);

        return convertView;
    }

    class ViewHolder {
        public ImageView mVideoImg;
        public TextView mVideoTitle, mVideoTime, mVideoPlayCount;
    }

}
