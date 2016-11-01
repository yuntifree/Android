/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yunxingzh.wirelesslibs.wireless.lib.view.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunxingzh.wirelesslibs.R;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AreaUtils;

import java.util.List;

import com.yunxingzh.wirelesslibs.widget.adapters.AbstractWheelAdapter;

public class AreaWheelAdapter extends AbstractWheelAdapter {

    // items
    private List<AreaUtils.Data> mItems;
    private Context mContext;

    public AreaWheelAdapter(Context context, List<AreaUtils.Data> items) {
        mContext = context;
        mItems = items;
    }

    public AreaUtils.Data getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public int getItemsCount() {
        return mItems.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.wheel_view_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTv.setText(getItem(index).getSname());
        return convertView;
    }

    class ViewHolder {
        public TextView nameTv;

        public ViewHolder(View convertView) {
            nameTv = (TextView) convertView.findViewById(R.id.name_tv);
        }
    }
}
