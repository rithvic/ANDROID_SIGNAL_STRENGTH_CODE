package com.quadrobay.qbayApps.networkproject.ActivityClasses;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.quadrobay.qbayApps.networkproject.Services.ConnectivityReceiver;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DataGetterSetterClass;
import com.quadrobay.qbayApps.networkproject.DataBaseClasses.DatabaseHandlerClass;
import com.quadrobay.qbayApps.networkproject.TabPages.FifthTabPage;
import com.quadrobay.qbayApps.networkproject.TabPages.FirstTabPage;
import com.quadrobay.qbayApps.networkproject.TabPages.FourthTabPage;
import com.quadrobay.qbayApps.networkproject.Services.JobSchedulerService;
import com.quadrobay.qbayApps.networkproject.R;
import com.quadrobay.qbayApps.networkproject.TabPages.SecondTabPage;
import com.quadrobay.qbayApps.networkproject.TabPages.ThirdTabPage;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.model.LatLng;
import com.quadrobay.qbayApps.networkproject.SplashScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements LocationListener, ConnectivityReceiver.ConnectivityReceiverListener,View.OnClickListener {

    //Identifier for whether need for database access
    Boolean startUp;
    Boolean databaseprocess;
    Boolean intconnectivity;

    //UI variables
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomNavigationView navigationview;
    private TelephonyManager telephonymanager;
    public Context context;

    LinearLayout dash_layout,history_layout,report_layout,settings_layout,logout_layout,feedback_layout;
    //Common class objects
    public static DataGetterSetterClass sdataGetterSetterClass;
    AppController appController;
    DatabaseHandlerClass db;

    //Slide View variables
    private DrawerLayout dlayout;

    OpenCellID opencellid;
    LocationManager locationManager;
    Geocoder geocoder;

    int position;
    //Permissions
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private String[] permissionsRequired = new String[]{Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    IntentFilter intentFilter;
    MyReceiver receiver;

    JobScheduler mJobScheduler;
    private static final long REFRESH_INTERVAL  = 5 * 1000;

    SharedPreferences sharedPreferences;
    PopupWindow popupWindow;
    View mCustomView;
    DrawerLayout mDrayerLayout;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    MenuItem prevItem;

    TextView logTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            MainActivity.this.startService(new Intent(getApplicationContext(),MainActivity.class));

        }
        else {
            MainActivity.this.startForegroundService(new Intent(getApplicationContext(), MainActivity.class));

        }

        logTxtView = (TextView) findViewById(R.id.log_txt_view);
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, SplashScreen.class);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        // Obtain the shared Tracker instance.
        AppController application = (AppController) getApplication();

        ((AppController) getApplication()).getAppTracker();

        startUp = true;
        databaseprocess = true;

        // Inflate the custom layout/view
        mDrayerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mCustomView = layoutInflater.inflate(R.layout.pop_up_screen,null);
        mCustomView.setClipToOutline(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        popupWindow = new PopupWindow(mCustomView,width-40,height-200);
        popupWindow.setAnimationStyle(android.R.style.Animation_Toast);
        popupWindow.setElevation(5);

        popupWindow.setWidth(width-60);
        popupWindow.setHeight(height-60);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setFocusable(true);
        Display display = getWindowManager().getDefaultDisplay();


        db = new DatabaseHandlerClass(this);
        appController = (AppController) getApplication();
        geocoder = new Geocoder(this, Locale.getDefault());

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

        sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
        if (!sharedPreferences.contains("Network_thresold")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Network_thresold",3);
            editor.commit();
        }

        intconnectivity = checkConnection();
        if (intconnectivity) {
            JSONArray resarr = db.getNotUpdatedDetails();
            if (resarr != null) {
              //  ShowSnackBar("Updating database to Server");
                appController.PostAllDataToServer(resarr);
            }else {
               // ShowSnackBar("No updates");
            }
        }



        //Connectivity service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(CONNECTIVITY_ACTION);
            receiver = new MyReceiver();
        }
      //  ShowSnackBar("Creating Background Service");
        Log.e("Background service", "onCreate: ");
        long interval = TimeUnit.SECONDS.toMillis(5); // every 6 hours
        long flex = TimeUnit.SECONDS.toMillis(3); // wait 3 hours before job runs again
      //  ShowSnackBar("Scheduling Background Service");
        mJobScheduler = (JobScheduler)
                getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder jobbuilder = new JobInfo.Builder(1,
                new ComponentName(getPackageName(),
                        JobSchedulerService.class.getName()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobbuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(3));
            jobbuilder.setPersisted(true);
        }else {

            jobbuilder.setPeriodic(600000);
            jobbuilder.setPersisted(true);

        }
       // ShowSnackBar("Scheduled Background Service");
        jobbuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        if (mJobScheduler.schedule(jobbuilder.build()) <= JobScheduler.RESULT_FAILURE) {
          //  ShowSnackBar("Could not start Background Service");
            Log.e("Background service", "onCreate: Some error while scheduling the job");
        }else{
         //   ShowSnackBar("Started Background Service");
            Log.e("Background service", "onCreate: Successfully created job service");
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateformatstr = dateFormat.format(calendar.getTime());
        String deviceName = Build.MODEL;
        String deviceMan = Build.MANUFACTURER;
        String productstr = Build.BRAND;

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Device_ID_TextView)).setText(id);
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Date_And_Time_TextView)).setText(dateformatstr);
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Mobile_Product_TextView)).setText(productstr);
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Mobile_Model_TextView)).setText(deviceName);

        //Set default values to DataGetterSetterClass
        sdataGetterSetterClass = new DataGetterSetterClass();
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

        //Checking background service
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

     //Checks for permission in shared preference. If accepted, proceed or ask permission
        permissionStatus = getSharedPreferences("PermissionStatus", MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])) {

                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
             }
              else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();
            }
        } else {
            proceedAfterPermission();
        }
    }

    public void proceedAfterPermission() {
        telephonymanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        navigationview = (BottomNavigationView) findViewById(R.id.navigation);

 if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           // ShowSnackBar("location Service not enabled");
            sdataGetterSetterClass.set_UserLatitude("Requires location access");
            sdataGetterSetterClass.set_UserLongitude("Requires location access");
            sdataGetterSetterClass.set_UserArea("Requires location access");
            sdataGetterSetterClass.set_UserCity("Requires location access");
            sdataGetterSetterClass.set_UserState("Requires location access");
            sdataGetterSetterClass.set_UserCountry("Requires location access");

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            FusedLocationProviderClient mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);;
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
              dialogalert("Location Required","This application requires Location. Please enable Location");
            }
            else {
           Log.e("","");
           }
        }





        GetTowerLocation();

        BottomNavigationViewHelper.disableShiftMode(navigationview);
        navigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_dashboard:

                        viewPager.setCurrentItem(0);

                        position=0;
                 break;
                    case R.id.main_map:
                        position=2;
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.main_device:
                        viewPager.setCurrentItem(4);
                        position=4;
                        break;
                    case R.id.main_wifi:
                        position=1;
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.test_speed:

                        position=3;
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initNavigationDrawer();
        setupViewPager(viewPager);

    }

    public void initNavigationDrawer() {
        NavigationView slidenavigation = (NavigationView) findViewById(R.id.navigation_view);


        View header = slidenavigation.getHeaderView(0);

        dash_layout=header.findViewById(R.id.dashlayout);
        history_layout=header.findViewById(R.id.historylayout);
        report_layout=header.findViewById(R.id.currentreportlayout);
        settings_layout=header.findViewById(R.id.settinglayout);
        logout_layout=header.findViewById(R.id.logoutlayout);
        feedback_layout=header.findViewById(R.id.feedbacklayout);


        dash_layout.setOnClickListener(this);
        history_layout.setOnClickListener(this);
        report_layout.setOnClickListener(this);
        settings_layout.setOnClickListener(this);
        logout_layout.setOnClickListener(this);
        feedback_layout.setOnClickListener(this);


        slidenavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.home:
                        viewPager.setCurrentItem(0);
                        dlayout.closeDrawers();

                        break;
                    case R.id.history:
                        databaseprocess = false;
                        Intent intent = new Intent(getApplicationContext(), HistoryClass.class);
                        appController.setDataGetterSetterClass(sdataGetterSetterClass);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        dlayout.closeDrawers();
                        break;
                    case R.id.report:
                        currentReport();
                        dlayout.closeDrawers();
                        break;

                    case R.id.settings:
                        databaseprocess = false;
                        Intent settingintent = new Intent(getApplicationContext(), SettingsClass.class);
                        appController.setDataGetterSetterClass(sdataGetterSetterClass);
                        startActivity(settingintent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        dlayout.closeDrawers();
                        break;
                    case R.id.logout:
                        finish();
                        break;
                    case R.id.nav_feedback:
                        databaseprocess = false;
                        Intent feedbackintent = new Intent(getApplicationContext(), FeedbackClass.class);
                        appController.setDataGetterSetterClass(sdataGetterSetterClass);
                        startActivity(feedbackintent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        dlayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
        dlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, dlayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        dlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.menu);

        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dlayout.isDrawerOpen(GravityCompat.START)) {
                    dlayout.closeDrawer(GravityCompat.START);
                } else {
                    dlayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dashlayout:

                viewPager.setCurrentItem(0);

         dlayout.closeDrawers();

                break;
            case R.id.historylayout:
                databaseprocess = false;
                Intent intent = new Intent(getApplicationContext(), HistoryClass.class);
                appController.setDataGetterSetterClass(sdataGetterSetterClass);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                dlayout.closeDrawers();
                break;
            case R.id.currentreportlayout:
                currentReport();
                dlayout.closeDrawers();
                break;
          case R.id.settinglayout:
                //Toast.makeText(getApplicationContext(), "Development in process", Toast.LENGTH_SHORT).show();
                databaseprocess = false;
                Intent settingintent = new Intent(getApplicationContext(), SettingsClass.class);
                appController.setDataGetterSetterClass(sdataGetterSetterClass);
                startActivity(settingintent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                dlayout.closeDrawers();
                break;
            case R.id.logoutlayout:
                finish();
                break;
            case R.id.feedbacklayout:
                databaseprocess = false;
                Intent feedbackintent = new Intent(getApplicationContext(), FeedbackClass.class);
                appController.setDataGetterSetterClass(sdataGetterSetterClass);
                startActivity(feedbackintent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                dlayout.closeDrawers();
                break;
        }


    }

    private void currentReport() {
        popupWindow.showAtLocation(mDrayerLayout, Gravity.CENTER,0,0);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateformatstr = dateFormat.format(calendar.getTime());
        sdataGetterSetterClass.set_DateAndTime(dateformatstr);

        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim1_Net_Operator_TextView)).setText(getDataGetterSetterClass().get_NetOperator());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim1_Net_Type_TextView)).setText(getDataGetterSetterClass().get_NetType());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim1_Net_Strength_TextView)).setText(getDataGetterSetterClass().get_NetStrength());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_sim1_Net_Country_TextView)).setText(getDataGetterSetterClass().get_NetCountry());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim1_Operator_TextView)).setText(getDataGetterSetterClass().get_SimOperator());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim1_Country_TextView)).setText(getDataGetterSetterClass().get_SimCountry());

        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim2_Net_Operator_TextView)).setText(getDataGetterSetterClass().get_SecondSimNetOperator());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim2_Net_Type_TextView)).setText(getDataGetterSetterClass().get_SecondSimNetType());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim2_Net_Strength_TextView)).setText(getDataGetterSetterClass().get_SecondSimNetStrength());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim2_Net_Country_TextView)).setText(getDataGetterSetterClass().get_SecondSimNetCountry());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim2_Operator_TextView)).setText(getDataGetterSetterClass().get_SecondSimSimOperator());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Sim2_Country_TextView)).setText(getDataGetterSetterClass().get_SecondSimSimCountry());

        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_WIFI_SSID_TextView)).setText(getDataGetterSetterClass().get_WIFISSID());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_WIFI_Speed_TextView)).setText(getDataGetterSetterClass().get_WIFISpeed());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_WIFI_Strength_TextView)).setText(getDataGetterSetterClass().get_WIFIStrength());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_WIFI_Frequency_TextView)).setText(getDataGetterSetterClass().get_WIFIFrequency());

        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_User_Area_TextView)).setText(getDataGetterSetterClass().get_UserArea());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_User_City_TextView)).setText(getDataGetterSetterClass().get_UserCity());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_User_State_TextView)).setText(getDataGetterSetterClass().get_UserState());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_User_Country_TextView)).setText(getDataGetterSetterClass().get_UserCountry());
        ((TextView)popupWindow.getContentView().findViewById(R.id.Current_Tower_Address_TextView)).setText(getDataGetterSetterClass().get_TowerAddress());


        intconnectivity = checkConnection();
        if (db.isExist(sdataGetterSetterClass.get_DateAndTime())){

        }else {
            if (intconnectivity) {
                appController.PostServerURLMethod("", sdataGetterSetterClass);
            } else {
                appController.LocalStorageMethod(sdataGetterSetterClass, "No");
            }
        }

        ((ImageView)popupWindow.getContentView().findViewById(R.id.Close_Btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
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
        List<CellInfo> cellLocation = telephonymanager.getAllCellInfo();
        if (cellLocation != null) {
            for (int i = 0; i < cellLocation.size(); ++i) {

                CellInfo cellinfo = cellLocation.get(i);
                Log.d("CellLocation", cellinfo.toString());
                if (cellinfo instanceof CellInfoGsm) {

                    CellIdentityGsm identityGsm = ((CellInfoGsm) cellinfo).getCellIdentity();

                    String networkOperator = telephonymanager.getNetworkOperator();
                    if (!"".equals(networkOperator)) {
                        String gsmnetmcc = networkOperator.substring(0, 3);
                        String gsmnetmnc = networkOperator.substring(3);
                        //  }

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
                    String networkOperator = telephonymanager.getNetworkOperator();

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
                    //   Log.d("CellLocation", "lac", String.valueOf(identityGsm.getLac()));

                    String networkOperator = telephonymanager.getNetworkOperator();

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

                    String networkOperator = telephonymanager.getNetworkOperator();

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
        }else {
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

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://us2.unwiredlabs.com/v2/process.php", finalreqobj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Server Res", response.toString());
                            //    callback.ReturnedResponseFromServer(response);

                            LatLng towerlocation = null;
                            try {
                                towerlocation = new LatLng(response.getDouble("lat"), response.getDouble("lon"));
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

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
        if (permsRequestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            proceedAfterPermission();
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.ACTION_LOCALE_SETTINGS);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission proceed
                proceedAfterPermission();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();

            }
        }
    }

    private  void setupViewPager(ViewPager viewPager){
       ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstTabPage(), "Signal Strength");
        adapter.addFragment(new SecondTabPage(), "Wifi Info");
        adapter.addFragment(new ThirdTabPage(), "Map View");
        adapter.addFragment(new FifthTabPage(), "Speed Test");
        adapter.addFragment(new FourthTabPage(), "Device Info");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(mFragmentTitleList.get(position));

                if (prevItem != null){
                    prevItem.setChecked(false);
                }else {
                    navigationview.getMenu().getItem(0).setChecked(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    // Method to manually check connection status
    private Boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }


    class ViewPageAdapter extends FragmentPagerAdapter {
        public  ViewPageAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public DataGetterSetterClass getDataGetterSetterClass(){
        return this.sdataGetterSetterClass;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unregisterReceiver(receiver);
        }
        if (databaseprocess) {

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

    @Override
    protected void onResume() {
        super.onResume();
        databaseprocess = true;
        // register connection status listener
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(receiver, intentFilter);
        }else {
            AppController.getInstance().setConnectivityListener(this);
            AppController.getInstance().setConnectivityListener(this);
            AppController.getInstance().setConnectivityListener(this);
            }
        proceedAfterPermission();
        if (position==0){
            viewPager.setCurrentItem(0);

        }else if (position==1){
            viewPager.setCurrentItem(1);
        }else if (position==2){
            viewPager.setCurrentItem(2);
        }else if (position==3){
            viewPager.setCurrentItem(3);
        }else if (position==4){
            viewPager.setCurrentItem(4);
        }
    }

    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    item.setPadding(0, 15, 0, 0);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

    public void dialogalert(final String title, final String msg){

                    new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme)
                            .setTitle(title)
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("Go to Permission", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    dialog.dismiss();
                                    startActivity(viewIntent);
                                    }
                            }
                            ).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            System.exit(1);

                        }
                    }).show();
    }

}
