package com.yunxingzh.wireless.wifi;

import com.yunxingzh.wireless.R;

public enum WifiState {
    UNKOWN(R.string.state_unkown),
    DISABLED(R.string.state_disabled),
    IDLE(R.string.state_idle),
    WAITING_DISCONNECT_LAST(R.string.state_disconnect_last),
    WAITING_SERVER_CONFIRM(R.string.state_server_confirm),
    CONNECTING(R.string.state_connecting),
    CONNECTING_AUTH(R.string.state_auth),
    CONNECTING_IPADDR(R.string.state_ipaddr),
    LOGINING(R.string.state_logining),
    CHECKING(R.string.state_checking),
    CONNECTED(R.string.section_state_connected),
    OFFLINEING(R.string.state_offlining),
    DISCONNECTED(R.string.state_disconnected),

    AUTH_ERROR(R.string.state_auth_error);

    public final int resId;

    WifiState(final int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static WifiState parse(int ordinal){
        if (ordinal < 0 || ordinal >= values().length) {
            return UNKOWN;
        }
        return values()[ordinal];
    }

}