package com.quadrobay.qbayApps.networkproject.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.quadrobay.qbayApps.networkproject.TabPages.FifthTabPage;

import java.lang.ref.WeakReference;
import java.net.InetAddress;

public class NetworkSniffTask extends AsyncTask<Void, Void, Void> {

    public interface deviceListCallBack{

        public void onGetDeviceList(int deviceCount);
        public void onStartGetDeviceList();

    }

    public static final String TAG = "NetworkSniffTask";
    public WeakReference<Context> mContextRef;
    public Context context;

    deviceListCallBack cBack;

    int devicesCount = 0;

    public NetworkSniffTask(Context newcontext, deviceListCallBack callback) {
        mContextRef = new WeakReference<>(newcontext);
        context = newcontext;
        cBack = callback;
    }
    @Override
    protected void onPreExecute() {
        cBack.onStartGetDeviceList();

    }

    @Override
    protected void onPostExecute(Void result) {

        cBack.onGetDeviceList(devicesCount+1);

        AppController appController=AppController.getInstance();
        appController.setConnectedDevice(String.valueOf(devicesCount+1));


    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.e(TAG, "Let's sniff the network");
        try {
            //Context context = mContextRef.get();
            if (context != null) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo connectionInfo = wm.getConnectionInfo();
                int ipAddress = connectionInfo.getIpAddress();

                String ip1 = String.format("%d", (ipAddress & 0xff));
                String ip2 = String.format("%d", (ipAddress >> 8 & 0xff));
                String ip3 = String.format("%d", (ipAddress >> 16 & 0xff));
                String ip4 = String.format("%d", (ipAddress >> 24 & 0xff));

                String ipString = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
                Log.e("IP Address", ipString);

                Log.e(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                Log.e(TAG, "ipString: " + String.valueOf(ipString));
                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                Log.e(TAG, "prefix: " + prefix);
                for (int i = 0; i < 255; i++) {
                    String testIp = prefix + String.valueOf(i);
                    InetAddress name = InetAddress.getByName(testIp);
                    String hostName = name.getCanonicalHostName();
                    if (name.isReachable(20)){
                        Log.e(TAG, "Host:" + hostName);
                        Log.e(TAG, "IPAddress:" + name);
                        devicesCount++;
                    }

                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "Well that's not good.", t);
        }
        return null;
    }


}