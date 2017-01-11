package com.yunxingzh.wireless.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import wireless.libs.bean.vo.HotInfo;

/**
 * Created by stephen on 2017/1/4.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<HotInfo> hotInfos;
    private final int TYPE_COUNT = 4;
    private final int TYPE_ONE = 1;//一张图片
    private final int TYPE_TWO = 2;//二张图片
    private final int TYPE_THREE = 3;//三张图片
    private final int TYPE_NULL = 0;//没有图片
    private final int TYPE_ADVIER = 5;//广告
    private HotInfo source;

    public NewsAdapter(Context context, List<HotInfo> data) {
        this.context = context;
        this.hotInfos = data;
    }

    public void startActivity(Class activity, int position, HotInfo result) {
        EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE_NEWS_FLAG, position));
        Intent intent = new Intent(context, activity);
        intent.putExtra(Constants.URL, result.dst);
        intent.putExtra(Constants.TITLE, result.title);
        context.startActivity(intent);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ONE:
            case TYPE_TWO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_type_two_img, parent, false);
                return new ViewHolderTwo(view);
            case TYPE_THREE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_type_three_img, parent, false);
                return new ViewHolderThree(view);
            case TYPE_NULL:
            case TYPE_ADVIER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news_type_advier, parent, false);
                return new ViewHolderOne(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (hotInfos.size() <= position) {
            return;
        }
        source = hotInfos.get(position);
        switch (getItemViewType(position)) {
            case TYPE_ADVIER:
            case TYPE_NULL:
                ViewHolderOne holderOne = (ViewHolderOne) holder;
                holderOne.mTypeOneTime.setText(source.source + "    " + StringUtils.formatDate(source.ctime));
                holderOne.mTypeOneTitle.setText(source.title);
                if (source.images != null && source.images.size() > 0) {
                    Glide.with(context).load(source.images.get(0)).placeholder(R.drawable.img_default).into(holderOne.mTypeOneImg);
                } else {
                    holderOne.mTypeOneImg.setVisibility(View.GONE);
                }
                if (source.isClickColor) {//已点击
                    holderOne.mTypeOneTitle.setTextColor(context.getResources().getColor(R.color.gray_848484));
                } else {
                    holderOne.mTypeOneTitle.setTextColor(context.getResources().getColor(R.color.gray_434343));
                }
                break;
            case TYPE_ONE:
            case TYPE_TWO:
                ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
                viewHolderTwo.mTypeTwoTime.setText(source.source + "    " + StringUtils.formatDate(source.ctime));
                viewHolderTwo.mTypeTwoTitle.setText(source.title);
                Glide.with(context).load(source.images.get(0)).placeholder(R.drawable.img_default).into(viewHolderTwo.mTypeTwoImg);
                if (source.isClickColor) {//已点击
                    viewHolderTwo.mTypeTwoTitle.setTextColor(context.getResources().getColor(R.color.gray_848484));
                } else {
                    viewHolderTwo.mTypeTwoTitle.setTextColor(context.getResources().getColor(R.color.gray_434343));
                }
                break;
            case TYPE_THREE:
                ViewHolderThree viewHolderThree = (ViewHolderThree) holder;
                viewHolderThree.mTypeThreeTime.setText(source.source + "    " + StringUtils.formatDate(source.ctime));
                viewHolderThree.mTypeThreeTitle.setText(source.title);

                List<String> imgs = source.images;
                if (imgs.size() == 1) {
                    Glide.with(context).load(imgs.get(0)).placeholder(R.drawable.img_default).into(viewHolderThree.mTypeThreeLeftImg);
                } else if (imgs.size() == 2) {
                    Glide.with(context).load(imgs.get(0)).placeholder(R.drawable.img_default).into(viewHolderThree.mTypeThreeLeftImg);
                    Glide.with(context).load(imgs.get(1)).placeholder(R.drawable.img_default).into(viewHolderThree.mTypeThreeMiddleImg);
                } else if (imgs.size() == 3) {
                    Glide.with(context).load(imgs.get(0)).placeholder(R.drawable.img_default).into(viewHolderThree.mTypeThreeLeftImg);
                    Glide.with(context).load(imgs.get(1)).placeholder(R.drawable.img_default).into(viewHolderThree.mTypeThreeMiddleImg);
                    Glide.with(context).load(imgs.get(2)).placeholder(R.drawable.img_default).into(viewHolderThree.mTypeThreeRightImg);
                }

                if (source.isClickColor) {//已点击
                    viewHolderThree.mTypeThreeTitle.setTextColor(context.getResources().getColor(R.color.gray_848484));
                } else {
                    viewHolderThree.mTypeThreeTitle.setTextColor(context.getResources().getColor(R.color.gray_434343));
                }
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotInfo clickData = hotInfos.get(position);
                if (!clickData.isClickColor) {
                    clickData.isClickColor = true;
                    startActivity(WebViewActivity.class, position, clickData);
                } else {
                    startActivity(WebViewActivity.class, position, clickData);
                }
            }
        });
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        ImageView mTypeOneImg;
        TextView mTypeOneTitle, mTypeOneTime;

        public ViewHolderOne(View itemView) {
            super(itemView);
            mTypeOneImg = (ImageView) itemView.findViewById(R.id.type_one_img);
            mTypeOneTime = (TextView) itemView.findViewById(R.id.type_one_time);
            mTypeOneTitle = (TextView) itemView.findViewById(R.id.type_one_title);
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        TextView mTypeTwoTime, mTypeTwoTitle;
        ImageView mTypeTwoImg;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            mTypeTwoTime = (TextView) itemView.findViewById(R.id.type_two_time);
            mTypeTwoTitle = (TextView) itemView.findViewById(R.id.type_two_title);
            mTypeTwoImg = (ImageView) itemView.findViewById(R.id.type_two_img);
        }
    }

    public class ViewHolderThree extends RecyclerView.ViewHolder {
        TextView mTypeThreeTime, mTypeThreeTitle;
        ImageView mTypeThreeLeftImg, mTypeThreeMiddleImg, mTypeThreeRightImg;

        public ViewHolderThree(View itemView) {
            super(itemView);
            mTypeThreeLeftImg = (ImageView) itemView.findViewById(R.id.type_three_left_img);
            mTypeThreeMiddleImg = (ImageView) itemView.findViewById(R.id.type_three_middle_img);
            mTypeThreeRightImg = (ImageView) itemView.findViewById(R.id.type_three_right_img);
            mTypeThreeTitle = (TextView) itemView.findViewById(R.id.type_three_title);
            mTypeThreeTime = (TextView) itemView.findViewById(R.id.type_three_time);
        }
    }

    @Override
    public int getItemCount() {
        return hotInfos.size();
    }

    //决定元素的布局使用哪种类型
    @Override
    public int getItemViewType(int position) {
        int type;
        if (hotInfos.size() <= position) {
            return -1;
        }
        if (hotInfos.get(position).stype == 1) {//广告
            type = TYPE_ADVIER;
            return TYPE_ADVIER;
        } else {//新闻
            if (hotInfos.get(position).images == null) {
                type = TYPE_NULL;
            } else {
                type = hotInfos.get(position).images.size();//根据图片size确定item布局
            }
            switch (type) {
                case TYPE_NULL://无图片
                    return TYPE_NULL;
                case TYPE_ONE://一张或二张图片
                case TYPE_TWO:
                    return TYPE_TWO;
                case TYPE_THREE://三张图片
                    return TYPE_THREE;
            }
            return -1;
        }
    }
}
