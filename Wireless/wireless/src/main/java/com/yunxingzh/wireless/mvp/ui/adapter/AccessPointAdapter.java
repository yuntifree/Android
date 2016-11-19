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
import com.yunxingzh.wireless.mvp.model.AccessData;
import com.yunxingzh.wireless.mvp.ui.activity.DialogActivity;
import com.yunxingzh.wireless.utility.Logg;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.WifiState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AccessPointAdapter extends SectionedRecyclerViewAdapter<
        AccessPointAdapter.HeaderViewHolder, AccessPointAdapter.ItemViewHolder, AccessPointAdapter.FooterViewHolder> {
    private static String TAG = "AccessPointAdapter";
    public static int TYPE_CURRENT_AP = 0;
    public static int TYPE_FOCUS_AP = 1;
    public static int TYPE_NOAUTH_AP = 2;

    private Context mContext;

    private List<AccessPoint> mAccessPoints;

    private AccessPoint mCurrentAPoint;
    private WifiState mCurrentState;

    private List<AccessPoint> mFocusPoints;
    private List<AccessPoint> mNoauthPoints;
    private List<Integer> sections;
    private HashMap<String, AccessData> mAccessDatas;

    public AccessPointAdapter(Context context) {
        this.mContext = context;
        mAccessPoints = new ArrayList<AccessPoint>();
        mCurrentAPoint = null;
        mCurrentState = WifiState.UNKOWN;
        mFocusPoints = new ArrayList<AccessPoint>();
        mNoauthPoints = new ArrayList<AccessPoint>();
        sections = new ArrayList<Integer>();
        mAccessDatas = new HashMap<String, AccessData>();
    }

    public void setData(WifiState state, AccessPoint current, List<AccessPoint> accessPoints, boolean forceRefresh){
        if(!forceRefresh && mCurrentState != WifiState.UNKOWN && mCurrentAPoint != null){
            if(mCurrentState == state && mCurrentAPoint.ssid.equals(current.ssid) && mAccessPoints.size() == accessPoints.size()){
                return;
            }
        }
        Logg.d(TAG, "set data: " + accessPoints.size());
        mCurrentState = state;
        mCurrentAPoint = current;
        mAccessPoints = accessPoints;

        refreshData(true);
    }


    private void refreshData(boolean refreshPWD){
        sections.clear();
        mFocusPoints.clear();
        mNoauthPoints.clear();
        for(AccessPoint ap : mAccessPoints){
            if(mCurrentAPoint != null){
                if(mCurrentAPoint.ssid.equals(ap.ssid)) continue;
            }
            if(ap.isOpen() || mAccessDatas.get(ap.ssid) != null || ap.isConfiged ){
                mFocusPoints.add(ap);
            } else {
                mNoauthPoints.add(ap);
            }
        }
        if(mCurrentAPoint != null) sections.add(TYPE_CURRENT_AP);
        if(mFocusPoints.size() > 0) sections.add(TYPE_FOCUS_AP);
        if(mNoauthPoints.size() > 0) sections.add(TYPE_NOAUTH_AP);

        notifyDataSetChanged();

        if(refreshPWD) refreshPassword();
    }

    @Override
    protected int getSectionCount() {
        return sections.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int type = sections.get(section);
        if(type == TYPE_CURRENT_AP){
            return 1;
        } else if(type == TYPE_FOCUS_AP){
            return mFocusPoints.size();
        } else if(type == TYPE_NOAUTH_AP){
            return mNoauthPoints.size();
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
        holder.setSection(type);
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, int section, int position) {
        int type = sections.get(section);
        if(type == TYPE_CURRENT_AP){
            holder.setAccessPoint(mCurrentAPoint, type);
        } else if(type == TYPE_FOCUS_AP){
            holder.setAccessPoint(mFocusPoints.get(position), type);
        } else if(type == TYPE_NOAUTH_AP){
            holder.setAccessPoint(mNoauthPoints.get(position), type);
        }
    }

    protected LayoutInflater getLayoutInflater(){
        return LayoutInflater.from(mContext);
    }

    private void refreshPassword(){
        List<AccessPoint> aps = new ArrayList<AccessPoint>();
        for(AccessPoint ap : mAccessPoints){
            if(!mAccessDatas.containsKey(ap.ssid)){
                mAccessDatas.put(ap.ssid, null);
                aps.add(ap);
            }
        }
        if(aps.size() == 0) return;

        Observable.create(new Observable.OnSubscribe<List<AccessData>>() {
            @Override
            public void call(Subscriber<? super List<AccessData>> subscriber) {
                subscriber.onNext(new ArrayList<AccessData>());
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<AccessData>>() {
            @Override
            public void call(List<AccessData> ads) {
                for(AccessData ad : ads){
                    mAccessDatas.put(ad.ssid, ad);
                }

                refreshData(false);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                Logg.d(TAG, "Error! " + e);
            }
        });
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private Context context;
        private AccessPoint accessPoint;
        private int type;

        private TextView mSSID;
        private TextView mSubtitle;
        private ImageView mIcon;
        private ImageView mSubicon;

        public ItemViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mSSID = (TextView) itemView.findViewById(R.id.tv_ssid);
            mSubtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mSubicon = (ImageView) itemView.findViewById(R.id.iv_subicon);
        }

        public void setAccessPoint(AccessPoint ap, int type){
            this.accessPoint = ap;
            this.type = type;

            mSSID.setText(ap.ssid);

            if(type == TYPE_CURRENT_AP){
                mSubtitle.setVisibility(View.VISIBLE);
                mSubtitle.setText(context.getResources().getString(mCurrentState.resId));
                if(mCurrentState == WifiState.CONNECTED){
                    mSubicon.setVisibility(View.VISIBLE);
                    mSubicon.setImageResource(R.drawable.tag_connected);
                } else {
                    mSubicon.setVisibility(View.GONE);
                }
            } else if(type == TYPE_FOCUS_AP){
                mSubtitle.setVisibility(View.GONE);
                if(!ap.isOpen()){
                    mSubicon.setVisibility(View.VISIBLE);
                    mSubicon.setImageResource(R.drawable.tag_focus);
                } else {
                    mSubicon.setVisibility(View.GONE);
                }
            } else if(type == TYPE_NOAUTH_AP){
                mSubtitle.setVisibility(View.GONE);
                mSubicon.setVisibility(View.GONE);
            }

            int percent = ap.signal.percent();
            int iconId;
            if(percent > 90){
                iconId = type == TYPE_CURRENT_AP || type == TYPE_FOCUS_AP ? R.drawable.signal_free3 : R.drawable.signal_password3;
            } else if(percent > 60){
                iconId = type == TYPE_CURRENT_AP || type == TYPE_FOCUS_AP ? R.drawable.signal_free2 : R.drawable.signal_password2;
            } else {
                iconId = type == TYPE_CURRENT_AP || type == TYPE_FOCUS_AP ? R.drawable.signal_free1 : R.drawable.signal_password1;
            }
            mIcon.setImageResource(iconId);
        }

        @Override
        public void onClick(View v) {
            if(type == TYPE_CURRENT_AP){

            } else if(type == TYPE_FOCUS_AP){
                FWManager.getInstance().connect(accessPoint);
            } else if(type == TYPE_NOAUTH_AP){
                DialogActivity.showInuptPWD(context, accessPoint, false);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //DialogActivity.showDetail(context, accessPoint);
            return false;
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        private Context context;
        private TextView mSection;

        public HeaderViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            mSection = (TextView) itemView.findViewById(R.id.tv_section);
        }

        public void setSection(int type){
            if(type == TYPE_CURRENT_AP){
                mSection.setText(context.getResources().getString(R.string.section_state_connected));
            } else if(type == TYPE_FOCUS_AP){
                mSection.setText(context.getResources().getString(R.string.section_state_freewifi));
            } else if(type == TYPE_NOAUTH_AP){
                mSection.setText(context.getResources().getString(R.string.section_state_password));
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}