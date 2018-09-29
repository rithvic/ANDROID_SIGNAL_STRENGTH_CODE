package com.quadrobay.qbayApps.networkproject.Services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DataGetterSetterClass;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DatabaseHandlerClass;
import com.quadrobay.qbayApps.networkproject.R;
import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.quadrobay.qbayApps.networkproject.SplashScreen;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by benchmark on 03/11/17.
 */

public class JobSchedulerService extends JobService implements LocationListener {

    TelephonyManager mTelephonyManager;
    int mSignalStrength = 0;
    Boolean quitlooper;
    SharedPreferences sharedPreferences;
    int Userthresold;
    public static final int THRESHOLD_VERY_POOR = 0;
    public static final int THRESHOLD_POOR = 1;
    public static final int THRESHOLD_INTERMEDIATE = 2;
    public static final int THRESHOLD_GOOD = 3;
    public static final int THRESHOLD_EXCELLENT = 4;

    WifiManager wifimanager;
    WifiInfo wifiInfo;
    DhcpInfo dhcpinfo;
    int rssi;
    String wifissid;
    int wifilinkspeed;
    int wififrequency;

    //Common class objects
    public static DataGetterSetterClass sdataGetterSetterClass;
    AppController appController;
    DatabaseHandlerClass db;

    Boolean intconnectivity;

    LocationManager locationManager;
    Geocoder geocoder;
    String NetTypeStr;
    OpenCellID opencellid;

    String secondsimcarriername;
    String secondsimname;
    Boolean secsimisroaming;
    String secondsimcountry;
    String secondsimnetworkcountry;
    public String secondsimnettype;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("Backgroun service", "onStartJob: Started");
        db = new DatabaseHandlerClass(this);
        appController = (AppController) getApplication();
        sdataGetterSetterClass = new DataGetterSetterClass();
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GetTowerLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            sdataGetterSetterClass.set_UserLatitude("Requires location access");
            sdataGetterSetterClass.set_UserLongitude("Requires location access");
            sdataGetterSetterClass.set_UserArea("Requires location access");
            sdataGetterSetterClass.set_UserCity("Requires location access");
            sdataGetterSetterClass.set_UserState("Requires location access");
            sdataGetterSetterClass.set_UserCountry("Requires location access");

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, this);

           }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateformatstr = dateFormat.format(calendar.getTime());
        String deviceName = Build.MODEL;
        String deviceMan = Build.MANUFACTURER;
        String productstr = Build.BRAND;
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        sdataGetterSetterClass.set_Device_ID(id);
        sdataGetterSetterClass.set_DateAndTime(dateformatstr);
        sdataGetterSetterClass.set_MobileProductName(productstr);
        sdataGetterSetterClass.set_MobileModel(deviceName);
        sdataGetterSetterClass.set_NetOperator("Not available");
        sdataGetterSetterClass.set_NetType("Not available");
        sdataGetterSetterClass.set_NetStrength("Not available");
        sdataGetterSetterClass.set_NetCountry("Not available");
        sdataGetterSetterClass.set_SimOperator("Not available");
        sdataGetterSetterClass.set_SimCountry("Not available");
        sdataGetterSetterClass.set_WIFISSID("Not available");
        sdataGetterSetterClass.set_WIFISpeed("Not available");
        sdataGetterSetterClass.set_WIFIStrength("Not available");
        sdataGetterSetterClass.set_WIFIFrequency("Not available");
        sdataGetterSetterClass.set_SecondSimNetOperator("Not Available");
        sdataGetterSetterClass.set_SecondSimNetType("Not Available");
        sdataGetterSetterClass.set_SecondSimNetStrength("Not Available");
        sdataGetterSetterClass.set_SecondSimNetCountry("Not Available");
        sdataGetterSetterClass.set_SecondSimSimOperator("Not Available");
        sdataGetterSetterClass.set_SecondSimSimCountry("Not Available");
        sdataGetterSetterClass.set_UserLatitude("Not Available");
        sdataGetterSetterClass.set_UserLongitude("Not Available");
        sdataGetterSetterClass.set_TowerLongitude("Not Available");
        sdataGetterSetterClass.set_TowerLongitude("Not Available");
        sdataGetterSetterClass.set_UserArea("Not Available");
        sdataGetterSetterClass.set_UserState("Not Available");
        sdataGetterSetterClass.set_UserCity("Not Available");
        sdataGetterSetterClass.set_UserCountry("Not Available");
        sdataGetterSetterClass.set_TowerLatitude("Not Available");
        sdataGetterSetterClass.set_TowerLongitude("Not Available");
        sdataGetterSetterClass.set_TowerAddress("Not Available");

        wifimanager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifimanager.getConnectionInfo();
        dhcpinfo = wifimanager.getDhcpInfo();
        rssi = wifimanager.getConnectionInfo().getRssi();
        wifissid = wifiInfo.getSSID();
        wifilinkspeed = wifiInfo.getLinkSpeed();
        wififrequency = wifiInfo.getFrequency();
        sdataGetterSetterClass.set_WIFISSID(wifissid);
        sdataGetterSetterClass.set_WIFISpeed(String.valueOf(wifilinkspeed));
        sdataGetterSetterClass.set_WIFIStrength(String.valueOf(rssi));
        sdataGetterSetterClass.set_WIFIFrequency(String.valueOf(wififrequency));

        sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
       Userthresold = sharedPreferences.getInt("Network_thresold", 0);


        Thread thread = new Thread() {
            @Override
            public void run() {

                if (!isAirplaneModeOn(getApplicationContext())) {

                    int simcount = 0;
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    int phonetype = telephonyManager.getNetworkType();
                    String carriername = telephonyManager.getNetworkOperatorName();
                    String simname = telephonyManager.getSimOperatorName();
                    int datastatestr = telephonyManager.getDataState();
                    Boolean isroaming = telephonyManager.isNetworkRoaming();
                    String simcountrystr = telephonyManager.getSimCountryIso();
                    String networkcountry = telephonyManager.getNetworkCountryIso();

                    sdataGetterSetterClass.set_NetOperator(carriername);
                    sdataGetterSetterClass.set_NetCountry(networkcountry);
                    sdataGetterSetterClass.set_SimOperator(simname);
                    sdataGetterSetterClass.set_SimCountry(simcountrystr);

                    switch (telephonyManager.getNetworkType())
                    {
                        case 0:
                            NetTypeStr = "Unknown";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);

                            break;
                        case 1:
                            NetTypeStr = "GPRS";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 2:
                            NetTypeStr = "EDGE";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 3:
                            NetTypeStr = "UMTS";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 4:
                            NetTypeStr = "CDMA";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 5:
                            NetTypeStr = "EVDO_0";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 6:
                            NetTypeStr = "EVDO_A";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 7:
                            NetTypeStr = "1xRTT";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 8:
                            NetTypeStr = "HSDPA";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 9:
                            NetTypeStr = "HSUPA";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 10:
                            NetTypeStr = "HSPA";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 11:
                            NetTypeStr = "iDen";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 12:
                            NetTypeStr = "EVDO_B";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 13:
                            NetTypeStr = "LTE";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 14:
                            NetTypeStr = "eHRPD";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            break;
                        case 15:
                            NetTypeStr = "HSPA+";
                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                            // networkoperatortypetextview.setText("Net.type : "+NetTypeStr);
                            break;

                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        SubscriptionManager smcount = SubscriptionManager.from(getApplicationContext());
                        simcount = smcount.getActiveSubscriptionInfoCountMax();
                    }

                    try {
                        Class<?> simdetectclass = Class.forName("android.telephony.TelephonyManager");
                        Method simStateMethod = simdetectclass.getDeclaredMethod("getSimState", int.class);

                        int sim1state = (int) simStateMethod.invoke(telephonyManager, 0);

                        switch (sim1state) {
                            case TelephonyManager.SIM_STATE_ABSENT:
                                // do something
                                break;
                            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                                // do something
                                break;
                            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                                // do something
                                break;
                            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                                // do something
                                break;
                            case TelephonyManager.SIM_STATE_READY:

                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.

                                    return;
                                } else {

                                    List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
                                    if (cellInfos != null) {
                                        for (int i = 0; i < cellInfos.size(); i++) {
                                            //     if (i == 0) {
                                            if (cellInfos.get(i).isRegistered()) {
                                                if (cellInfos.get(i) instanceof CellInfoWcdma) {
                                                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(i);
                                                    CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                                                    String strength = String.valueOf(cellSignalStrengthWcdma.getDbm());
                                                    //int rssi = -113 + 2 * cellSignalStrengthWcdma;
                                                    sdataGetterSetterClass.set_NetStrength(strength);
                                                    int level = cellSignalStrengthWcdma.getLevel();



                                                    // Log.e(str , strength);
                                                    switch (level) {
                                                        case 0:
                                                            if (Userthresold >= THRESHOLD_VERY_POOR){
                                                                String str = "Sim 1 signal level is very low";
                                                             //   NotificationMethod(str);

                                                            }
                                                            break;
                                                        case 1:
                                                            if (Userthresold >= THRESHOLD_POOR) {
                                                                String str1 = "Sim 1 signal level is low";
                                                             //   NotificationMethod(str1);
                                                            }
                                                            break;
                                                        case 2:
                                                            if (Userthresold >= THRESHOLD_INTERMEDIATE) {
                                                                String str2 = "Sim 1 signal level is moderate";
                                                             //   NotificationMethod(str2);

                                                            }
                                                            break;
                                                        case 3:
                                                            if (Userthresold >= THRESHOLD_GOOD) {
                                                                String str3 = "Sim 1 signal level is good";
                                                             //   NotificationMethod(str3);

                                                            }
                                                            break;
                                                        case 4:
                                                            if (Userthresold >= THRESHOLD_EXCELLENT) {
                                                                String str4 = "Sim 1 signal level is excellent";
                                                             //   NotificationMethod(str4);
                                                            }
                                                            break;

                                                    }
                                                } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                                                    CellInfoGsm cellInfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(i);
                                                    CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                                                    String strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                                                    sdataGetterSetterClass.set_NetStrength(strength);
                                                    int level = cellSignalStrengthGsm.getLevel();

                                                    //Log.e(str , strength);
                                                    switch (level) {
                                                        case 0:
                                                            if (Userthresold >= THRESHOLD_VERY_POOR){
                                                                String str = "Sim 1 signal level is very low";
                                                             //   NotificationMethod(str);

                                                            }
                                                            break;
                                                        case 1:
                                                            if (Userthresold >= THRESHOLD_POOR) {
                                                                String str1 = "Sim 1 signal level is low";
                                                             //   NotificationMethod(str1);
                                                            }
                                                            break;
                                                        case 2:
                                                            if (Userthresold >= THRESHOLD_INTERMEDIATE) {
                                                                String str2 = "Sim 1 signal level is moderate";
                                                             //   NotificationMethod(str2);

                                                            }
                                                            break;
                                                        case 3:
                                                            if (Userthresold >= THRESHOLD_GOOD) {
                                                                String str3 = "Sim 1 signal level is good";
                                                             //   NotificationMethod(str3);

                                                            }
                                                            break;
                                                        case 4:
                                                            if (Userthresold >= THRESHOLD_EXCELLENT) {
                                                                String str4 = "Sim 1 signal level is excellent";
                                                             //   NotificationMethod(str4);
                                                            }
                                                            break;

                                                    }
                                                } else if (cellInfos.get(i) instanceof CellInfoLte) {
                                                    CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(i);
                                                    CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                                                    String strength = String.valueOf(cellSignalStrengthLte.getDbm());
                                                    sdataGetterSetterClass.set_NetStrength(strength);
                                                    int level = cellSignalStrengthLte.getLevel();

                                                    // Log.e(str , strength);
                                                    switch (level) {
                                                        case 0:
                                                            if (Userthresold >= THRESHOLD_VERY_POOR){
                                                                String str = "Sim 1 signal level is very low";
                                                             //   NotificationMethod(str);

                                                            }
                                                            break;
                                                        case 1:
                                                            if (Userthresold >= THRESHOLD_POOR) {
                                                                String str1 = "Sim 1 signal level is low";
                                                             //   NotificationMethod(str1);
                                                            }
                                                            break;
                                                        case 2:
                                                            if (Userthresold >= THRESHOLD_INTERMEDIATE) {
                                                                String str2 = "Sim 1 signal level is moderate";
                                                             //   NotificationMethod(str2);

                                                            }
                                                            break;
                                                        case 3:
                                                            if (Userthresold >= THRESHOLD_GOOD) {
                                                                String str3 = "Sim 1 signal level is good";
                                                             //   NotificationMethod(str3);

                                                            }
                                                            break;
                                                        case 4:
                                                            if (Userthresold >= THRESHOLD_EXCELLENT) {
                                                                String str4 = "Sim 1 signal level is excellent";
                                                             //   NotificationMethod(str4);
                                                            }
                                                            break;

                                                    }
                                                } else if (cellInfos.get(i) instanceof CellInfoCdma) {
                                                    CellInfoCdma cellInfoWcdma = (CellInfoCdma) telephonyManager.getAllCellInfo().get(i);
                                                    CellSignalStrengthCdma cellSignalStrengthcdma = cellInfoWcdma.getCellSignalStrength();
                                                    String strength = String.valueOf(cellSignalStrengthcdma.getDbm());
                                                    sdataGetterSetterClass.set_NetStrength(strength);
                                                    int level = cellSignalStrengthcdma.getLevel();

                                                    // Log.e(str , strength);
                                                    switch (level) {
                                                        case 0:
                                                            if (Userthresold >= THRESHOLD_VERY_POOR){
                                                                String str = "Sim 1 signal level is very low";
                                                             //   NotificationMethod(str);

                                                            }
                                                            break;
                                                        case 1:
                                                            if (Userthresold >= THRESHOLD_POOR) {
                                                                String str1 = "Sim 1 signal level is low";
                                                             //   NotificationMethod(str1);
                                                            }
                                                            break;
                                                        case 2:
                                                            if (Userthresold >= THRESHOLD_INTERMEDIATE) {
                                                                String str2 = "Sim 1 signal level is moderate";
                                                             //   NotificationMethod(str2);

                                                            }
                                                            break;
                                                        case 3:
                                                            if (Userthresold >= THRESHOLD_GOOD) {
                                                                String str3 = "Sim 1 signal level is good";
                                                             //   NotificationMethod(str3);

                                                            }
                                                            break;
                                                        case 4:
                                                            if (Userthresold >= THRESHOLD_EXCELLENT) {
                                                                String str4 = "Sim 1 signal level is excellent";
                                                             //   NotificationMethod(str4);
                                                            }
                                                            break;

                                                    }
                                                }
                                                //   }
                                            }
                                        }
                                    }
                                }

                                // do something
                                break;
                            case TelephonyManager.SIM_STATE_UNKNOWN:
                                // do something
                                break;
                            case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                                break;
                            case TelephonyManager.SIM_STATE_PERM_DISABLED:
                                break;
                            case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                                break;
                        }

                        if (simcount == 2) {

                            int sim2state = (int) simStateMethod.invoke(telephonyManager, 1);

                            switch (sim2state) {
                                case TelephonyManager.SIM_STATE_ABSENT:
                                    // do something
                                    break;
                                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                                    // do something
                                    break;
                                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                                    // do something
                                    break;
                                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                                    // do something
                                    break;
                                case TelephonyManager.SIM_STATE_READY:
                                    //Dual sim methods
                                    final Class<?> tmClassSM;
                                    Class<?> tClassSM;

                                    try {

                                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.

                                            return;
                                        } else {


                                        tmClassSM = Class.forName("android.telephony.SubscriptionManager");

                                        // Static method to return list of active subids
                                        Method method[] = tmClassSM.getDeclaredMethods();
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

                                            Method tmmethod = TelephonyManager.class.getMethod("getNetworkOperatorName", int.class);
                                            Method tmnettypemethod = TelephonyManager.class.getMethod("getNetworkType", int.class);
                                            Method tmisroaming = TelephonyManager.class.getMethod("isNetworkRoaming", int.class);



                                            Method methodsub = SubscriptionManager.class.getMethod("getActiveSubscriptionIdList");
                                            int[] ssubIdList = (int[]) methodsub.invoke(SubscriptionManager.from(getApplicationContext()));

                                            SubscriptionManager sm = SubscriptionManager.from(getApplicationContext());
                                            List<SubscriptionInfo> subscriptions = sm.getActiveSubscriptionInfoList();

                                            if (subscriptions != null) {

                                                for (SubscriptionInfo subscriptioninfo : subscriptions) {

                                                    if (subscriptioninfo.getSimSlotIndex() == 1) {

                                                        try {
                                                            Method tmcountrymethod = TelephonyManager.class.getMethod("getNetworkCountryIso", int.class);
                                                            secondsimnetworkcountry = (String) tmcountrymethod.invoke(mTelephonyManager, subscriptioninfo.getSubscriptionId());
                                                            //secsimnetworkcountrytextview.setText(secondsimnetworkcountry.toUpperCase());
                                                            sdataGetterSetterClass.set_SecondSimNetCountry(secondsimnetworkcountry);
                                                        } catch (NoSuchMethodException e) {
                                                            e.printStackTrace();
                                                        } catch (IllegalAccessException e) {
                                                            e.printStackTrace();
                                                        } catch (InvocationTargetException e) {
                                                            e.printStackTrace();
                                                        }

                                                        secondsimcountry = (String) subscriptioninfo.getCountryIso();
                                                        secsimisroaming = (Boolean) tmisroaming.invoke(mTelephonyManager, subscriptioninfo.getSubscriptionId());
                                                        secondsimcarriername = (String) tmmethod.invoke(mTelephonyManager, subscriptioninfo.getSubscriptionId());
                                                        secondsimname = (String) subscriptioninfo.getCarrierName();
                                                        int ssnettype = (int) tmnettypemethod.invoke(mTelephonyManager, subscriptioninfo.getSubscriptionId());

                                                        sdataGetterSetterClass.set_SecondSimNetOperator(secondsimcarriername);
                                                        sdataGetterSetterClass.set_SecondSimSimOperator(secondsimname);
                                                        sdataGetterSetterClass.set_SecondSimSimCountry(secondsimcountry);

                                                        switch (ssnettype) {
                                                            case 0:
                                                                secondsimnettype = "Unknown";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);

                                                                break;
                                                            case 1:
                                                                secondsimnettype = "GPRS";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);

                                                                break;
                                                            case 2:
                                                                secondsimnettype = "EDGE";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 3:
                                                                secondsimnettype = "UMTS";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 4:
                                                                secondsimnettype = "CDMA";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 5:
                                                                secondsimnettype = "EVDO_0";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 6:
                                                                secondsimnettype = "EVDO_A";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 7:
                                                                secondsimnettype = "1xRTT";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 8:
                                                                secondsimnettype = "HSDPA";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 9:
                                                                secondsimnettype = "HSUPA";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 10:
                                                                secondsimnettype = "HSPA";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 11:
                                                                secondsimnettype = "iDen";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 12:
                                                                secondsimnettype = "EVDO_B";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 13:
                                                                secondsimnettype = "LTE";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 14:
                                                                secondsimnettype = "eHRPD";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                break;
                                                            case 15:
                                                                secondsimnettype = "HSPA+";
                                                                sdataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                                                // networkoperatortypetextview.setText("Net.type : "+NetTypeStr);
                                                                break;

                                                        }

                                                        quitlooper = false;
                                                        Looper.prepare();
                                                        JobSchedulerService.MultiSimListener listener = new JobSchedulerService.MultiSimListener(subscriptioninfo.getSubscriptionId());
                                                        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_CALL_STATE);
                                                        Looper.loop();

                                                    }
                                                }

                                            }
                                        }

                                    }
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                    // do something
                                    break;
                                case TelephonyManager.SIM_STATE_UNKNOWN:
                                    // do something
                                    break;
                                case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                                    break;
                                case TelephonyManager.SIM_STATE_PERM_DISABLED:
                                    break;
                                case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                                    break;
                            }
                        }else {
                            intconnectivity = checkConnection();
                            if (db.isExist(sdataGetterSetterClass.get_DateAndTime())){

                            }else {
                                if (intconnectivity) {
                                    appController.PostServerURLMethod("", sdataGetterSetterClass);
                                } else {
                                    appController.LocalStorageMethod(sdataGetterSetterClass, "No");
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();

        // Uses a handler to delay the execution of jobFinished().

        jobFinished(params,true);

       // if(Build.VERSION.SDK_INT ==Build.VERSION_CODES.M) {
            return true;
       // }else {
      //      return true;
      //  }

    }

    public void FirstSimMethods() {

        TelephonyManager telephonyManagers = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int phonetype = telephonyManagers.getNetworkType();
        String carriername = telephonyManagers.getNetworkOperatorName();
        String simname = telephonyManagers.getSimOperatorName();
        int datastatestr = telephonyManagers.getDataState();
        Boolean isroaming = telephonyManagers.isNetworkRoaming();
        String simcountrystr = telephonyManagers.getSimCountryIso();
        String networkcountry = telephonyManagers.getNetworkCountryIso();

        sdataGetterSetterClass.set_NetOperator(carriername);
        sdataGetterSetterClass.set_NetCountry(networkcountry);
        sdataGetterSetterClass.set_SimOperator(simname);
        sdataGetterSetterClass.set_SimCountry(simcountrystr);

        simcountrystr = mTelephonyManager.getSimCountryIso();
        networkcountry = mTelephonyManager.getNetworkCountryIso();
        datastatestr = mTelephonyManager.getDataState();

        //Dual sim methods
        final Class<?> tmClassSM;
        Class<?> tClassSM;
        try {
            tmClassSM = Class.forName("android.telephony.SubscriptionManager");
            // Static method to return list of active subids
            Method method[] = tmClassSM.getDeclaredMethods();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

                Method methodsub = SubscriptionManager.class.getMethod("getActiveSubscriptionIdList");
                int[] ssubIdList = (int[]) methodsub.invoke(SubscriptionManager.from(getApplicationContext()));

                SubscriptionManager sm = SubscriptionManager.from(getApplicationContext());

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                } else {

                    List<SubscriptionInfo> subscriptions = sm.getActiveSubscriptionInfoList();

                    if (subscriptions != null) {

                        for (SubscriptionInfo subscriptioninfo : subscriptions) {

                            if (subscriptioninfo.getSimSlotIndex() == 0) {

                                tClassSM = Class.forName(mTelephonyManager.getClass().getName());
                                Method tmmethod = TelephonyManager.class.getMethod("getNetworkOperatorName", int.class);
                                Method tmnettypemethod = TelephonyManager.class.getMethod("getNetworkType", int.class);
                                Method tmisroaming = TelephonyManager.class.getMethod("isNetworkRoaming", int.class);
                                try {
                                    Method tmcountrymethod = TelephonyManager.class.getMethod("getNetworkCountryIso", int.class);
                                    networkcountry = (String) tmcountrymethod.invoke(mTelephonyManager, subscriptioninfo.getSubscriptionId());
                                  //  networkcountrytextview.setText(networkcountry.toUpperCase());
                                    sdataGetterSetterClass.set_NetCountry(networkcountry);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }

                                sdataGetterSetterClass.set_NetCountry(networkcountry);
                                isroaming = (Boolean) tmisroaming.invoke(mTelephonyManager, subscriptioninfo.getSubscriptionId());
                                carriername = (String) tmmethod.invoke(mTelephonyManager, subscriptioninfo.getSubscriptionId());
                                simname = (String) subscriptioninfo.getCarrierName();
                                int ssnettype = (int) tmnettypemethod.invoke(mTelephonyManager, subscriptioninfo.getSubscriptionId());
                                sdataGetterSetterClass.set_NetOperator(carriername);
                                sdataGetterSetterClass.set_SimOperator(simname);
                                sdataGetterSetterClass.set_SimCountry(simcountrystr);

                                if (!simname.equals("No service")) {
                                    switch (ssnettype) {
                                        case 0:
                                            NetTypeStr = "Unknown";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 1:
                                            NetTypeStr = "GPRS";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 2:
                                            NetTypeStr = "EDGE";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 3:
                                            NetTypeStr = "UMTS";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 4:
                                            NetTypeStr = "CDMA";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 5:
                                            NetTypeStr = "EVDO_0";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 6:
                                            NetTypeStr = "EVDO_A";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 7:
                                            NetTypeStr = "1xRTT";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 8:
                                            NetTypeStr = "HSDPA";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 9:
                                            NetTypeStr = "HSUPA";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                          //  phonetypetextview.setText(NetTypeStr);
                                            break;
                                        case 10:
                                            NetTypeStr = "HSPA";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 11:
                                            NetTypeStr = "iDen";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 12:
                                            NetTypeStr = "EVDO_B";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 13:
                                            NetTypeStr = "LTE";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 14:
                                            NetTypeStr = "eHRPD";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);
                                            break;
                                        case 15:
                                            NetTypeStr = "HSPA+";
                                            sdataGetterSetterClass.set_NetType(NetTypeStr);

                                            break;

                                    }

                                    FirstMultiSimListener listener = new FirstMultiSimListener(subscriptioninfo.getSubscriptionId());
                                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        sdataGetterSetterClass.set_TowerAddress("Requires location access");
                                        sdataGetterSetterClass.set_NetStrength("Requires location access");

                                    } else {
                                        telephonyManagers.listen(listener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_CALL_STATE);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch(NoSuchMethodException e){
            e.printStackTrace();
        } catch(IllegalAccessException e){
            e.printStackTrace();
        } catch(InvocationTargetException e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onStopJob(JobParameters params) {

        Log.e("Background service", "onStopJob: Stopped");
        return true;
    }

    private Boolean checkConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    public void NotificationMethod(String strength){
        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext(), "12345");

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.antenna)
                .setTicker("notification")
                .setContentTitle("Tower Strength")
                .setContentText(strength)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(pIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    @Override
    public void onLocationChanged(Location location) {

        sdataGetterSetterClass.set_UserLatitude(String.valueOf(location.getLatitude()));
        sdataGetterSetterClass.set_UserLongitude(String.valueOf(location.getLongitude()));

        try {
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String country = address.get(0).getCountryName();
            String state = address.get(0).getAdminArea();
            String city = address.get(0).getSubAdminArea();
            String area = address.get(0).getSubLocality();
            sdataGetterSetterClass.set_UserCountry(country);
            sdataGetterSetterClass.set_UserState(state);
            sdataGetterSetterClass.set_UserCity(city);
            sdataGetterSetterClass.set_UserArea(area);

            Log.e("","");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class FirstMultiSimListener extends PhoneStateListener {

        private Field subIdField;
        private Integer subId = -1;

        public FirstMultiSimListener (Integer subId) {
            super();
            try {
                // Get the protected field mSubId of PhoneStateListener and set it
                subIdField = this.getClass().getSuperclass().getDeclaredField("mSubId");
                subIdField.setAccessible(true);
                subIdField.set(this, subId);
                this.subId = subId;
            } catch (NoSuchFieldException e) {

            } catch (IllegalAccessException e) {

            } catch (IllegalArgumentException e) {

            }
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            int rssi = -113 + 2 * signalStrength.getGsmSignalStrength();

            if (rssi <= -100) {
             //   qualitypercent = 0;
            } else if (rssi >= -50) {
              //  qualitypercent = 100;
            } else {
              //  qualitypercent = 2 * (rssi + 100);
            }


            sdataGetterSetterClass.set_NetStrength(String.valueOf(rssi));


        }

    }

    public class MultiSimListener extends PhoneStateListener {

        private Field subIdField;
        private Integer subId = -1;

        public MultiSimListener (Integer subId) {
            super();
            try {
                // Get the protected field mSubId of PhoneStateListener and set it
                subIdField = this.getClass().getSuperclass().getDeclaredField("mSubId");
                subIdField.setAccessible(true);
                subIdField.set(this, subId);
                this.subId = subId;
            } catch (NoSuchFieldException e) {

            } catch (IllegalAccessException e) {

            } catch (IllegalArgumentException e) {

            }
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            // Handle the event here, subId indicates the subscription id if > 0
            int rssi = -113 + 2 * signalStrength.getGsmSignalStrength();
            sdataGetterSetterClass.set_SecondSimNetStrength(String.valueOf(rssi));
            int signallevel = 0;
            intconnectivity = checkConnection();
            if (db.isExist(sdataGetterSetterClass.get_DateAndTime())){

            }else {
                if (intconnectivity) {
                    appController.PostServerURLMethod("", sdataGetterSetterClass);
                } else {
                    appController.LocalStorageMethod(sdataGetterSetterClass, "No");
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                signallevel = signalStrength.getLevel();
                switch (signallevel){
                    case 0:
                        if (Userthresold >= THRESHOLD_VERY_POOR) {
                            String str = "Sim 2 signal level is very low";
                         //   NotificationMethod(str);
                        }
                        break;
                    case 1:
                        if (Userthresold >= THRESHOLD_POOR) {
                            String str1 = "Sim 2 signal level is low";
                         //   NotificationMethod(str1);
                        }
                        break;
                    case 2:
                        if (Userthresold >= THRESHOLD_INTERMEDIATE) {
                            String str2 = "Sim 2 signal level is moderate";
                         //   NotificationMethod(str2);
                        }
                        break;
                    case 3:
                        if (Userthresold >= THRESHOLD_GOOD) {
                            String str3 = "Sim 2 signal level is good";
                         //   NotificationMethod(str3);
                        }
                        break;
                    case 4:
                        if (Userthresold >= THRESHOLD_EXCELLENT) {
                            String str4 = "Sim 2 signal level is excellent";
                         //   NotificationMethod(str4);
                        }
                        break;
                }
                if(quitlooper)
                    Looper.myLooper().quit();
            }
        }

    }

    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

    }

    public class OpenCellID {
        String mcc; //Mobile Country Code
        String mnc; //mobile network code
        String cellid; //Cell ID
        String lac; //Location Area Code
        Boolean error;
        String strURLSent;
        String GetOpenCellID_fullresult;
        String latitude;
        String longitude;
        JSONArray requestarray;
        String radio;

        public void setRadio(String radiotype) {
            radio = radiotype;
        }

        public Boolean isError() {
            return error;
        }

        public void setRequestarray(JSONArray requestarr) {
            requestarray = requestarr;
        }

        public void setMcc(String value) {
            mcc = value;
        }

        public void setMnc(String value) {
            mnc = value;
        }

        public void setCallID(int value) {
            cellid = String.valueOf(value);
        }

        public void setCallLac(int value) {
            lac = String.valueOf(value);
        }

        public String getLocation() {
            return (latitude + " : " + longitude);
        }

        public void groupURLSent() {
            strURLSent = "https://us2.unwiredlabs.com/v2/process.php";
        }

        public String getstrURLSent() {
            return strURLSent;
        }

        public String getGetOpenCellID_fullresult() {
            return GetOpenCellID_fullresult;
        }

        public void GetOpenCellID() throws Exception {

            final Boolean retrieved;
            int SocketTimeout = 30000;
            RetryPolicy retry = new DefaultRetryPolicy(SocketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            );
            int metint = Request.Method.POST;
            JSONObject finalreqobj = new JSONObject();
            finalreqobj.put("token", "9a810d6f60efd9");
            finalreqobj.put("radio", radio);
            finalreqobj.put("mcc", mcc);
            finalreqobj.put("mnc", mnc);
            finalreqobj.put("cells", requestarray);
            finalreqobj.put("address", "1");
            groupURLSent();
            retrieved =false;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://us2.unwiredlabs.com/v2/process.php", finalreqobj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Server Res", response.toString());
                            //    callback.ReturnedResponseFromServer(response);

                            LatLng towerlocation = null;
                            try {
                                sdataGetterSetterClass.set_TowerLatitude(String.valueOf(response.getDouble("lat")));
                                sdataGetterSetterClass.set_TowerLongitude(String.valueOf(response.getDouble("lon")));
                                sdataGetterSetterClass.set_TowerAddress(response.getString("address"));

                                Log.e("","");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("Request", "Success");

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Server err", error.toString());
                        }
                    }
            );

            request.setRetryPolicy(retry);
            AppController.getInstance().addToRequestQueue(request, "json_req_method");
        }
    }

    public void GetTowerLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // return;
            sdataGetterSetterClass.set_TowerAddress("Requires location access");

        }else{
            List<CellInfo> cellLocation = mTelephonyManager.getAllCellInfo();
            if (cellLocation != null) {
                for (int i = 0; i < cellLocation.size(); ++i) {

                    CellInfo cellinfo = cellLocation.get(i);
                    Log.d("CellLocation", cellinfo.toString());
                    if (cellinfo instanceof CellInfoGsm) {

                        CellIdentityGsm identityGsm = ((CellInfoGsm) cellinfo).getCellIdentity();
                        String networkOperator = mTelephonyManager.getNetworkOperator();
                        if (!"".equals(networkOperator)) {
                            String gsmnetmcc = networkOperator.substring(0, 3);
                            String gsmnetmnc = networkOperator.substring(3);
                            int gsmlac = identityGsm.getLac();
                            int gsmcid = identityGsm.getCid();
                            int gsmmcc = identityGsm.getMcc();
                            int gsmmnc = identityGsm.getMnc();

                            JSONArray gsmreqarr = new JSONArray();
                            JSONObject gsmreqobject = new JSONObject();
                            try {
                                gsmreqobject.put("lac", gsmlac);
                                gsmreqobject.put("cid", gsmcid);
                                gsmreqarr.put(gsmreqobject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            opencellid = new OpenCellID();

                            opencellid.setMcc(gsmnetmcc);
                            opencellid.setMnc(gsmnetmnc);
                            opencellid.setCallID(gsmcid);
                            opencellid.setCallLac(gsmlac);
                            opencellid.setRadio("gsm");
                            opencellid.requestarray = gsmreqarr;
                            try {
                                opencellid.GetOpenCellID();
                                if (!opencellid.isError()) {
                                    Log.e("Opencellid", opencellid.getLocation());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    } else if (cellinfo instanceof CellInfoLte) {


                        CellIdentityLte identityLte = ((CellInfoLte) cellinfo).getCellIdentity();
                        int lac = identityLte.getTac();
                        int cid = identityLte.getCi();
                        int mcc = identityLte.getMcc();
                        int mnc = identityLte.getMnc();
                        int psc = identityLte.getPci();

                        JSONArray reqarr = new JSONArray();
                        JSONObject reqobject = new JSONObject();
                        try {
                            reqobject.put("lac", lac);
                            reqobject.put("cid", cid);
                            reqarr.put(reqobject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String networkOperator = mTelephonyManager.getNetworkOperator();

                        if (!"".equals(networkOperator)) {

                            String netmcc = networkOperator.substring(0, 3);
                            String netmnc = networkOperator.substring(3);

                            opencellid = new OpenCellID();

                            opencellid.setMcc(netmcc);
                            opencellid.setMnc(netmnc);
                            opencellid.setCallID(cid);
                            opencellid.setCallLac(lac);
                            opencellid.setRadio("lte");
                            opencellid.requestarray = reqarr;

                            try {
                                opencellid.GetOpenCellID();
                                if (!opencellid.isError()) {
                                    Log.e("Opencellid", opencellid.getLocation());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (cellinfo instanceof CellInfoCdma) {

                        CellIdentityCdma identityCdma = ((CellInfoCdma) cellinfo).getCellIdentity();
                        String networkOperator = mTelephonyManager.getNetworkOperator();

                        if (!"".equals(networkOperator)) {
                            String cdmanetmcc = networkOperator.substring(0, 3);
                            String cdmanetmnc = networkOperator.substring(3);

                            int cdmalat = identityCdma.getLongitude();
                            int cdmalon = identityCdma.getLongitude();

                            LatLng towerlocation = null;
                            towerlocation = new LatLng(cdmalat, cdmalon);

                        }
                    } else if (cellinfo instanceof CellInfoWcdma) {

                        CellIdentityWcdma identityWcdma = ((CellInfoWcdma) cellinfo).getCellIdentity();

                        String networkOperator = mTelephonyManager.getNetworkOperator();

                        if (!"".equals(networkOperator)) {
                            String wcdmanetmcc = networkOperator.substring(0, 3);
                            String wcdmanetmnc = networkOperator.substring(3);

                            int wcdmalac = identityWcdma.getLac();
                            int wcdmacid = identityWcdma.getCid();
                            int wcdmamcc = identityWcdma.getMcc();
                            int wcdmamnc = identityWcdma.getMnc();


                            JSONArray reqarr = new JSONArray();
                            JSONObject reqobject = new JSONObject();
                            try {
                                reqobject.put("lac", wcdmalac);
                                reqobject.put("cid", wcdmacid);
                                reqarr.put(reqobject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            opencellid = new OpenCellID();

                            opencellid.setMcc(wcdmanetmcc);
                            opencellid.setMnc(wcdmanetmnc);
                            opencellid.setCallID(wcdmacid);
                            opencellid.setCallLac(wcdmalac);
                            opencellid.setRadio("cdma");
                            opencellid.requestarray = reqarr;


                            try {
                                opencellid.GetOpenCellID();
                                if (!opencellid.isError()) {
                                    Log.e("Opencellid", opencellid.getLocation());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
            }
        }
    }

}
