package com.quadrobay.qbayApps.networkproject.ActivityClasses;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.quadrobay.qbayApps.networkproject.R;
import com.quadrobay.qbayApps.networkproject.SplashScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sairaj on 13/11/17.
 */

public class SettingsClass extends AppCompatActivity{

    int i=0;
    Context context;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;

    public static final String thresholdkey = "networkthershold";

    private Toolbar settingsdetailstoolbar;
    Spinner spinner;
    SharedPreferences sharedPreferences;

    TextView locationservicetxtview;
    TextView locationaccesstxtview;

    ImageView locationserviceswitch;
    ImageView locationaccessswitch;
    ImageView hideiconswitch;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        context = this;

        sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
        settingsdetailstoolbar = (Toolbar) findViewById(R.id.settings_details_toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);

        locationservicetxtview = (TextView) findViewById(R.id.settings_location_service_txtview);
        locationaccesstxtview = (TextView) findViewById(R.id.settings_location_access_txtview);

        locationserviceswitch = (ImageView) findViewById(R.id.setting_location_service_switch);
        locationaccessswitch = (ImageView) findViewById(R.id.settings_location_access_switch);
        hideiconswitch = (ImageView) findViewById(R.id.settings_hide_icon_switch);

        setSupportActionBar(settingsdetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Settings");

        List<String> categories = new ArrayList<String>();
        categories.add("10% or below");
        categories.add("25% or below");
        categories.add("50% or below");
        categories.add("75% or below");
        categories.add("100% or below");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_dropdown_item, categories);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(sharedPreferences.getInt("Network_thresold",0));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Network_thresold",4);
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

            locationservicetxtview.setText("Location service not enabled");
            locationserviceswitch.setImageDrawable(getApplicationContext().getDrawable(R.drawable.off));

        }else {
            locationservicetxtview.setText("Location service enabled");
            locationserviceswitch.setImageDrawable(getApplicationContext().getDrawable(R.drawable.on));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // infoview.setText("Location access is required. Please enable location access to continue");
            // mPopupWindow.showAtLocation(lay,Gravity.CENTER,0,0);

            locationaccesstxtview.setText("Location access not enabled");
            locationaccessswitch.setImageDrawable(getApplicationContext().getDrawable(R.drawable.off));
             return;
        }else {

            locationaccesstxtview.setText("Location access enabled");
            locationaccessswitch.setImageDrawable(getApplicationContext().getDrawable(R.drawable.on));

                 }

        locationserviceswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i==0) {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }

            }
        } );

        locationaccessswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (i==0) {

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, PERMISSION_CALLBACK_CONSTANT);
                }

            }
        });

        hideiconswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    PackageManager p = getPackageManager();
                    ComponentName componentName = new ComponentName(context, SplashScreen.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
                    p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

            locationservicetxtview.setText("Location service not enabled");
            locationserviceswitch.setImageDrawable(getApplicationContext().getDrawable(R.drawable.off));


        }else {
            locationservicetxtview.setText("Location service enabled");
            locationserviceswitch.setImageDrawable(getApplicationContext().getDrawable(R.drawable.on));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // infoview.setText("Location access is required. Please enable location access to continue");
            // mPopupWindow.showAtLocation(lay,Gravity.CENTER,0,0);

            locationaccesstxtview.setText("Location access not enabled");
            locationaccessswitch.setImageDrawable(getApplicationContext().getDrawable(R.drawable.off));



            return;
        }else {

            locationaccesstxtview.setText("Location access enabled");
            locationaccessswitch.setImageDrawable(getApplicationContext().getDrawable(R.drawable.on));
        }
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

}
