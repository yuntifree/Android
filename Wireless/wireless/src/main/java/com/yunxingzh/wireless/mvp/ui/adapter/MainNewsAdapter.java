package com.yunxingzh.wireless.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by stephon on 2016/11/25.
 */

public class MainNewsAdapter extends BaseAdapter {

    private Context context;
    private List<WeatherNewsVo.WeatherNewsData.mainNewsVo> newsList;

    private LayoutInflater inflater;
    private final int TYPE_COUNT = 4;
    private final int TYPE_ONE = 1;//一张图片
    private final int TYPE_TWO = 2;//二张图片
    private final int TYPE_THREE = 3;//三张图片
    private final int TYPE_NULL = 0;//没有图片
    private int currentType;

    public MainNewsAdapter(Context context, List<WeatherNewsVo.WeatherNewsData.mainNewsVo> newsList) {
        this.context = context;
        this.newsList = newsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final WeatherNewsVo.WeatherNewsData.mainNewsVo result = newsList.get(position);
        currentType = getItemViewType(position);

        if (currentType == TYPE_ONE || currentType == TYPE_TWO) { //加载第二种布局（一张或二张）
            MainNewsAdapter.ViewHolderTwo viewHolderTwo;
            if (convertView == null) {
                viewHolderTwo = new MainNewsAdapter.ViewHolderTwo();
                convertView = inflater.inflate(R.layout.list_item_news_type_two_img, null);
                viewHolderTwo.mTypeTwoTime = (TextView) convertView.findViewById(R.id.type_two_time);
                viewHolderTwo.mTypeTwoTitle = (TextView) convertView.findViewById(R.id.type_two_title);
                viewHolderTwo.mTypeTwoImg = (ImageView) convertView.findViewById(R.id.type_two_img);
                convertView.setTag(viewHolderTwo);
            } else {
                viewHolderTwo = (MainNewsAdapter.ViewHolderTwo) convertView.getTag();
            }

            if (result.isClick()) {//已点击
                viewHolderTwo.mTypeTwoTitle.setTextColor(context.getResources().getColor(R.color.gray_848484));
            } else {
                viewHolderTwo.mTypeTwoTitle.setTextColor(context.getResources().getColor(R.color.gray_434343));
            }

            Glide.with(context).load(result.getImages().get(0)).placeholder(R.drawable.img_default).into(viewHolderTwo.mTypeTwoImg);
            viewHolderTwo.mTypeTwoTime.setText(result.getSource() + "    " + StringUtils.formatDate(result.getCtime()));
            viewHolderTwo.mTypeTwoTitle.setText(result.getTitle());
        } else if (currentType == TYPE_THREE) {//加载第三种布局（三张图片）
            MainNewsAdapter.ViewHolderThree viewHolderThree;
            if (convertView == null) {
                viewHolderThree = new MainNewsAdapter.ViewHolderThree();
                convertView = inflater.inflate(R.layout.list_item_news_type_three_img, null);
                viewHolderThree.mTypeThreeLeftImg = (ImageView) convertView.findViewById(R.id.type_three_left_img);
                viewHolderThree.mTypeThreeMiddleImg = (ImageView) convertView.findViewById(R.id.type_three_middle_img);
                viewHolderThree.mTypeThreeRightImg = (ImageView) convertView.findViewById(R.id.type_three_right_img);
                viewHolderThree.mTypeThreeTitle = (TextView) convertView.findViewById(R.id.type_three_title);
                viewHolderThree.mTypeThreeTime = (TextView) convertView.findViewById(R.id.type_three_time);
                convertView.setTag(viewHolderThree);
            } else {
                viewHolderThree = (MainNewsAdapter.ViewHolderThree) convertView.getTag();
            }

            if (result.isClick()) {//已点击
                viewHolderThree.mTypeThreeTitle.setTextColor(context.getResources().getColor(R.color.gray_848484));
            } else {
                viewHolderThree.mTypeThreeTitle.setTextColor(context.getResources().getColor(R.color.gray_434343));
            }

            List<String> imgs = result.getImages();
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

            viewHolderThree.mTypeThreeTitle.setText(result.getTitle());
            viewHolderThree.mTypeThreeTime.setText(result.getSource() + "    " + StringUtils.formatDate(result.getCtime()));
        } else if (currentType == TYPE_NULL) {//无图片
            MainNewsAdapter.ViewHolderOne viewHolderOne;
            if (convertView == null) {
                viewHolderOne = new ViewHolderOne();
                convertView = inflater.inflate(R.layout.list_item_news_type_advier, null);
                viewHolderOne.mTypeOneImg = (ImageView) convertView.findViewById(R.id.type_one_img);
                viewHolderOne.mTypeOneTime = (TextView) convertView.findViewById(R.id.type_one_time);
                viewHolderOne.mTypeOneTitle = (TextView) convertView.findViewById(R.id.type_one_title);
                convertView.setTag(viewHolderOne);
            } else {
                viewHolderOne = (MainNewsAdapter.ViewHolderOne) convertView.getTag();
            }

            if (result.isClick()) {//已点击
                viewHolderOne.mTypeOneTitle.setTextColor(context.getResources().getColor(R.color.gray_848484));
            } else {
                viewHolderOne.mTypeOneTitle.setTextColor(context.getResources().getColor(R.color.gray_434343));
            }

            viewHolderOne.mTypeOneImg.setVisibility(View.GONE);
            viewHolderOne.mTypeOneTime.setText(result.getSource() + "    " + StringUtils.formatDate(result.getCtime()));
            viewHolderOne.mTypeOneTitle.setText(result.getTitle());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!result.isClick()) {
                    result.setClick(true);
                    startActivity(WebViewActivity.class, position, result,true);
                } else {
                    startActivity(WebViewActivity.class, position, result,true);
                }
            }
        });
        return convertView;
    }

    public void startActivity(Class activity, int position, WeatherNewsVo.WeatherNewsData.mainNewsVo result,boolean fromNews) {
        EventBus.getDefault().post(new EventBusType(Constants.MAIN_NEWS_FLAG, position));
        Intent intent = new Intent(context, activity);
        intent.putExtra(Constants.URL, result.getDst());
        intent.putExtra(Constants.TITLE, result.getTitle());
        intent.putExtra(Constants.FROM_NEWS,fromNews);
        context.startActivity(intent);
        notifyDataSetChanged();
    }

    class ViewHolderOne {
        public ImageView mTypeOneImg;
        public TextView mTypeOneTitle, mTypeOneTime;
    }

    class ViewHolderTwo {
        public TextView mTypeTwoTime, mTypeTwoTitle;
        public ImageView mTypeTwoImg;
    }

    class ViewHolderThree {
        public TextView mTypeThreeTime, mTypeThreeTitle;
        public ImageView mTypeThreeLeftImg, mTypeThreeMiddleImg, mTypeThreeRightImg;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (newsList.get(position).getImages() == null) {
            type = TYPE_NULL;
        } else {
            type = newsList.get(position).getImages().size();//根据图片size确定item布局
        }
        switch (type) {
            case TYPE_NULL://无图片
                return TYPE_NULL;
            case TYPE_ONE://一张或二张图片
            case TYPE_TWO:
                return TYPE_TWO;
            case TYPE_THREE://三章图片
                return TYPE_THREE;
        }
        return -1;
    }
}
