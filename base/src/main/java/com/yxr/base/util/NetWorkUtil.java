package com.yxr.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * @author ciba
 * @description 描述
 * @date 2020/09/17
 */
public class NetWorkUtil {
    /**
     * 获取网络类型
     */
    @SuppressLint("MissingPermission")
    public static NetworkType getCurrentNetType(Context context) {
        try {
            // 获取网络服务
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null == connManager) {
                // 为空则认为无网络
                return NetworkType.NETWORK_UNKNOWN;
            }
            // 获取网络类型，如果为空，返回无网络
            NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
            if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
                return NetworkType.NETWORK_UNKNOWN;
            }
            // 判断是否为WIFI
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != wifiInfo) {
                NetworkInfo.State state = wifiInfo.getState();
                if (null != state) {
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        return NetworkType.NETWORK_WIFI;
                    }
                }
            }
            // 若不是WIFI，则去判断是2G、3G、4G网
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return NetworkType.NETWORK_UNKNOWN;
            }
            int networkType = telephonyManager.getNetworkType();
            switch (networkType) {
            /*
             GPRS : 2G(2.5) General Packet Radia Service 114kbps
             EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
             UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
             CDMA : 2G 电信 Code Division Multiple Access 码分多址
             EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
             EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
             1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
             HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
             HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
             HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
             IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
             EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
             LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
             EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
             HSPAP : 3G HSPAP 比 HSDPA 快些
             */
                // 2G网络
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NetworkType.NETWORK_2G;
                // 3G网络
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NetworkType.NETWORK_3G;
                // 4G网络
                case TelephonyManager.NETWORK_TYPE_LTE:
                case 19:
                    return NetworkType.NETWORK_4G;
                // 5G
                case 20:
                    return NetworkType.NETWORK_5G;
                default:
                    return NetworkType.NETWORK_UNKNOWN;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return NetworkType.NETWORK_UNKNOWN;
    }

    @SuppressLint("MissingPermission")
    public static boolean isWifi(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
            return info != null && ConnectivityManager.TYPE_WIFI == info.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static final String NETWORK_WIFI = "WIFI";
    public static final String NETWORK_2G = "2G";
    public static final String NETWORK_3G = "3G";
    public static final String NETWORK_4G = "4G";
    public static final String NETWORK_5G = "5G";

    public enum NetworkType {
        NETWORK_UNKNOWN("UNKNOWN"),
        NETWORK_WIFI("WIFI"),
        NETWORK_2G("2G"),
        NETWORK_3G("3G"),
        NETWORK_4G("4G"),
        NETWORK_5G("5G");

        private final String networkType;

        NetworkType(String networkType) {
            this.networkType = networkType;
        }

        public String getNetworkType() {
            return networkType;
        }
    }
}
