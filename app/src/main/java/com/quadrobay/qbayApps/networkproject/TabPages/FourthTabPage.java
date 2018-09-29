package com.quadrobay.qbayApps.networkproject.TabPages;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quadrobay.qbayApps.networkproject.Customadapter;
import com.quadrobay.qbayApps.networkproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by benchmark on 05/10/17.
 */

public class FourthTabPage extends Fragment implements GLSurfaceView.Renderer,View.OnClickListener {

    TextView textviewgpurender, textviewgpuversion, textviewgpuextension, textviewgpuvendor, textviewfingerprint, textviewbootloader, textviewbattlevel, textviewbattplug, textviewbatthealth, textviewbatttech, textviewtemptxt, textviewbattvolts, textViewRelase, textviewcodenameos, textviewbaseos, textViewsdk, textViewBrand, textViewDisplay, textViewId, textViewManufacture, textViewModel, textViewProduct, textViewSerial, textViewType, textViewUser, batterylvltxtview;
    //  DonutProgress batteryProgress;
    TelephonyManager tMgr;
    BatteryReceiver batteryReceiver;
    private TextView textView;

    ListView listViewone;
    RecyclerView listViewtwo;
    ImageView versionimageview;

    ArrayList<String> cpuinfos;
    LinearLayout cpuinfolayout;
    LinearLayout sensorinfolayout;
    private GLSurfaceView glSurfaceView;

    List<Sensor> sensor;
    SensorManager smm;

    LinearLayout linearone, lineartwo, linearthree, linearfour, linearfive;
    ImageView imgone, imgtwo, imgthree, imgfour, imgfive;
    LinearLayout layoutone, layouttwo, layoutthree;

    //  RoundCornerProgressBar batteryprogressbar;

    public FourthTabPage() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fouth_tab_page, container, false);
        cpuinfolayout = (LinearLayout) view.findViewById(R.id.listlayout1);
        sensorinfolayout = (LinearLayout) view.findViewById(R.id.listlayout2);
        listViewone = view.findViewById(R.id.listview1);
        listViewtwo= view.findViewById(R.id.listview2);
        this.glSurfaceView = new GLSurfaceView(getActivity());
        this.glSurfaceView.setRenderer(this);


        smm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = smm.getSensorList(Sensor.TYPE_ALL);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        listViewtwo.setLayoutManager(linearLayoutManager);
                    Customadapter adapt=new Customadapter(getActivity(),sensor);
                    listViewtwo.setAdapter(adapt);

        cpuinfos = new ArrayList<String>();

        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> dataa2= new ArrayList<>();

        String value1[];
        String value2[];


        Log.e("Cpu info", getInfo());
        if (cpuinfos != null || cpuinfos.size() != 0) {
            for (int i = 0; i <= cpuinfos.size() - 1; i++) {
                String info = cpuinfos.get(i);
                if ("".equals(info)) {

                } else {
                    String[] separated = info.split(":");

                    TextView emptyview = new TextView(getActivity());
                    TextView cpukeytxtview = new TextView(getActivity());
                    TextView cpuvaluetextview = new TextView(getActivity());
                    cpukeytxtview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    cpuvaluetextview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    emptyview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    cpukeytxtview.setTextColor(Color.parseColor("#0575E6"));
                    cpuvaluetextview.setTextColor(Color.parseColor("#000000"));
                    cpukeytxtview.setTextSize(12);
                    cpuvaluetextview.setTextSize(12);
                    cpukeytxtview.setText(separated[0]);
                    cpuvaluetextview.setText(separated[1]);
                    data1.add(separated[0]);
                    dataa2.add(separated[1]);

                    emptyview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    emptyview.setText("");
             }

            }
        }

        TextViewAdpater adpater = new TextViewAdpater(getActivity().getApplicationContext(), data1,dataa2);
        listViewone.setAdapter(adpater);
        linearone = view.findViewById(R.id.linear1);
        lineartwo = view.findViewById(R.id.linear2);
        linearthree = view.findViewById(R.id.linear3);
        linearfour = view.findViewById(R.id.linear4);
        linearfive = view.findViewById(R.id.linear5);
        imgone = view.findViewById(R.id.image1);
        imgtwo = view.findViewById(R.id.image2);
        imgthree = view.findViewById(R.id.image3);
        imgfour = view.findViewById(R.id.image4);
        imgfive = view.findViewById(R.id.image5);
        layoutone = view.findViewById(R.id.data1);
        layouttwo = view.findViewById(R.id.data2);
        layoutthree = view.findViewById(R.id.data3);
        linearone.setOnClickListener(this);
        lineartwo.setOnClickListener(this);
        linearthree.setOnClickListener(this);
        linearfour.setOnClickListener(this);
        linearfive.setOnClickListener(this);

        layoutone.setVisibility(View.VISIBLE);
        layouttwo.setVisibility(View.GONE);
        layoutthree.setVisibility(View.GONE);
        cpuinfolayout.setVisibility(View.GONE);
        sensorinfolayout.setVisibility(View.GONE);
        linearone.setBackground(getActivity().getResources().getDrawable(R.color.white));
        lineartwo.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
        linearthree.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
        linearfour.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
        linearfive.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));

        imgone.setColorFilter(getResources().getColor(R.color.bluecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgtwo.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgthree.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgfour.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgfive.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
        textViewRelase = (TextView) view.findViewById(R.id.release_version);
        textviewtemptxt = (TextView) view.findViewById(R.id.battery_tmp_text_view);
        textviewbatttech = (TextView) view.findViewById(R.id.battery_tech_text_view);
        textviewbatthealth = (TextView) view.findViewById(R.id.battery_health_text_view);
        textviewbattplug = (TextView) view.findViewById(R.id.battery_plugged_text_view);
        textviewbattlevel = (TextView) view.findViewById(R.id.battery_level_text_view);
        textviewbattvolts = (TextView) view.findViewById(R.id.battery_volts_text_view);
        textviewbaseos = (TextView) view.findViewById(R.id.base_os_info_txtview);
        textviewcodenameos = (TextView) view.findViewById(R.id.os_code);
        textViewsdk = (TextView) view.findViewById(R.id.sdk_version);
        textViewBrand = (TextView) view.findViewById(R.id.brand);
        textViewId = (TextView) view.findViewById(R.id.deviceid);
        textViewManufacture = (TextView) view.findViewById(R.id.manufacture);
        textViewModel = (TextView) view.findViewById(R.id.model);
        textViewProduct = (TextView) view.findViewById(R.id.product);
        textViewSerial = (TextView) view.findViewById(R.id.serial);
        textViewType = (TextView) view.findViewById(R.id.type);
        textViewUser = (TextView) view.findViewById(R.id.user);
        textviewbootloader = (TextView) view.findViewById(R.id.bootloader_txt_view);
        textviewfingerprint = (TextView) view.findViewById(R.id.fingerprint_txt_view);

        batteryReceiver = new BatteryReceiver();
        IntentFilter batteryLevelFliter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(batteryReceiver, batteryLevelFliter);

        tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        textViewRelase.setText("" + Build.VERSION.RELEASE);
        textViewsdk.setText("" + Build.VERSION.SDK_INT);
        textviewcodenameos.setText("" + Build.VERSION.CODENAME);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return TODO;
                textViewSerial.setText("Requires phone state permission");
            } else {
                String deviceserial = Build.getSerial();
                textViewSerial.setText(deviceserial);
            }
        } else {
            textViewSerial.setText(Build.SERIAL);
        }


        textviewfingerprint.setText(Build.FINGERPRINT);
        textviewbootloader.setText(Build.BOOTLOADER);
        textViewBrand.setText(Build.BRAND);
        textViewId.setText(Build.ID);
        textViewManufacture.setText(Build.MANUFACTURER);
        textViewModel.setText(Build.MODEL);
        textViewProduct.setText(Build.PRODUCT);
        textViewType.setText(Build.TYPE);
        textViewUser.setText(Build.USER);
        return view;
    }

    private String getInfo() {
        StringBuffer sb = new StringBuffer();
        String abilist[] = Build.SUPPORTED_ABIS;
        sb.append("abi: ").append(abilist[0]).append("n");
        if (new File("/proc/cpuinfo").exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String aLine;
                while ((aLine = br.readLine()) != null) {
                    sb.append(aLine + "n");
                    cpuinfos.add(aLine);
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //Battery Status Methods
    private String getStatusString(int status) {
        String statusString = "Unknown";

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not Charging";
                break;
        }

        return statusString;
    }

    private String getHealthString(int health) {
        String healthString = "Unknown";

        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Over Heat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                break;
        }

        return healthString;
    }

    private String getPlugTypeString(int plugged) {
        String plugType = "Unknown";

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = "USB";
                break;
        }

        return plugType;
    }

    private double CelisiusToFernheit(double c) {
        return (c * 9) / 5 + 32;
    }

    public void onPause() {
        getActivity().unregisterReceiver(batteryReceiver);
        super.onPause();
    }

    public void onResume() {
        getActivity().registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onResume();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.linear1:
                linearone.setBackground(getActivity().getResources().getDrawable(R.color.white));
                lineartwo.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearthree.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearfour.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearfive.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));


                layoutone.setVisibility(View.VISIBLE);
                layouttwo.setVisibility(View.GONE);
                layoutthree.setVisibility(View.GONE);
                cpuinfolayout.setVisibility(View.GONE);
                sensorinfolayout.setVisibility(View.GONE);


                imgone.setColorFilter(getResources().getColor(R.color.bluecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgtwo.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgthree.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfour.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfive.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);


                break;
            case R.id.linear2:
                linearone.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                lineartwo.setBackground(getActivity().getResources().getDrawable(R.color.white));
                linearthree.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearfour.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearfive.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));

                imgone.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgtwo.setColorFilter(getResources().getColor(R.color.bluecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgthree.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfour.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfive.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);


                layoutone.setVisibility(View.GONE);
                layouttwo.setVisibility(View.VISIBLE);
                layoutthree.setVisibility(View.GONE);
                cpuinfolayout.setVisibility(View.GONE);
                sensorinfolayout.setVisibility(View.GONE);


                break;

            case R.id.linear3:
                linearone.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                lineartwo.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearthree.setBackground(getActivity().getResources().getDrawable(R.color.white));
                linearfour.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearfive.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));

                imgone.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgtwo.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgthree.setColorFilter(getResources().getColor(R.color.bluecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfour.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfive.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);

                layoutone.setVisibility(View.GONE);
                layouttwo.setVisibility(View.GONE);
                layoutthree.setVisibility(View.VISIBLE);
                cpuinfolayout.setVisibility(View.GONE);
                sensorinfolayout.setVisibility(View.GONE);


                break;
            case R.id.linear4:

                linearone.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                lineartwo.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearthree.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearfour.setBackground(getActivity().getResources().getDrawable(R.color.white));
                linearfive.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));

                imgone.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgtwo.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgthree.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfour.setColorFilter(getResources().getColor(R.color.bluecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfive.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);


                layoutone.setVisibility(View.GONE);
                layouttwo.setVisibility(View.GONE);
                layoutthree.setVisibility(View.GONE);
                cpuinfolayout.setVisibility(View.VISIBLE);
                sensorinfolayout.setVisibility(View.GONE);


                break;
            case R.id.linear5:

                linearone.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                lineartwo.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearthree.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearfour.setBackground(getActivity().getResources().getDrawable(R.drawable.bluebox));
                linearfive.setBackground(getActivity().getResources().getDrawable(R.color.white));


                imgone.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgtwo.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgthree.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfour.setColorFilter(getResources().getColor(R.color.whitecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgfive.setColorFilter(getResources().getColor(R.color.bluecolor), android.graphics.PorterDuff.Mode.MULTIPLY);


                layoutone.setVisibility(View.GONE);
                layouttwo.setVisibility(View.GONE);
                layoutthree.setVisibility(View.GONE);
                cpuinfolayout.setVisibility(View.GONE);
                sensorinfolayout.setVisibility(View.VISIBLE);

                break;
        }


    }

    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int rawlevel = -1;
            if (level >= 0 && scale > 0) {
                rawlevel = (level * 100) / scale;
            }

            float batteryvoltage = (float) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0));
            float temperature = (float) (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
            double temperaturefernheit = CelisiusToFernheit(temperature);
            int batterytemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            String technology = intent.getStringExtra("technology");
            int plugged = intent.getIntExtra("plugged", -1);
            int health = intent.getIntExtra("health", 0);

            textviewbattlevel.setText(String.valueOf(rawlevel) + " %");
            textviewbatthealth.setText(getHealthString(health));
            textviewbattplug.setText(getPlugTypeString(plugged));
            textviewbatttech.setText(technology);
            textviewbattvolts.setText(String.valueOf(batteryvoltage) + "mV");
            textviewtemptxt.setText(String.valueOf(temperature) + Character.toString((char) 176) + " C (" + temperaturefernheit + "F)");
            int chargingstatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = chargingstatus == BatteryManager.BATTERY_STATUS_CHARGING;

            if (isCharging) {

            } else {

            }

        }
    }

    class TextViewAdpater extends ArrayAdapter<String> {

        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> dataa2= new ArrayList<>();
        Context context;
        public TextViewAdpater(@NonNull Context context, ArrayList<String> data1, ArrayList<String> data2) {
            super(context, -1, data1);

            this.context = context;
            this.data1 = data1;
            this.dataa2=data2;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflator.inflate(R.layout.cpu_layout, parent, false);
            TextView text1 = (TextView) rowView.findViewById(R.id.tex1);
            TextView text2 = (TextView) rowView.findViewById(R.id.tex2);
                        text1.setText(data1.get(position));
                        text2.setText(dataa2.get(position));

        return rowView;
        }
    }
    }