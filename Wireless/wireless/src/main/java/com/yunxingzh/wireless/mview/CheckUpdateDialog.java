package com.yunxingzh.wireless.mview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;

import wireless.libs.bean.vo.UpdateVo;

/**
 * Created by stephen on 2017/1/18.
 * 版本更新对话框
 */

public class CheckUpdateDialog extends Dialog {
    private Context context;
    private TextView mUpdateQueryTv, mUpdateCancelTv, mVersionTv;
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
        mVersionTv = (TextView)findViewById(R.id.version_tv);
        if (updateVo != null) {
            mVersionTv.setText(updateVo.version.toString());
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
