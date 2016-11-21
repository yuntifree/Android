package com.yunxingzh.wireless.mvp.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yunxingzh.wireless.R;

/**
 * Created by stephon on 2016/11/14.
 * 自定义测速对话框
 */

public class SpeedTestDialog extends Dialog{
    private TextView mMyQueryBtn;

    public SpeedTestDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_speed_test);
        mMyQueryBtn = (TextView)findViewById(R.id.my_cancel);
        mMyQueryBtn.setOnClickListener(buttonDialogListener);

    }

    private View.OnClickListener buttonDialogListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.my_cancel){
                dismiss();
            }
        }
    };
}
