package com.yunxingzh.wireless.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yunxingzh.wirelesslibs.convenientbanner.holder.Holder;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(final Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(final Context context, final int position, String data) {
        Glide.with(context).load(data).into(imageView);
    }
}
