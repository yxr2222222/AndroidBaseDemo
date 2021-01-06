package com.yxr.base.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * @author ciba
 * @date 2020/11/18
 * @description 设备信息工具类
 */

public class DeviceUtil {
    private static final String DEFAULT_MAC = "02:00:00:00:00:00";

    public static String getImei(Context context) {
        return getImei(context, true);
    }

    /**
     * 获取Imei值，安卓10及其之后无法获取到Imei
     *
     * @param context            上下文
     * @param isCanUsePhoneState 是否允许获取，不允许直接返回空
     * @return Imei值
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImei(Context context, boolean isCanUsePhoneState) {
        if (isCanUsePhoneState && Build.VERSION.SDK_INT < 29) {
            try {
                int per = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
                if (per == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    String imei = null;
                    if (tm != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            try {
                                imei = tm.getImei();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (TextUtils.isEmpty(imei)) {
                            try {
                                imei = tm.getDeviceId();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return imei;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取AndroidId
     */
    public static String getAndroidId(Context context) {
        try {
            @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(androidId)) {
                androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            }
            return androidId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    public static boolean isWifi(Context context) {
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return (wm != null && WifiManager.WIFI_STATE_ENABLED == wm.getWifiState());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取MAC地址
     */
    public static String getMacAddress(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getMacAddressAboveVersionM();
        }
        return getMacAddressBelowVersionM(context);
    }

    /**
     * 安卓7.0以下获取Mac方式
     */
    private static String getMacAddressBelowVersionM(Context context) {
        try {
            int per = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE);
            if (per == PackageManager.PERMISSION_GRANTED) {
                WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifi == null) {
                    return DEFAULT_MAC;
                }
                @SuppressLint("MissingPermission") WifiInfo info = wifi.getConnectionInfo();
                if (info == null) {
                    return DEFAULT_MAC;
                }
                @SuppressLint("HardwareIds")
                String mac = info.getMacAddress();
                if (TextUtils.isEmpty(mac)) {
                    mac = DEFAULT_MAC;
                }
                return mac;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_MAC;
    }

    /**
     * 获取Mac地址 7.0以上
     */
    private static String getMacAddressAboveVersionM() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!"wlan0".equalsIgnoreCase(nif.getName())) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_MAC;
    }
}
