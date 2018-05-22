package com.example.hatuan.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Adminstrator on 1/23/2018.
 */

public class CheckConnection {
    public static boolean haveNetworkConnection(Context context){
        boolean haveConnectWifi = false;
        boolean haveConnectMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni: netInfo){
            if (ni.getTypeName().equalsIgnoreCase("WIFI")){
                if(ni.isConnected()){
                    haveConnectWifi = true;
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
                if(ni.isConnected()){
                    haveConnectMobile = true;
                }
            }
        }
        return haveConnectWifi || haveConnectMobile;
    }

    public static void ShowToast_Short(Context context, String thongbao){
        Toast.makeText(context, thongbao, Toast.LENGTH_SHORT).show();
    }
}
