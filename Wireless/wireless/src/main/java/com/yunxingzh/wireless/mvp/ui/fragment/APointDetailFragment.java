package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.activity.DialogActivity;
import com.yunxingzh.wireless.wifi.AccessPoint;

public class APointDetailFragment extends Fragment {

    private static final String EXTRA_ACCESS_POINT = "extra_access_point";

    public static APointDetailFragment newInstance(AccessPoint accessPoint) {
        APointDetailFragment fragment = new APointDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ACCESS_POINT, accessPoint);
        fragment.setArguments(bundle);
        return fragment;
    }

    private AccessPoint accessPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        accessPoint = (AccessPoint) arguments.getParcelable(EXTRA_ACCESS_POINT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apoint_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_ssid = (TextView) view.findViewById(R.id.tv_ssid);
        tv_ssid.setText(accessPoint.ssid);

        TextView tv_mac = (TextView) view.findViewById(R.id.tv_mac);
        tv_mac.setText(accessPoint.bssid.toUpperCase());

        TextView tv_signal = (TextView) view.findViewById(R.id.tv_signal);
        tv_signal.setText(accessPoint.signal.percent() + "% - " + accessPoint.signal.level().description.toUpperCase());

        Button btn_connect = (Button) view.findViewById(R.id.btn_connect);
        Button btn_forget = (Button) view.findViewById(R.id.btn_forgetpwd);

        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FWManager.getInstance().ignore(accessPoint.ssid);
                getActivity().finish();
            }
        });

        AccessPoint current = FWManager.getInstance().getCurrent();
        if(current != null && current.ssid.equals(accessPoint.ssid)){
            btn_connect.setText(R.string.disconnect);
            btn_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FWManager.getInstance().disconnect();
                    getActivity().finish();
                }
            });
        } else {
            btn_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    if(accessPoint.isFreeWifi()){
                        FWManager.getInstance().connect(accessPoint);
                    } else {
                        DialogActivity.showInuptPWD(getActivity(), accessPoint,false);
                    }
                }
            });

            if(!accessPoint.isConfiged){
                btn_forget.setVisibility(View.GONE);
            }
        }
    }
}
