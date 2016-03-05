package com.magus.magusutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 网络连接工具类
 */
public final class NetWorkUtil {

	public static boolean isNetworkConnected() {
		return isNetworkConnected(ContextUtil.context);
	}

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable()&& mNetworkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 为了兼容底层网络addRequest的逻辑，增加此方法，请求前最好两个方法同时调用来判断是否有网络
     * 1)isNetworkConnected(Context)
     * 2)isNetworkValid()
     */
	public static boolean isNetworkValid() {
        //重构架构，先注释掉下面，使用自己的方法判断
        //Comm模块中的类com.eastmoney.android.network.net.EmNetHelper
//        if (!EmNetHelper.sHasInitNetwork && !EmNetHelper.sHasNetworkNow){
//            return false;
//        }
		return isNetworkConnected();
	}
    /**
     * true 有wifi
     *
     */
    public static boolean isWifiEnabled() {
        WifiManager mWifiManager = (WifiManager) ContextUtil.context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
//            Logger.i("**** WIFI is on");
            return true;
        } else {
//            Logger.i("**** WIFI is off");
            return false;
        }
    }
}
