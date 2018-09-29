package com.quadrobay.qbayApps.networkproject.TabPages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.SignalStrength;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quadrobay.qbayApps.networkproject.ActivityClasses.MainActivity;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DataGetterSetterClass;
import com.quadrobay.qbayApps.networkproject.R;
import com.github.anastr.speedviewlib.PointerSpeedometer;

import java.util.List;

/**
 * Created by benchmark on 03/10/17.
 */

public class SecondTabPage extends Fragment {

    //private GoogleMap gmap;
    WifiManager wifimanager;
    WifiInfo wifiInfo;
    DhcpInfo dhcpinfo;


    boolean mode=true;
    ImageView advancemodeimg;

    LinearLayout wifilayoutone,wifilayouttwo,wifilayoutthree,wifilayout5;

    DataGetterSetterClass dataGetterSetterClass;

    WifiReceiver mWifiReceiver;
    List<ScanResult> wifiList;

    int rssi;
    int level;
    String wifissid;
    String wifibssid;
    int wifilinkspeed;
    String wifimacaddress;
    int wififrequency;
    String wifiipaddress;
    int wifinetworkid;
    String wifidns1;
    String  wifidns2;
    String wifigateway;
    String wifinetmask;
    int asufinal;

    int qualitypercent;
    ImageView asu_strength,dbm_strength;
    WifiManager wifiManager;

   TextView mode_Txt_view,wifileveltextview,ssidtextview,bssidtextview,speedtextview,frequencytextview,strengthtextview,mactextview,iptextview,netidtextview,dns1textview,dns2textview,gatewaytextview,netmasktextview;

    ImageView wifisignalimgview;
    TextView wifidbmtxtview;
    TextView wifiasutxtview;
    TextView wifirangetext;

    public SecondTabPage () {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        MainActivity cl = (MainActivity)getActivity();
        dataGetterSetterClass = cl.getDataGetterSetterClass();

        wifimanager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifimanager.getConnectionInfo();
        dhcpinfo = wifimanager.getDhcpInfo();

        mWifiReceiver = new WifiReceiver();
        IntentFilter mIntentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        getActivity().registerReceiver(mWifiReceiver, mIntentFilter);
        wifimanager.startScan();

        rssi = wifimanager.getConnectionInfo().getRssi();
        level = WifiManager.calculateSignalLevel(rssi, 5);
        wifissid = wifiInfo.getSSID();
        wifibssid = wifiInfo.getBSSID();
        wifilinkspeed = wifiInfo.getLinkSpeed();
        wifimacaddress = wifiInfo.getMacAddress();
        wififrequency = wifiInfo.getFrequency();
        int ipAddress = wifiInfo.getIpAddress();
        wifiipaddress= String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        wifinetworkid = wifiInfo.getNetworkId();
        int  wifidns11 = dhcpinfo.dns1;
        wifidns1=    String.format("%d.%d.%d.%d", (wifidns11 & 0xff),(wifidns11 >> 8 & 0xff),(wifidns11 >> 16 & 0xff),(wifidns11 >> 24 & 0xff));
        int dns22 = dhcpinfo.dns2;
        wifidns2=    String.format("%d.%d.%d.%d", (dns22 & 0xff),(dns22 >> 8 & 0xff),(dns22>> 16 & 0xff),(dns22 >> 24 & 0xff));
        int gatew=dhcpinfo.gateway;
        wifigateway=    String.format("%d.%d.%d.%d", (gatew & 0xff),(gatew >> 8 & 0xff),(gatew>> 16 & 0xff),(gatew >> 24 & 0xff));
        wifinetmask = Formatter.formatIpAddress( dhcpinfo.netmask);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

    }
    public int getWifiSignalStrengthIndBm() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return asu2dBm(wifiInfo.getRssi());
    }
    private int asu2dBm(int asu) {

       this.asufinal=asu;
        return (2 * asu) - 113;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.second_tab_page, container, false);
        String deviceDensity = "";
        switch (getActivity().getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                deviceDensity =  0.75 + " ldpi";
                Log.e("Dimension",deviceDensity);
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                deviceDensity =  1.0 + " mdpi";
                Log.e("Dimension",deviceDensity);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                deviceDensity =  1.5 + " hdpi";
                Log.e("Dimension",deviceDensity);
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                deviceDensity =  2.0 + " xhdpi";
                Log.e("Dimension",deviceDensity);
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                deviceDensity =  3.0 + " xxhdpi";
                Log.e("Dimension",deviceDensity);
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                deviceDensity =  4.0 + " xxxhdpi";
                Log.e("Dimension",deviceDensity);
                break;
            default:
                deviceDensity = "Not found";
        }
        wifisignalimgview = (ImageView) lay.findViewById(R.id.strengthimg);
        wifidbmtxtview = (TextView) lay.findViewById(R.id.wifi_dbm_txtview);
        wifiasutxtview = (TextView) lay.findViewById(R.id.wifi_asu_txtview);
        wifileveltextview = (TextView) lay.findViewById(R.id.WifiLevelTextView);
        ssidtextview = (TextView) lay.findViewById(R.id.WifiSSIDTextView);
        bssidtextview = (TextView) lay.findViewById(R.id.WifiBssiTextView);
        speedtextview = (TextView) lay.findViewById(R.id.WifiSpeedTextView);
        strengthtextview = (TextView) lay.findViewById(R.id.WifiStrengthTextView);
        frequencytextview = (TextView) lay.findViewById(R.id.WifiFrequencyTextView);
        mactextview = (TextView) lay.findViewById(R.id.WifiMacAddressTextView);
        iptextview = (TextView) lay.findViewById(R.id.WifiIpAddressTextView);
        netidtextview = (TextView) lay.findViewById(R.id.WifiNetIdTextView);
        dns1textview = (TextView) lay.findViewById(R.id.DNS1TextView);
        dns2textview = (TextView) lay.findViewById(R.id.DNS2TextView);
        gatewaytextview = (TextView) lay.findViewById(R.id.GateWayTextView);
        netmasktextview = (TextView) lay.findViewById(R.id.NetMaskTextView);
        wifirangetext=lay.findViewById(R.id.wifi_range);
        mode_Txt_view=lay.findViewById(R.id.mode_text_view);
        advancemodeimg=lay.findViewById(R.id.normalmodeimg);
        wifilayoutone=lay.findViewById(R.id.wifilayout1);
        wifilayouttwo=lay.findViewById(R.id.wifilayout2);
        wifilayoutthree=lay.findViewById(R.id.wifilayout3);
        wifilayout5=lay.findViewById(R.id.wifilayout4);

        advancemodeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (mode){
                    mode=false;
                    mode_Txt_view.setText("Advance mode");
                    wifilayoutone.setVisibility(View.VISIBLE);
                    wifilayouttwo.setVisibility(View.VISIBLE);
                    wifilayoutthree.setVisibility(View.VISIBLE);
                    wifilayout5.setVisibility(View.VISIBLE);
                    advancemodeimg.setImageDrawable(getActivity().getDrawable(R.drawable.advancedmode));
                    }else {
                    mode=true;
                    wifilayoutone.setVisibility(View.INVISIBLE);
                    wifilayouttwo.setVisibility(View.INVISIBLE);
                    wifilayoutthree.setVisibility(View.GONE);
                    wifilayout5.setVisibility(View.GONE);
                    advancemodeimg.setImageDrawable(getActivity().getDrawable(R.drawable.normalmode));
                    mode_Txt_view.setText("Normal mode");
                }
            }
        });

        asu_strength=lay.findViewById(R.id.wifiasu_strength);
        dbm_strength=lay.findViewById(R.id.wifidbm_strength);
        if (wifimanager.isWifiEnabled()){
            ssidtextview.setText(": "+wifissid);
            bssidtextview.setText(": "+wifibssid);
            speedtextview.setText(": "+wifilinkspeed);
            wifirangetext.setText(String.valueOf(qualitypercent));
            strengthtextview.setText(": " + rssi + "dBm");
            frequencytextview.setText(": "+wififrequency);
            frequencytextview.setText(": "+wififrequency);
            mactextview.setText(": "+wifimacaddress);
            iptextview.setText(": "+wifiipaddress);
            netidtextview.setText(": "+wifinetworkid);
            dns1textview.setText(": " + wifidns1);
            dns2textview.setText(": " + wifidns2);
            gatewaytextview.setText(": " + wifigateway);
            netmasktextview.setText(": " + wifinetmask);
            wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            int speed=qualitypercent;
            if (speed>0 && speed<5){
                wifisignalimgview.setImageResource(R.drawable.wifione);
            } else if (speed>5 && speed<=30){
                wifisignalimgview.setImageResource(R.drawable.wifithree);
            }
            else if (speed>30 && speed<=45){
                wifisignalimgview.setImageResource(R.drawable.wififour);
            }
            else if (speed>45 && speed<=56){
                wifisignalimgview.setImageResource(R.drawable.wififive);
            }else if (speed>56 && speed<=70){
                wifisignalimgview.setImageResource(R.drawable.wifisix);
            }
            else if (speed>70 && speed<=80){
                wifisignalimgview.setImageResource(R.drawable.wifieigth);
            }
            else if (speed>80 && speed<=90){
                wifisignalimgview.setImageResource(R.drawable.wifinine);
            }else  if (speed>90&& speed<=100){
                wifisignalimgview.setImageResource(R.drawable.wifiten);
            }
            wifidbmtxtview.setText(getWifiSignalStrengthIndBm()+"");
            wifiasutxtview.setText(String.valueOf(asufinal));
            switch (level){
                case 0:
                    wifileveltextview.setText("WIFI signal level very poor");
                    dbm_strength.setImageResource(R.drawable.onestrengthsignal);
                    asu_strength.setImageResource(R.drawable.onestrengthsignal);
                 break;
                case 1:
                    wifileveltextview.setText("WIFI signal level poor");
                    dbm_strength.setImageResource(R.drawable.twostrengthsignal);
                    asu_strength.setImageResource(R.drawable.twostrengthsignal);
                  break;
                case 2:
                    wifileveltextview.setText("WIFI signal level Moderate");
                    dbm_strength.setImageResource(R.drawable.threestrengthsignal);
                    asu_strength.setImageResource(R.drawable.threestrengthsignal);
                  break;
                case 3:
                    wifileveltextview.setText("WIFI signal level Good");
                    dbm_strength.setImageResource(R.drawable.fourstrengthsignal);
                    asu_strength.setImageResource(R.drawable.fourstrengthsignal);
                  break;
                case 4:
                    wifileveltextview.setText("WIFI signal level Excellent");
                    dbm_strength.setImageResource(R.drawable.fullstrengthsignal);
                    asu_strength.setImageResource(R.drawable.fullstrengthsignal);
                  break;
            }

            int newrssi = wifimanager.getConnectionInfo().getRssi();
            if(newrssi <= -100) {
                qualitypercent = 0;
            }else if(newrssi >= -50) {
                qualitypercent = 100;
            }else {
                qualitypercent = 2 * (newrssi + 100);
            }
        }else {
            wifileveltextview.setText("WIFI is in OFF condition");
        }
        return lay;
    }

    public void onPause() {
        getActivity().unregisterReceiver(mWifiReceiver);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        super.onPause();
    }

    public void onResume() {
        getActivity().registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        super.onResume();
    }

    class WifiReceiver extends BroadcastReceiver {
        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
            int state = wifimanager.getWifiState();
            int maxLevel = 5;
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();
                } else {
            }
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
            if (state == WifiManager.WIFI_STATE_ENABLED) {
                // Get Scanned results in an array List
                wifiList = wifimanager.getScanResults();
                // Iterate on the list
                for (ScanResult result : wifiList) {
                    //The level of each wifiNetwork from 0-5
                    int level = WifiManager.calculateSignalLevel(
                            result.level, maxLevel);

                    int newrssi = wifimanager.getConnectionInfo().getRssi();

                    if (newrssi <= -100) {
                        qualitypercent = 0;
                    } else if (newrssi >= -50) {
                        qualitypercent = 100;
                    } else {
                        qualitypercent = 2 * (newrssi + 100);
                    }
                    int newlevel = WifiManager.calculateSignalLevel(newrssi, 5);
                 //   wifidbmtxtview.setText(String.valueOf(newrssi));
                    int asu2 = newrssi;

                    Log.e("asu signal 1", "" + asu2);

                     strengthtextview.setText(": " + qualitypercent + "% (" + newrssi + " dbm)");
                    wifirangetext.setText(String.valueOf(qualitypercent));
                    int speed = qualitypercent;

                    if (speed > 0 && speed < 5) {
                        wifisignalimgview.setImageResource(R.drawable.wifione);
                    } else if (speed > 5 && speed <= 30) {
                        wifisignalimgview.setImageResource(R.drawable.wifithree);
                    } else if (speed > 30 && speed <= 45) {
                        wifisignalimgview.setImageResource(R.drawable.wififour);
                    } else if (speed > 45 && speed <= 56) {
                        wifisignalimgview.setImageResource(R.drawable.wififive);
                    } else if (speed > 56 && speed <= 70) {
                        wifisignalimgview.setImageResource(R.drawable.wifisix);
                    } else if (speed > 70 && speed <= 80) {
                        wifisignalimgview.setImageResource(R.drawable.wifieigth);
                    } else if (speed > 80 && speed <= 90) {
                        wifisignalimgview.setImageResource(R.drawable.wifinine);
                    } else if (speed > 90 && speed <= 100) {
                        wifisignalimgview.setImageResource(R.drawable.wifiten);
                    }
                    switch (level) {
                        case 0:
                            wifileveltextview.setText("WIFI signal level very poor");
                            // wifisignalimgview.setImageResource(R.drawable.wifi0);
                            break;
                        case 1:
                            wifileveltextview.setText("WIFI signal level poor");

                            break;
                        case 2:
                            wifileveltextview.setText("WIFI signal level Moderate");
                            //  wifisignalimgview.setImageResource(R.drawable.wifi2);
                            break;
                        case 3:
                            wifileveltextview.setText("WIFI signal level Good");
                            //  wifisignalimgview.setImageResource(R.drawable.wifi4);
                            break;
                        case 4:
                            wifileveltextview.setText("WIFI signal level Excellent");
                            //   wifisignalimgview.setImageResource(R.drawable.wifi5);
                            break;
                    }
                    wifissid = wifiInfo.getSSID();
                    wifibssid = wifiInfo.getBSSID();
                    wifilinkspeed = wifiInfo.getLinkSpeed();
                    wifimacaddress = wifiInfo.getMacAddress();
                    wififrequency = wifiInfo.getFrequency();
                    int ipAddress = wifiInfo.getIpAddress();
                    wifiipaddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
                    wifinetworkid = wifiInfo.getNetworkId();
                    int wifidns11 = dhcpinfo.dns1;
                    wifidns1 = String.format("%d.%d.%d.%d", (wifidns11 & 0xff), (wifidns11 >> 8 & 0xff), (wifidns11 >> 16 & 0xff), (wifidns11 >> 24 & 0xff));
                    int dns22 = dhcpinfo.dns2;
                    wifidns2 = String.format("%d.%d.%d.%d", (dns22 & 0xff), (dns22 >> 8 & 0xff), (dns22 >> 16 & 0xff), (dns22 >> 24 & 0xff));
                    int gatew = dhcpinfo.gateway;
                    wifigateway = String.format("%d.%d.%d.%d", (gatew & 0xff), (gatew >> 8 & 0xff), (gatew >> 16 & 0xff), (gatew >> 24 & 0xff));
                    wifinetmask = Formatter.formatIpAddress(dhcpinfo.netmask);
                    ssidtextview.setText(": " + wifissid);
                    bssidtextview.setText(": " + wifibssid);
                    speedtextview.setText(": " + wifilinkspeed);
                    frequencytextview.setText(": " + wififrequency);
                    mactextview.setText(": " + wifimacaddress);
                    iptextview.setText(": " + wifiipaddress);
                    netidtextview.setText(": " + wifinetworkid);
                    dns1textview.setText(": " + wifidns1);
                    dns2textview.setText(": " + wifidns2);
                    gatewaytextview.setText(": " + wifigateway);
                    netmasktextview.setText(": " + wifinetmask);

                    String SSID = result.SSID;
                    String capabilities = result.capabilities;
                    // TODO add your own code.

                }
            }
        }
        }
    }

}
