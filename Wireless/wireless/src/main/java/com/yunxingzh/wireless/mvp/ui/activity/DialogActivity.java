package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.fragment.APointDetailFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.IptPasswordFragment;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiVo;

import java.io.Serializable;
import java.util.List;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_TYPE = "extra_type";
    private static final String EXTRA_ACCESS_POINT = "extra_access_point";
    private static final String EXTRA_SHOW_ERROR = "extra_show_error";
    public static final int TYPE_ACCESS_POINT_DETAIL = 0;
    public static final int TYPE_INPUT_PASSWORD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        findViewById(R.id.close).setOnClickListener(this);
        setContent();
    }

    public static void showDetail(Context context, AccessPoint accessPoint) {
        Intent intent = new Intent();
        intent.setClass(context, DialogActivity.class);
        intent.putExtra(EXTRA_TYPE, TYPE_ACCESS_POINT_DETAIL);
        intent.putExtra(EXTRA_ACCESS_POINT, accessPoint);
        context.startActivity(intent);
    }

    public static void showInuptPWD(Context context, AccessPoint accessPoint, boolean isShowError){
        Intent intent = new Intent();
        intent.setClass(context, DialogActivity.class);
        intent.putExtra(EXTRA_TYPE, TYPE_INPUT_PASSWORD);
        intent.putExtra(EXTRA_ACCESS_POINT, accessPoint);
        intent.putExtra(EXTRA_SHOW_ERROR, isShowError);
        context.startActivity(intent);
    }

    private void setContent(){
        Intent intent = getIntent();
        int type = intent.getIntExtra(EXTRA_TYPE, TYPE_ACCESS_POINT_DETAIL);
        if(type == TYPE_ACCESS_POINT_DETAIL){
            AccessPoint accessPoint = intent.getParcelableExtra(EXTRA_ACCESS_POINT);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content, APointDetailFragment.newInstance(accessPoint)).commit();
        } else if(type == TYPE_INPUT_PASSWORD){
            AccessPoint accessPoint = intent.getParcelableExtra(EXTRA_ACCESS_POINT);
            boolean isShowError = intent.getBooleanExtra(EXTRA_SHOW_ERROR, false);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content, IptPasswordFragment.newInstance(accessPoint, isShowError)).commit();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.close){
            finish();
        }
    }
}
