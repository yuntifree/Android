package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.wifi.AccessPoint;

/***
 * wifi管理-密码连接dialog
 */

public class IptPasswordFragment extends Fragment {

    private static final String EXTRA_ACCESS_POINT = "extra_access_point";
    private static final String EXTRA_SHOW_ERROR = "extra_show_error";

    public static IptPasswordFragment newInstance(AccessPoint accessPoint, boolean isShowError) {
        IptPasswordFragment fragment = new IptPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ACCESS_POINT, accessPoint);
        bundle.putBoolean(EXTRA_SHOW_ERROR, isShowError);
        fragment.setArguments(bundle);
        return fragment;
    }

    private AccessPoint accessPoint;
    private boolean isShowError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        accessPoint = (AccessPoint) arguments.getParcelable(EXTRA_ACCESS_POINT);
        isShowError = arguments.getBoolean(EXTRA_SHOW_ERROR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_pwd, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView wifi_ssid = (TextView) view.findViewById(R.id.wifi_ssid);
        wifi_ssid.setText(accessPoint.ssid);
        final EditText edt_password = (EditText) view.findViewById(R.id.edt_password);

        Button btn_connect = (Button) view.findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = edt_password.getText().toString();

                if(TextUtils.isEmpty(pwd)){
                    //TODO: toast empty password
                } else {
                    //TODO: cb_share
                    accessPoint.setPassword(pwd, AccessPoint.PasswordFrom.INPUT);
                    FWManager.getInstance().connect(accessPoint);
                    getActivity().finish();
                }
            }
        });
    }
}
