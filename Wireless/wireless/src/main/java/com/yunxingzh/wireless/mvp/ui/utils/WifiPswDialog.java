package com.yunxingzh.wireless.mvp.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yunxingzh.wireless.R;

/**
 * Created by stephon on 2016/11/14.
 */

public class WifiPswDialog extends Dialog{
    private Button cancelButton;
    private Button okButton;
    private EditText pswEdit;
    private OnCustomDialogListener customDialogListener;
    public WifiPswDialog(Context context, OnCustomDialogListener customListener) {
        super(context);
        customDialogListener = customListener;

    }
    //定义dialog的回调事件
    public interface OnCustomDialogListener{
        void back(String str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_connect_wifi);
        setTitle("请输入密码");
        pswEdit = (EditText)findViewById(R.id.dia_pwd_et);
        cancelButton = (Button)findViewById(R.id.dia_cancel);
        okButton = (Button)findViewById(R.id.dia_query);
        cancelButton.setOnClickListener(buttonDialogListener);
        okButton.setOnClickListener(buttonDialogListener);

    }

    private View.OnClickListener buttonDialogListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.dia_cancel){
                pswEdit = null;
                customDialogListener.back(null);
                cancel();//自动调用dismiss();
            } else if(view.getId() == R.id.dia_query){
                customDialogListener.back(pswEdit.getText().toString());
                dismiss();
            }
        }
    };
}
