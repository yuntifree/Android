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
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import java.util.List;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLineNewsAdapter extends BaseAdapter {

    private Context context;
    private List<NewsVo.Data.NewsData> dataList;
    private LayoutInflater inflater;
    private final int TYPE_COUNT = 2;
    private final int TYPE_ONE = 0;
    private final int TYPE_TWO = 1;
    private int currentType;
    private boolean isMainNews;

    public HeadLineNewsAdapter(Context context, List<NewsVo.Data.NewsData> dataList, boolean isMainNews) {
        this.context = context;
        this.dataList = dataList;
        this.isMainNews = isMainNews;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (isMainNews) {
            return 3;
        } else {
            return dataList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsVo.Data.NewsData result = dataList.get(position);
        currentType = getItemViewType(position);
        if (currentType == TYPE_ONE) { //加载第一种布局
            ViewHolderOne viewHolderOne;
            if (convertView == null) {
                viewHolderOne = new ViewHolderOne();
                convertView = inflater.inflate(R.layout.list_item_news_type_one, null);
                viewHolderOne.mTypeOneLeftImg = (ImageView) convertView.findViewById(R.id.type_one_left_img);
                viewHolderOne.mTypeOneMiddleImg = (ImageView) convertView.findViewById(R.id.type_one_middle_img);
                viewHolderOne.mTypeOneRightImg = (ImageView) convertView.findViewById(R.id.type_one_right_img);
                viewHolderOne.mTypeOneTitle = (TextView) convertView.findViewById(R.id.type_one_title);
                viewHolderOne.mTypeOneTime = (TextView) convertView.findViewById(R.id.type_one_time);
                convertView.setTag(viewHolderOne);
            } else {
                viewHolderOne = (ViewHolderOne) convertView.getTag();
            }

            List<String> imgs = result.getImages();
            if (imgs.size() == 1) {
                Glide.with(context).load(imgs.get(0)).placeholder(R.drawable.img_default).into(viewHolderOne.mTypeOneLeftImg);
            } else if(imgs.size() == 2){
                Glide.with(context).load(imgs.get(0)).placeholder(R.drawable.img_default).into(viewHolderOne.mTypeOneLeftImg);
                Glide.with(context).load(imgs.get(1)).placeholder(R.drawable.img_default).into(viewHolderOne.mTypeOneMiddleImg);
            } else if(imgs.size() == 3){
                Glide.with(context).load(imgs.get(0)).placeholder(R.drawable.img_default).into(viewHolderOne.mTypeOneLeftImg);
                Glide.with(context).load(imgs.get(1)).placeholder(R.drawable.img_default).into(viewHolderOne.mTypeOneMiddleImg);
                Glide.with(context).load(imgs.get(2)).placeholder(R.drawable.img_default).into(viewHolderOne.mTypeOneRightImg);
            }


            viewHolderOne.mTypeOneTitle.setText(result.getTitle());
            viewHolderOne.mTypeOneTime.setText(result.getCtime());

        } else if (currentType == TYPE_TWO) { //加载第二种布局
            ViewHolderTwo viewHolderTwo;
            if (convertView == null) {
                viewHolderTwo = new ViewHolderTwo();
                convertView = inflater.inflate(R.layout.list_item_news_type_two, null);
                viewHolderTwo.mTypeTwoTime = (TextView) convertView.findViewById(R.id.type_two_time);
                viewHolderTwo.mTypeTwoTitle = (TextView) convertView.findViewById(R.id.type_two_title);
                viewHolderTwo.mTypeTwoImg = (ImageView) convertView.findViewById(R.id.type_two_img);
                convertView.setTag(viewHolderTwo);
            } else {
                viewHolderTwo = (ViewHolderTwo) convertView.getTag();
            }
            Glide.with(context).load(result.getImages()).into(viewHolderTwo.mTypeTwoImg);
            viewHolderTwo.mTypeTwoTime.setText(result.getTitle());
            viewHolderTwo.mTypeTwoTitle.setText(result.getTitle());
        }
        return convertView;
    }

    class ViewHolderOne {
        public ImageView mTypeOneLeftImg, mTypeOneMiddleImg, mTypeOneRightImg;
        public TextView mTypeOneTitle, mTypeOneTime;
    }

    class ViewHolderTwo {
        public TextView mTypeTwoTime, mTypeTwoTitle;
        public ImageView mTypeTwoImg;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        NewsVo.Data.NewsData result = dataList.get(position);
        //int type = result.getType();//需要后台提供type字段
//        switch (type) {
//            case TYPE_ONE:
//                return TYPE_ONE;
//            case TYPE_TWO:
//                return TYPE_TWO;
//            default:
//                return -1;
//        }
        return 0;
    }
}
