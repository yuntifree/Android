package com.yunxingzh.wireless.mview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.bean.vo.UpdateVo;

/**
 * Created by stephen on 2017/1/18.
 * 版本更新对话框
 */

public class CheckUpdateDialog extends Dialog {
    private Context context;
    private TextView mUpdateQueryTv, mUpdateCancelTv, mTitleTv, mDescTv;
    private UpdateVo updateVo;

    public CheckUpdateDialog(Context context, UpdateVo updateVo) {
        super(context, R.style.myUpdateDialogTheme);
        this.context = context;
        this.updateVo = updateVo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check_update);
        mTitleTv = (TextView)findViewById(R.id.version_title);
        mDescTv = (TextView)findViewById(R.id.version_desc);
        if (this.updateVo != null) {
            if (!StringUtils.isEmpty(this.updateVo.title)) {
                mTitleTv.setText(this.updateVo.title);
            }
            if (!StringUtils.isEmpty(this.updateVo.desc)) {
                mDescTv.setText(this.updateVo.desc);
            }
        }
        mUpdateCancelTv = (TextView)findViewById(R.id.update_cancel_tv);
        mUpdateCancelTv.setOnClickListener(buttonDialogListener);
        mUpdateQueryTv = (TextView)findViewById(R.id.update_query_tv);
        mUpdateQueryTv.setOnClickListener(buttonDialogListener);
    }

    private View.OnClickListener buttonDialogListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.update_cancel_tv){
                dismiss();
            } else if (view.getId() == R.id.update_query_tv){
                if (updateVo != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateVo.downurl));
                    context.startActivity(intent);
                }
                dismiss();
            }
        }
    };
}
