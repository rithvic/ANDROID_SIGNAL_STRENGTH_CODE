package com.quadrobay.qbayApps.networkproject.ActivityClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.quadrobay.qbayApps.networkproject.Services.ConnectivityReceiver;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DataGetterSetterClass;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DatabaseHandlerClass;
import com.quadrobay.qbayApps.networkproject.R;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by benchmark on 10/10/17.
 */

public class HistoryDetailsClass extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Boolean historydetailsdatabaseprocess;

    private Toolbar historydetailstoolbar;
    ListView historydetailslistView;

    Boolean intconnectivity;

    AppController appController;
    DataGetterSetterClass dataGetterSetterClass;
    String colvalue;
    Cursor cursor;

    DatabaseHandlerClass db;
    ArrayList<String> detailslist;

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    IntentFilter intentFilter;
    MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_details);

        historydetailsdatabaseprocess = true;

        historydetailslistView = (ListView) findViewById(R.id.history_details_list);

        db = new DatabaseHandlerClass(this);
        detailslist = new ArrayList<String>();

        appController = (AppController) getApplication();
        dataGetterSetterClass = appController.getDataGetterSetterClass();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(CONNECTIVITY_ACTION);
            receiver = new MyReceiver();
        }

        colvalue = appController.getSelectedhistoryvalue();
        Log.d("Database selected value",colvalue);

        cursor = db.getSingleHistoryDetail(colvalue);
        StringBuffer buf = new StringBuffer();

        if(cursor.moveToFirst()) {

            do {

                Log.d("Database hist name--->", "Entered" );
                Log.d("Database hist name--->", "" + cursor.getString(0));
                Log.d("Database hist name--->", "" + cursor.getString(1));
                Log.d("Database hist name--->", "" + cursor.getString(2));
                Log.d("Database hist name--->", "" + cursor.getString(3));
                Log.d("Database hist name--->", "" + cursor.getString(4));
                Log.d("Database hist name--->", "" + cursor.getString(5));
                Log.d("Database hist name--->", "" + cursor.getString(6));
                Log.d("Database hist name--->", "" + cursor.getString(7));
                Log.d("Database hist name--->", "" + cursor.getString(8));
                Log.d("Database hist name--->", "" + cursor.getString(9));

                detailslist.add("Updated on: "+cursor.getString(2));
                detailslist.add("Device_ID: "+cursor.getString(1));
                detailslist.add("Mobile Product: "+cursor.getString(3));
                detailslist.add("Mobile Model: "+cursor.getString(4));
                detailslist.add("Network Operator: "+cursor.getString(5));
                detailslist.add("Network Type: "+cursor.getString(6));
                detailslist.add("Network Strength: "+cursor.getString(7));
                detailslist.add("Network Country: "+cursor.getString(8));
                detailslist.add("Sim Operator: "+cursor.getString(9));
                detailslist.add("Sim Country: "+cursor.getString(10));
                detailslist.add("Second Sim Network Operator: "+cursor.getString(11));
                detailslist.add("Second Sim Network Type: "+cursor.getString(12));
                detailslist.add("Second Sim Network Strength: "+cursor.getString(13));
                detailslist.add("Second Sim Network Country: "+cursor.getString(14));
                detailslist.add("Second Sim Sim Operator: "+cursor.getString(15));
                detailslist.add("Second Sim Sim Country: "+cursor.getString(16));
                detailslist.add("WIFI SSID: "+cursor.getString(17));
                detailslist.add("WIFI Speed: "+cursor.getString(18));
                detailslist.add("WIFI Strength: "+cursor.getString(19));
                detailslist.add("WIFI Frequency: "+cursor.getString(20));
                detailslist.add("Area: "+cursor.getString(26));
                detailslist.add("City: "+cursor.getString(25));
                detailslist.add("State: "+cursor.getString(24));
                detailslist.add("Country: "+cursor.getString(23));
                detailslist.add("Tower Location: "+cursor.getString(29));
                detailslist.add("Updated: "+cursor.getString(30));
                detailslist.add("");


                buf.append(cursor.getString(0) + ",");

            } while (cursor.moveToNext());
            Log.e("Database column list: ", "" + buf);

        }

        Log.d("Final history detail",String.valueOf(detailslist));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, detailslist);
        historydetailslistView.setAdapter(adapter);

        historydetailstoolbar = (Toolbar) findViewById(R.id.history_details_toolbar);
        setSupportActionBar(historydetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("History Detail");

    }
    private Boolean checkConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
        //showSnack(isConnected);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    Log.i("Navigation", "You have forgotten to specify the parentActivityName in the AndroidManifest!");
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        historydetailsdatabaseprocess = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        historydetailsdatabaseprocess = true;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(receiver, intentFilter);
        }else {
            AppController.getInstance().setConnectivityListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unregisterReceiver(receiver);
        }

        if (historydetailsdatabaseprocess) {

            if (db.isExist(dataGetterSetterClass.get_DateAndTime())){
            }else {

                intconnectivity = checkConnection();
                if (intconnectivity) {
                    appController.PostServerURLMethod("", dataGetterSetterClass);
                } else {
                    appController.LocalStorageMethod(dataGetterSetterClass, "No");
                }
            }

        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            JSONArray resarr = db.getNotUpdatedDetails();
            if (resarr !=null) {
                appController.PostAllDataToServer(resarr);
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String actionOfIntent = intent.getAction();
            boolean isConnected = checkConnection();
            if(actionOfIntent.equals(CONNECTIVITY_ACTION)){
                if (isConnected){
                    JSONArray resarr = db.getNotUpdatedDetails();
                    if (resarr !=null) {
                        appController.PostAllDataToServer(resarr);
                    }
                }
            }
        }
    }
}
