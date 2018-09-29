package com.quadrobay.qbayApps.networkproject.ActivityClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.quadrobay.qbayApps.networkproject.Customadapter;
import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.quadrobay.qbayApps.networkproject.Services.ConnectivityReceiver;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DataGetterSetterClass;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DatabaseHandlerClass;
import com.quadrobay.qbayApps.networkproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by benchmark on 10/10/17.
 */

public class HistoryClass extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Boolean historydatabaseprocess;
    Boolean listclickable;
    Boolean intconnectivity;

    private Toolbar historytoolbar;
    private ListView historylist;

    List<DataGetterSetterClass> data;
    DataGetterSetterClass dataGetterSetterClass;
    DatabaseHandlerClass db;
    MainActivity mainActivity;

    ArrayList<String> listitems;
    ArrayList<String> datelist;
    String selectedvalue;

    AppController appController;
    RecyclerView recyclerView;

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    IntentFilter intentFilter;
    MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_history);

        historydatabaseprocess = true;
        listclickable = true;
        db = new DatabaseHandlerClass(this);
        appController = (AppController) getApplication();
        dataGetterSetterClass = appController.getDataGetterSetterClass();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(CONNECTIVITY_ACTION);
            receiver = new MyReceiver();
        }

        listitems = new ArrayList<String>();
        datelist = new ArrayList<String>();

        Cursor cursor = db.getColumnValueList("date_and_time");
        StringBuffer buf = new StringBuffer();

        if (cursor.moveToFirst()) {

            do {

                Log.e("name--->", "" + cursor.getString(0));


                buf.append(cursor.getString(0) + ",");
                listitems.add(cursor.getString(0));
                datelist.add(cursor.getString(0));

            } while (cursor.moveToNext());
            Log.e("Database column list: ", "" + buf);


        } else {

            listitems.add("No updates available");
            listclickable = false;
        }

        historytoolbar = (Toolbar) findViewById(R.id.history_toolbar);
        setSupportActionBar(historytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("History List");


        historylist = (ListView) findViewById(R.id.history_list);
        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(HistoryClass.this);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list, android.R.id.text1, listitems);
        historylist.setAdapter(adapter);


        recyclerView.setLayoutManager(linearLayoutManager);
        myRecyclerview myRecyclerview=new myRecyclerview(getApplicationContext(),listitems);
        recyclerView.setAdapter(myRecyclerview);


        historylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listclickable) {
                    selectedvalue = datelist.get(position);
                    appController.setSelectedhistoryvalue(selectedvalue);
                    historydatabaseprocess = false;
                    Intent intent = new Intent(getApplicationContext(), HistoryDetailsClass.class);
                    startActivity(intent);
                }
            }
        });


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
        historydatabaseprocess = false;
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unregisterReceiver(receiver);
        }


        if (historydatabaseprocess) {
            if (db.isExist(dataGetterSetterClass.get_DateAndTime())) {

            } else {
                intconnectivity = checkConnection();
                if (intconnectivity) {
                    appController.PostServerURLMethod("", dataGetterSetterClass);
                } else {
                    appController.LocalStorageMethod(dataGetterSetterClass, "No");
                }
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionOfIntent = intent.getAction();
            boolean isConnected = checkConnection();
            if (actionOfIntent.equals(CONNECTIVITY_ACTION)) {
                if (isConnected) {
                    JSONArray resarr = db.getNotUpdatedDetails();
                    if (resarr != null) {
                        appController.PostAllDataToServer(resarr);
                    }
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        historydatabaseprocess = true;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(receiver, intentFilter);
        } else {
            AppController.getInstance().setConnectivityListener(this);
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            JSONArray resarr = db.getNotUpdatedDetails();
            if (resarr != null) {
                appController.PostAllDataToServer(resarr);
            }
        }
    }

    class myRecyclerview extends RecyclerView.Adapter<myRecyclerview.custom> {



        Context context;
        List<String> listdat;

        public myRecyclerview(Context context,List<String> items) {

            this.listdat=items;
            this.context = context;
        }

        @Override
        public myRecyclerview.custom onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_adapter, parent, false);

            custom custom = new custom(view);
            return custom;

        }

        @Override
        public void onBindViewHolder(myRecyclerview.custom holder, int position) {
                      holder.text1.setText(listdat.get(position));

            }

        @Override
        public int getItemCount() {
            return listdat.size();
        }

        class custom extends RecyclerView.ViewHolder {
            TextView text1;

            public custom(View itemView) {
                super(itemView);
                text1 = (TextView) itemView.findViewById(R.id.text11);

            }
        }


    }
}
