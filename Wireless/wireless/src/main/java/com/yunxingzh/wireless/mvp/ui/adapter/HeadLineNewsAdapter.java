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

import java.util.List;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLineNewsAdapter extends BaseAdapter {

    private Context context;
    private List<NewsVo.Data.NewsData> dataList;
    private LayoutInflater inflater;
    private final int TYPE_COUNT = 2;
    private final int TYPE_ONE = 1;
    private final int TYPE_TWO = 2;
    private final int TYPE_THREE = 3;
    private final int TYPE_NULL = 0;
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
                viewHolderOne.mTypeOneTime = (TextView) convertView.findViewById(R.id.type_one_time);
                viewHolderOne.mTypeOneTitle = (TextView) convertView.findViewById(R.id.type_one_title);
                viewHolderOne.mTypeOneImg = (ImageView) convertView.findViewById(R.id.type_one_img);
                convertView.setTag(viewHolderOne);
            } else {
                viewHolderOne = (ViewHolderOne) convertView.getTag();
            }
            Glide.with(context).load(result.getImages().get(0)).placeholder(R.drawable.img_default).into(viewHolderOne.mTypeOneImg);
            viewHolderOne.mTypeOneTime.setText(result.getCtime());
            viewHolderOne.mTypeOneTitle.setText(result.getTitle());

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
            Glide.with(context).load(result.getImages().get(0)).placeholder(R.drawable.img_default).into(viewHolderTwo.mTypeTwoImg);
            viewHolderTwo.mTypeTwoTime.setText(result.getCtime());
            viewHolderTwo.mTypeTwoTitle.setText(result.getTitle());
        } else if (currentType == TYPE_THREE) {//加载第三种布局
            ViewHolderThree viewHolderThree;
            if (convertView == null) {
                viewHolderThree = new ViewHolderThree();
                convertView = inflater.inflate(R.layout.list_item_news_type_three, null);
                viewHolderThree.mTypeThreeLeftImg = (ImageView) convertView.findViewById(R.id.type_three_left_img);
                viewHolderThree.mTypeThreeMiddleImg = (ImageView) convertView.findViewById(R.id.type_three_middle_img);
                viewHolderThree.mTypeThreeRightImg = (ImageView) convertView.findViewById(R.id.type_three_right_img);
                viewHolderThree.mTypeThreeTitle = (TextView) convertView.findViewById(R.id.type_three_title);
                viewHolderThree.mTypeThreeTime = (TextView) convertView.findViewById(R.id.type_three_time);
                convertView.setTag(viewHolderThree);
            } else {
                viewHolderThree = (ViewHolderThree) convertView.getTag();
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
            viewHolderThree.mTypeThreeTime.setText(result.getCtime());
        }
        return convertView;
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
        int type = dataList.get(position).getImages().size();//根据图片size确定item布局
        switch (type) {
            case TYPE_ONE:
                return TYPE_ONE;
            case TYPE_NULL:
            case TYPE_TWO:
                return TYPE_TWO;
            case TYPE_THREE:
                return TYPE_THREE;
        }
        return -1;
    }
}
