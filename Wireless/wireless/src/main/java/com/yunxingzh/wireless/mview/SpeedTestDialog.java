package com.yunxingzh.wireless.mview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by stephon on 2016/11/14.
 * 自定义测速对话框
 */

public class SpeedTestDialog extends Dialog{

    private Context context;
    private TextView mMyQueryBtn,mMyCancel;

    public SpeedTestDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_speed_test);
        mMyCancel = (TextView)findViewById(R.id.my_cancel);
        mMyCancel.setOnClickListener(buttonDialogListener);
        mMyQueryBtn = (TextView)findViewById(R.id.my_query);
        mMyQueryBtn.setOnClickListener(buttonDialogListener);
        //注册EventBus
        EventBus.getDefault().register(context);
    }

    private View.OnClickListener buttonDialogListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.my_cancel){
                if (EventBus.getDefault().isRegistered(context)) {
                    EventBus.getDefault().unregister(context);
                }
                dismiss();
            } else if (view.getId() == R.id.my_query){
                EventBus.getDefault().post(new EventBusType(Constants.SPEED_TEST,Constants.SPEED_FLAG));
                if (EventBus.getDefault().isRegistered(context)) {
                    EventBus.getDefault().unregister(context);
                }
                dismiss();
            }
        }
    };
}
