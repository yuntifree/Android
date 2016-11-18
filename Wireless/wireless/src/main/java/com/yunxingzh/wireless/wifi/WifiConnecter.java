package com.yunxingzh.wireless.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.yunxingzh.wireless.utility.Logg;

import java.util.Comparator;
import java.util.List;

public class WifiConnecter{

    private static final ConfigurationSecurities ConfigSec = ConfigurationSecurities.newInstance();
    private static int numOpenNetWorksKept = -1;

    private static final String TAG = "Connecter";

    public static boolean connectToAccessPoint(final Context ctx, final WifiManager wifiMgr, final AccessPoint ap){
        if(numOpenNetWorksKept < 0)
            numOpenNetWorksKept = Settings.Secure.getInt(ctx.getContentResolver(), Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT, 10);

        if(ap.config != null){
            //connect to a saved wifi
            final WifiConfiguration config = getWifiConfiguration(wifiMgr, ap.config, ap.security);
            if(!TextUtils.isEmpty(ap.password)){
                return changePasswordAndConnect(ctx, wifiMgr, config, ap.password, numOpenNetWorksKept);
            } else {
                return connectToConfiguredNetwork(ctx, wifiMgr, config, true);
            }
        } else if(ap.scanResult != null){
            //connect to a unsaved wifi
            return connectToNewNetwork(ctx, wifiMgr, ap.scanResult, ap.password, numOpenNetWorksKept);
        } else {
            //connect to a hidden wifi
            WifiConfiguration config = new WifiConfiguration();
            config.SSID = convertToQuotedString(ap.ssid);
            if(!TextUtils.isEmpty(ap.bssid)) config.BSSID = ap.bssid;
            ConfigSec.setupSecurity(config, ap.security, ap.password);

            int id = -1;
            try {
                id = wifiMgr.addNetwork(config);
            } catch(NullPointerException e) {
                Logg.e(TAG, "Weird!! Really!! What's wrong??" + e.getMessage());
            }
            if(id == -1) {
                return false;
            }

            if(!wifiMgr.saveConfiguration()) {
                return false;
            }

            config = getWifiConfiguration(wifiMgr, config, ap.security);
            if(config == null) {
                return false;
            }

            return connectToConfiguredNetwork(ctx, wifiMgr, config, true);
        }
    }


    /**
     * Change the password of an existing configured network and connect to it
     * @param wifiMgr
     * @param config
     * @param newPassword
     * @return
     */
    public static boolean changePasswordAndConnect(final Context ctx, final WifiManager wifiMgr, final WifiConfiguration config, final String newPassword, final int numOpenNetworksKept) {
        ConfigSec.setupSecurity(config, ConfigSec.getWifiConfigurationSecurity(config), newPassword);
        final int networkId = wifiMgr.updateNetwork(config);
        if(networkId == -1) {
            // Update failed.
            return false;
        }
        // Force the change to apply.
        wifiMgr.disconnect();
        return connectToConfiguredNetwork(ctx, wifiMgr, config, true);
    }

    /**
     * Configure a network, and connect to it.
     * @param wifiMgr
     * @param scanResult
     * @param password Password for secure network or is ignored.
     * @return
     */
    public static boolean connectToNewNetwork(final Context ctx, final WifiManager wifiMgr, final ScanResult scanResult, final String password, final int numOpenNetworksKept) {
        final String security = ConfigSec.getScanResultSecurity(scanResult);

        if(ConfigSec.isOpenNetwork(security)) {
            checkForExcessOpenNetworkAndSave(wifiMgr, numOpenNetworksKept);
        }

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = convertToQuotedString(scanResult.SSID);
        config.BSSID = scanResult.BSSID;
        ConfigSec.setupSecurity(config, security, password);

        int id = -1;
        try {
            id = wifiMgr.addNetwork(config);
        } catch(NullPointerException e) {
            Logg.e(TAG, "Weird!! Really!! What's wrong??" + e.getMessage());
            // Weird!! Really!!
            // This exception is reported by user to Android Developer Console(https://market.android.com/publish/Home)
        }
        if(id == -1) {
            return false;
        }

        if(!wifiMgr.saveConfiguration()) {
            return false;
        }

        config = getWifiConfiguration(wifiMgr, config, security);
        if(config == null) {
            return false;
        }

        return connectToConfiguredNetwork(ctx, wifiMgr, config, true);
    }

    /**
     * Connect to a configured network.
     * @param wifiManager
     * @param config
     * @param numOpenNetworksKept Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT
     * @return
     */
    public static boolean connectToConfiguredNetwork(final Context ctx, final WifiManager wifiMgr, WifiConfiguration config, boolean reassociate) {
        final String security = ConfigSec.getWifiConfigurationSecurity(config);

        int oldPri = config.priority;
        // Make it the highest priority.
        int newPri = getMaxPriority(wifiMgr) + 1;
        if(newPri > MAX_PRIORITY) {
            newPri = shiftPriorityAndSave(wifiMgr);
            config = getWifiConfiguration(wifiMgr, config, security);
            if(config == null) {
                return false;
            }
        }

        // Set highest priority to this configured network
        config.priority = newPri;
        int networkId = wifiMgr.updateNetwork(config);
        if(networkId == -1) {
            return false;
        }

        // Do not disable others
        if(!wifiMgr.enableNetwork(networkId, false)) {
            config.priority = oldPri;
            return false;
        }

        if(!wifiMgr.saveConfiguration()) {
            config.priority = oldPri;
            return false;
        }

        // We have to retrieve the WifiConfiguration after save.
        config = getWifiConfiguration(wifiMgr, config, security);
        if(config == null) {
            return false;
        }

        ReenableAllApsWhenNetworkStateChanged.schedule(ctx);

        // Disable others, but do not save.
        // Just to force the WifiManager to connect to it.
        if(!wifiMgr.enableNetwork(config.networkId, true)) {
            return false;
        }

        final boolean connect = reassociate ? wifiMgr.reassociate() : wifiMgr.reconnect();
        if(!connect) {
            return false;
        }

        return true;
    }

    private static void sortByPriority(final List<WifiConfiguration> configurations) {
        java.util.Collections.sort(configurations, new Comparator<WifiConfiguration>() {

            @Override
            public int compare(WifiConfiguration object1,
                               WifiConfiguration object2) {
                return object1.priority - object2.priority;
            }
        });
    }

    /**
     * Ensure no more than numOpenNetworksKept open networks in configuration list.
     * @param wifiMgr
     * @param numOpenNetworksKept
     * @return Operation succeed or not.
     */
    private static boolean checkForExcessOpenNetworkAndSave(final WifiManager wifiMgr, final int numOpenNetworksKept) {
        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        sortByPriority(configurations);

        boolean modified = false;
        int tempCount = 0;
        for(int i = configurations.size() - 1; i >= 0; i--) {
            final WifiConfiguration config = configurations.get(i);
            if(ConfigSec.isOpenNetwork(ConfigSec.getWifiConfigurationSecurity(config))) {
                tempCount++;
                if(tempCount >= numOpenNetworksKept) {
                    modified = true;
                    wifiMgr.removeNetwork(config.networkId);
                }
            }
        }
        if(modified) {
            return wifiMgr.saveConfiguration();
        }

        return true;
    }

    private static final int MAX_PRIORITY = 99999;

    private static int shiftPriorityAndSave(final WifiManager wifiMgr) {
        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        sortByPriority(configurations);
        final int size = configurations.size();
        for(int i = 0; i < size; i++) {
            final WifiConfiguration config = configurations.get(i);
            config.priority = i;
            wifiMgr.updateNetwork(config);
        }
        wifiMgr.saveConfiguration();
        return size;
    }

    private static int getMaxPriority(final WifiManager wifiManager) {
        final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        int pri = 0;
        for(final WifiConfiguration config : configurations) {
            if(config.priority > pri) {
                pri = config.priority;
            }
        }
        return pri;
    }

    private static final String BSSID_ANY = "any";

    public static WifiConfiguration getWifiConfiguration(final WifiManager wifiMgr, final ScanResult hotsopt, String hotspotSecurity) {
        final String ssid = convertToQuotedString(hotsopt.SSID);
        if(ssid.length() == 0) {
            return null;
        }

        final String bssid = hotsopt.BSSID;
        if(bssid == null) {
            return null;
        }

        if(hotspotSecurity == null) {
            hotspotSecurity = ConfigSec.getScanResultSecurity(hotsopt);
        }

        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        if(configurations == null) {
            return null;
        }

        for(final WifiConfiguration config : configurations) {
            if(config.SSID == null || !ssid.equals(config.SSID)) {
                continue;
            }
            if(config.BSSID == null || BSSID_ANY.equals(config.BSSID) ||  bssid.equals(config.BSSID)) {
                final String configSecurity = ConfigSec.getWifiConfigurationSecurity(config);
                if(hotspotSecurity.equals(configSecurity)) {
                    return config;
                }
            }
        }
        return null;
    }

    public static WifiConfiguration getWifiConfiguration(final WifiManager wifiMgr, final WifiConfiguration configToFind, String security) {
        final String ssid = configToFind.SSID;
        if(ssid.length() == 0) {
            return null;
        }

        final String bssid = configToFind.BSSID;


        if(security == null) {
            security = ConfigSec.getWifiConfigurationSecurity(configToFind);
        }

        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();

        for(final WifiConfiguration config : configurations) {
            if(config.SSID == null || !ssid.equals(config.SSID)) {
                continue;
            }
            if(config.BSSID == null || BSSID_ANY.equals(config.BSSID) || bssid == null || bssid.equals(config.BSSID)) {
                final String configSecurity = ConfigSec.getWifiConfigurationSecurity(config);
                if(security.equals(configSecurity)) {
                    return config;
                }
            }
        }
        return null;
    }

    public static String convertToQuotedString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }

        final int lastPos = string.length() - 1;
        if(lastPos > 0 && (string.charAt(0) == '"' && string.charAt(lastPos) == '"')) {
            return string;
        }

        return "\"" + string + "\"";
    }

}