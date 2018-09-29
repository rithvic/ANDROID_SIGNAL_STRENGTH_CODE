package com.quadrobay.qbayApps.networkproject.SingletonClass;

/**
 * Created by sairaj on 31/08/17.
 */

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DataGetterSetterClass;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DatabaseHandlerClass;
import com.quadrobay.qbayApps.networkproject.R;
import com.quadrobay.qbayApps.networkproject.Services.ConnectivityReceiver;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import bss.bbs.com.teslaclone.utils.LruBitmapCache;

public class AppController extends Application {

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    private static Tracker sappTracker;

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private String selectedhistoryvalue;

    DataGetterSetterClass dataGetterSetterClass;
    DatabaseHandlerClass db;

    public String getConnectedDevice() {
        return connectedDevice;
    }

    public void setConnectedDevice(String connectedDevice) {
        this.connectedDevice = connectedDevice;
    }

    String connectedDevice;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

    synchronized public Tracker getAppTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sappTracker == null) {
            sappTracker = sAnalytics.newTracker(R.xml.app_tracker);
            sappTracker.enableExceptionReporting(true);

        }

        return sappTracker;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public void setDataGetterSetterClass(DataGetterSetterClass datasetterclass){
        dataGetterSetterClass = datasetterclass;
    }

    public DataGetterSetterClass getDataGetterSetterClass(){
        return dataGetterSetterClass;
    }

    public void setSelectedhistoryvalue(String selectedhistoryval){
        selectedhistoryvalue = selectedhistoryval;
    }

    public String getSelectedhistoryvalue(){
        return selectedhistoryvalue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void LocalStorageMethod(DataGetterSetterClass dataGetterSetterClass, String updated) {

        db = new DatabaseHandlerClass(this);
        db.addDetails(new DataGetterSetterClass(dataGetterSetterClass.get_Device_ID(), dataGetterSetterClass.get_DateAndTime(), dataGetterSetterClass.get_MobileProductName(), dataGetterSetterClass.get_MobileModel(), dataGetterSetterClass.get_NetOperator(), dataGetterSetterClass.get_NetType(), dataGetterSetterClass.get_NetStrength(), dataGetterSetterClass.get_NetCountry(), dataGetterSetterClass.get_SimOperator(), dataGetterSetterClass.get_SimCountry(), dataGetterSetterClass.get_SecondSimNetOperator(), dataGetterSetterClass.get_SecondSimNetType(), dataGetterSetterClass.get_SecondSimNetStrength(), dataGetterSetterClass.get_SecondSimNetCountry(), dataGetterSetterClass.get_SecondSimSimOperator(), dataGetterSetterClass.get_SecondSimSimCountry(), dataGetterSetterClass.get_WIFISSID(), dataGetterSetterClass.get_WIFISpeed(), dataGetterSetterClass.get_WIFIStrength(), dataGetterSetterClass.get_WIFIFrequency(), dataGetterSetterClass.get_UserLatitude(), dataGetterSetterClass.get_UserLongitude(), dataGetterSetterClass.get_UserCountry(), dataGetterSetterClass.get_UserState(), dataGetterSetterClass.get_UserCity(), dataGetterSetterClass.get_UserArea(), dataGetterSetterClass.get_TowerLatitude(), dataGetterSetterClass.get_TowerLongitude(), dataGetterSetterClass.get_TowerAddress(), dataGetterSetterClass.get_Updated()), updated);

    }



    public void PostServerURLMethod(String url, final DataGetterSetterClass dataGetterSetterClass) {

        JSONObject finalobj = new JSONObject();
        db = new DatabaseHandlerClass(this);
        JSONArray jsarr = new JSONArray();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("Device_ID",dataGetterSetterClass.get_Device_ID());
            postdata.put("Date_And_Time",dataGetterSetterClass.get_DateAndTime());
            postdata.put("Mobile_Product",dataGetterSetterClass.get_MobileProductName());
            postdata.put("Mobile_Model", dataGetterSetterClass.get_MobileModel());
            postdata.put("Sim1_Net_Operator", dataGetterSetterClass.get_NetOperator());
            postdata.put("Sim1_Net_Type", dataGetterSetterClass.get_NetType());
            postdata.put("Sim1_Net_Strength", dataGetterSetterClass.get_NetStrength());
            postdata.put("Sim1_Net_Country", dataGetterSetterClass.get_NetCountry());
            postdata.put("Sim1_Sim_Operator", dataGetterSetterClass.get_SimOperator());
            postdata.put("Sim1_Sim_Country", dataGetterSetterClass.get_SimCountry());
            postdata.put("Sim2_Net_Operator", dataGetterSetterClass.get_SecondSimNetOperator());
            postdata.put("Sim2_Net_Type", dataGetterSetterClass.get_SecondSimNetType());
            postdata.put("Sim2_Net_Strength", dataGetterSetterClass.get_SecondSimNetStrength());
            postdata.put("Sim2_Net_Country", dataGetterSetterClass.get_SecondSimNetCountry());
            postdata.put("Sim2_Sim_Operator", dataGetterSetterClass.get_SecondSimSimOperator());
            postdata.put("Sim2_Sim_Country", dataGetterSetterClass.get_SecondSimSimCountry());
            postdata.put("WIFI_SSID", dataGetterSetterClass.get_WIFISSID());
            postdata.put("WIFI_Speed", dataGetterSetterClass.get_WIFISpeed());
            postdata.put("WIFI_Strength", dataGetterSetterClass.get_WIFIStrength());
            postdata.put("WIFI_Frequency", dataGetterSetterClass.get_WIFIFrequency());
            postdata.put("User_Latitude", dataGetterSetterClass.get_UserLatitude());
            postdata.put("User_Longitude", dataGetterSetterClass.get_UserLongitude());
            postdata.put("User_Country", dataGetterSetterClass.get_UserCountry());
            postdata.put("User_State", dataGetterSetterClass.get_UserState());
            postdata.put("User_City", dataGetterSetterClass.get_UserCity());
            postdata.put("User_Area", dataGetterSetterClass.get_UserArea());
            postdata.put("Tower_Latitude", dataGetterSetterClass.get_TowerLatitude());
            postdata.put("Tower_Longitude", dataGetterSetterClass.get_TowerLongitude());
            postdata.put("Tower_Address", dataGetterSetterClass.get_TowerAddress());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsarr.put(postdata);
        try {
            finalobj.put("post_data",jsarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int SocketTimeout = 30000;
        RetryPolicy retry= new DefaultRetryPolicy(SocketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                );
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://quadrobay.co.in/Signal_App/public/api/showroom/network-project", finalobj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Server Res", response.toString());
                        if (response != null){
                            try {
                                JSONArray arrvalue = response.getJSONArray("Response");
                                for (int i=0; i < arrvalue.length(); i++){
                                   // db.updateDetails(arrvalue.getString(i), "Yes");
                                    LocalStorageMethod(dataGetterSetterClass, "Yes");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Server err", error.toString());
                        error.printStackTrace();
                    }


                }

        );

        request.setRetryPolicy(retry);
        this.getInstance().addToRequestQueue(request,"json_req_method");
    }

    public void PostAllDataToServer(final JSONArray postarray){

        db = new DatabaseHandlerClass(this);
        JSONObject finaldata = new JSONObject();
        try {
            finaldata.put("post_data",postarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int SocketTimeout = 30000;
        RetryPolicy retry= new DefaultRetryPolicy(SocketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://findyourcarapp.com/findyourcarapp_apitest/public/api/showroom/network-project", finaldata,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Response", response.toString());
                        if (response != null){
                            try {
                                JSONArray arrvalue = response.getJSONArray("Response");
                                for (int i=0; i < arrvalue.length(); i++){
                                    db.updateDetails("Yes", arrvalue.getString(i));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Server err", error.toString());
                        error.printStackTrace();
                    }


                }

        );

        request.setRetryPolicy(retry);
        this.getInstance().addToRequestQueue(request,"json_req_method");

    }


}
