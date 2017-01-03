package com.yunxingzh.wireless.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.activity.DialogActivity;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.WifiState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wireless.libs.bean.resp.WifiList;
import wireless.libs.bean.vo.AccessData;
import wireless.libs.bean.vo.WifiInfoVo;

public class AccessPointAdapter extends SectionedRecyclerViewAdapter<
        AccessPointAdapter.HeaderViewHolder, AccessPointAdapter.ItemViewHolder, AccessPointAdapter.FooterViewHolder> {
    private static String TAG = "AccessPointAdapter";
    public final static int TYPE_CURRENT_AP = 0;//当前连接的wifi
    public final static int TYPE_FOCUS_AP = 1;//已经配置好的wifi
    public final static int TYPE_NOAUTH_AP = 2;//需要密码连接的wifi
    public final static int TYPE_OTHER_AP = 3; //除了当前WiFi外的

    private Context mContext;

    private List<AccessPoint> mAccessPoints;

    private AccessPoint mCurrentAPoint;
    private WifiState mCurrentState;

    private List<AccessPointEx> mFocusPoints;
    private List<AccessPointEx> mNoauthPoints;
    private List<AccessPointEx> mOtherPoints;
    private List<Integer> sections;
    private HashMap<String, AccessData> mAccessDatas;
    private List<WifiInfoVo> mWifiInfoList;//服务器获取的附近wifi列表


    public class AccessPointEx {
        public int type;
        public AccessPoint ap;

        public AccessPointEx(AccessPoint ap, int type) {
            this.ap = ap;
            this.type = type;
        }
    }

    public AccessPointAdapter(Context context) {
        this.mContext = context;
        mAccessPoints = new ArrayList<AccessPoint>();
        mCurrentAPoint = null;
        mCurrentState = WifiState.UNKOWN;
        mFocusPoints = new ArrayList<AccessPointEx>();
        mNoauthPoints = new ArrayList<AccessPointEx>();
        mOtherPoints = new ArrayList<AccessPointEx>();
        sections = new ArrayList<Integer>();
        mAccessDatas = new HashMap<String, AccessData>();
    }

    public void setData(WifiState state, AccessPoint current, List<AccessPoint> accessPoints, List<WifiInfoVo> mWifiInfos, boolean forceRefresh) {
        if (!forceRefresh && mCurrentState != WifiState.UNKOWN && mCurrentAPoint != null) {
            if (mCurrentState == state && mCurrentAPoint.ssid.equals(current.ssid) && mAccessPoints.size() == accessPoints.size()) {
                return;
            }
        }
        if (mWifiInfos != null) {
            mWifiInfoList = mWifiInfos;
        }

        mCurrentState = state;
        mCurrentAPoint = current;
        mAccessPoints = accessPoints;

        // 判断本次改变的SSID,避免多次请求服务器
        ArrayList<String> uploadList = new ArrayList<String>();
        int apType;
        for (AccessPoint ap : mAccessPoints) {
            if (!mAccessDatas.containsKey(ap.ssid)) {
                mAccessDatas.put(ap.ssid, null);
                apType = checkAPType(ap);
                // 搜集需要提交的ap
                if (apType == TYPE_NOAUTH_AP) {
                    uploadList.add(ap.ssid);
                }
            }
        }

        // 如果有改变，服务器端比较
//        if (uploadList.size() > 0) {
//            // TODO:此处进行服务器比较, 完成后 refreshData
//            String longitude = "";
//            String latitude = "";
//            String[] ssids = null;
//            if (uploadList != null) {
//                ssids = new String[uploadList.size()];
//                for (int i = 0; i < uploadList.size(); i++) {
//                    ssids[i] = uploadList.get(i);
//                }
//                longitude = SPUtils.get(mContext, "longitude", "");
//                latitude = SPUtils.get(mContext, "latitude", "");
//            }
//
//            String jsonStr = JsonUtils.jsonStirngForWifi(MainApplication.sApplication.getUser().getData().getUid(),
//                    MainApplication.sApplication.getToken(),
//                    0, Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
//                    StringUtils.getCurrentTime(), AppUtils.getNetWorkType(MainApplication.sApplication), Double.parseDouble(longitude), Double.parseDouble(latitude), ssids);
//            OkRequestParams params = new OkRequestParams();
//            params.put("key", jsonStr);
//            OkHttpUtil.post(Api.GET_WIFI_LIST, params, new OkHttpResBeanHandler<WifiVo>() {
//                @Override
//                public void onSuccess(int code, Headers headers, WifiVo response) {
//                    if (response.getErrno() == HttpCode.HTTP_OK) {
//                        List<WifiVo.WifiData.MWifiInfo> mWifiList = response.getData().getWifipass();
//                        if (mWifiList != null) {
//                            for (AccessPoint ap : mAccessPoints) {
//                                for (WifiVo.WifiData.MWifiInfo item : mWifiList) {
//                                    if (ap.ssid.equals(item.getSsid())) {
//                                        ap.setPassword(item.getPass(), AccessPoint.PasswordFrom.CLOUD);
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        //   listener.onGetWifiFailed(response.getErrno());
//                    }
//                }
//
//                @Override
//                public void onFailure(int code, Headers headers, int error, Throwable t) {
//                    //   listener.onGetWifiFailed(error);
//                }
//            });
//
//            refreshData(true);
//        } else {
//            refreshData(true);
//        }
        refreshData(true);
    }

    private int checkAPType(AccessPoint ap) {
        if (mCurrentAPoint != null) {
            if (mCurrentAPoint.ssid.equals(ap.ssid)) return TYPE_CURRENT_AP;
        }
        if (ap.isOpen() || mAccessDatas.get(ap.ssid) != null || ap.isConfiged) {
            return TYPE_FOCUS_AP;
        } else {
            return TYPE_NOAUTH_AP;
        }
    }

    private void refreshData(boolean refreshPWD) {
        sections.clear();
        mFocusPoints.clear();
        mNoauthPoints.clear();
        mOtherPoints.clear();
        int apType;
        for (AccessPoint ap : mAccessPoints) {
            apType = checkAPType(ap);
            switch (apType) {
                case TYPE_CURRENT_AP:
                    break;
                case TYPE_FOCUS_AP:
                    mFocusPoints.add(new AccessPointEx(ap, TYPE_FOCUS_AP));
                    break;
                case TYPE_NOAUTH_AP:
                    mNoauthPoints.add(new AccessPointEx(ap, TYPE_NOAUTH_AP));
                    break;
                default:
                    break;
            }
        }
        mOtherPoints.addAll(mFocusPoints);
        mOtherPoints.addAll(mNoauthPoints);

        if (mCurrentAPoint != null) sections.add(TYPE_CURRENT_AP);
        if (mOtherPoints.size() > 0) sections.add(TYPE_OTHER_AP);

        //if (mFocusPoints.size() > 0) sections.add(TYPE_FOCUS_AP);
        //if (mNoauthPoints.size() > 0) sections.add(TYPE_NOAUTH_AP);

        notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        return sections.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int type = sections.get(section);
        if (type == TYPE_CURRENT_AP) {
            return 1;
//        } else if (type == TYPE_FOCUS_AP) {
//            return mFocusPoints.size();
//        } else if (type == TYPE_NOAUTH_AP) {
//            return mNoauthPoints.size();
        } else if (type == TYPE_OTHER_AP) {
            return mOtherPoints.size();
        }
        return 0;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_ap_section, parent, false);
        return new HeaderViewHolder(view, mContext);
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return new FooterViewHolder(new FrameLayout(mContext));
    }

    @Override
    protected ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_ap, parent, false);
        return new ItemViewHolder(view, mContext);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        int type = sections.get(section);
        holder.setWifiCount(mOtherPoints.size(), type);
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, int section, int position) {
        int type = sections.get(section);
        if (type == TYPE_CURRENT_AP) {
            holder.setLayerVisible(true);
            holder.setAccessPoint(mCurrentAPoint, type);
//        } else if (type == TYPE_FOCUS_AP) {
//            holder.setAccessPoint(mFocusPoints.get(position), type);
//        } else if (type == TYPE_NOAUTH_AP) {
//            holder.setAccessPoint(mNoauthPoints.get(position), type);
        } else if (type == TYPE_OTHER_AP) {
            AccessPointEx ap = mOtherPoints.get(position);
            holder.setLayerVisible(false);
            holder.setAccessPoint(ap.ap, ap.type);
        }
    }


    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private Context context;
        private AccessPoint accessPoint;
        private int type;

        private TextView mSSID;
        private TextView mSubtitle;
        private ImageView mIcon;
        private ImageView mSubicon;
        private View mWifiItemLay;

        public ItemViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mWifiItemLay = itemView.findViewById(R.id.place_holder);
            mSSID = (TextView) itemView.findViewById(R.id.tv_ssid);
            mSubtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mSubicon = (ImageView) itemView.findViewById(R.id.iv_subicon);
        }

        public void setLayerVisible(boolean visible) {
            mWifiItemLay.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        public void setAccessPoint(AccessPoint ap, int type) {
            this.accessPoint = ap;
            this.type = type;
            int percent = ap.signal.percent();
            mSSID.setText(ap.ssid);

            if (type == TYPE_CURRENT_AP) {
                mSubtitle.setVisibility(View.VISIBLE);
                mSubtitle.setText(context.getResources().getString(mCurrentState.resId));
                if (mCurrentState == WifiState.CONNECTED) {//当前状态-已经连接
                    mSubicon.setVisibility(View.VISIBLE);
                    mSubicon.setImageResource(R.drawable.connected);
                } else {
                    mSubicon.setVisibility(View.GONE);
                }
            } else if (type == TYPE_FOCUS_AP) {
                mSubtitle.setVisibility(View.VISIBLE);
                if (!ap.isOpen()) {
                    mSubicon.setVisibility(View.GONE);
                    mSubtitle.setText(R.string.configed_wifi);
                } else {
                    mSubicon.setVisibility(View.GONE);
                    mSubtitle.setText(R.string.free_wifi);
                }
            } else if (type == TYPE_NOAUTH_AP) {
                mSubtitle.setText(R.string.need_pwd);
                mSubicon.setVisibility(View.GONE);
            }

            int iconId;
            if (percent > 90) {
                iconId = type == TYPE_CURRENT_AP || type == TYPE_FOCUS_AP ? R.drawable.ico_wifi_small4 : R.drawable.ico_wifi_code4;
            } else if (percent > 60) {
                iconId = type == TYPE_CURRENT_AP || type == TYPE_FOCUS_AP ? R.drawable.ico_wifi_small3 : R.drawable.ico_wifi_code3;
            } else if (percent > 30) {
                iconId = type == TYPE_CURRENT_AP || type == TYPE_FOCUS_AP ? R.drawable.ico_wifi_small2 : R.drawable.ico_wifi_code2;
            } else {
                iconId = type == TYPE_CURRENT_AP || type == TYPE_FOCUS_AP ? R.drawable.ico_wifi_small1 : R.drawable.ico_wifi_code1;
            }
            mIcon.setImageResource(iconId);
        }

        @Override
        public void onClick(View v) {
            if (type == TYPE_CURRENT_AP) {
                // do nothing
            } else if (type == TYPE_FOCUS_AP) {
                FWManager.getInstance().connect(accessPoint);
            } else if (type == TYPE_NOAUTH_AP) {
                DialogActivity.showInuptPWD(context, accessPoint, false);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            DialogActivity.showDetail(context, accessPoint);
            return false;
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView mToptitle;

        public HeaderViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            mToptitle = (TextView) itemView.findViewById(R.id.tv_section);
        }

        //显示wifi个数
        public void setWifiCount(int wifiCount, int type) {
            if (type == TYPE_OTHER_AP) {
                mToptitle.setText("扫描到附近共有" + wifiCount + "个WiFi");
            } else if (type == TYPE_CURRENT_AP) {
                mToptitle.setVisibility(View.GONE);
//            } else if (type == TYPE_NOAUTH_AP) {//需要密码连接
//                mToptitle.setVisibility(View.GONE);
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
