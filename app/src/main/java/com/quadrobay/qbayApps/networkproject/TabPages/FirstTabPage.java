package com.quadrobay.qbayApps.networkproject.TabPages;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
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
import com.quadrobay.qbayApps.networkproject.SingletonClass.CustomSpeedometer;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by benchmark on 03/10/17.
 */

public class FirstTabPage extends Fragment {

    PhoneStateListener phonestatelistener;
    TextView phonetypetextview, networkoperatortypetextview, simoperatortextview, networkstrengthtextview, datastatetextview, roamingtextview, simcountrytextview, networkcountrytextview, signalleveltextview, secsimsignallevel, secsimphonetypetextview, secsimnetworkoperatortypetextview, secsimsimoperatortextview, secsimnetworkstrengthtextview, secsimdatastatetextview, secsimroamingtextview, secsimsimcountrytextview, secsimnetworkcountrytextview;
    TelephonyManager telephonymanager;
    TelephonyManager secondsimtelephonymanager;
    int simcount;
    SignalStrength sstrength;

    int sim1,sim2=0;

    TextView single_sim_netoperator,single_nettype,single_sim_simoperator,single_sim_netstrength,single_sim_datastate,mode_Txt_view;

    TextView double_simnetoperator,dual_simnettype,double_simoperator,dpuble_simnetworkstrength,double_simdatastate;

    LinearLayout secondviewone,secondviewthree,singlesimview;
    TextView secondviewtwo;

    ImageView advmodeoptions;

    ImageView sim1signalimage,sim2signalimage;

    LinearLayout sim1_advace,sim1_advance1,sim2_advace,sim2_advace1;
    LinearLayout double_sim_view;
    TextView secondtextview;


    String carriername;
    String simname;
    int datastatestr;
    Boolean isroaming;
    String simcountrystr;
    String networkcountry;
    public int phonetype;
    public String NetTypeStr;

    String secondsimcarriername;
    String secondsimname;
    Boolean secsimisroaming;
    String secondsimcountry;
    String secondsimnetworkcountry;
    public String secondsimnettype;

    DataGetterSetterClass dataGetterSetterClass;

    ConstraintLayout conslayout;

    PointerSpeedometer pointerSpeedometer;
    PointerSpeedometer secondsimpointerspeedometer;

    ImageView firstsimsignallevelimgview;
    ImageView secondsimsignallevelimgview;
    ImageView asu_signal_first_txt,asu_signal_second_txt;

    TextView firstsimdbmtxtview;
    TextView secondsimdbmtxtview;

    TextView firstsimasutxtview;
    TextView secondsimasutxtview;
    LinearLayout secondlayouttwo;
    LinearLayout secondlayoutone,doublesimviewlayout;

    int qualitypercent;
    TextView sim_1_strength,sim_2_strength;

    boolean mode;
    CustomSpeedometer customSpeedometer;
    public FirstTabPage() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        ConstraintLayout lay = (ConstraintLayout) inflater.inflate(R.layout.first_tab_page, container, false);
        conslayout = (ConstraintLayout) inflater.inflate(R.layout.first_tab_page, container, false);
        mode=true;
        pointerSpeedometer = (PointerSpeedometer) lay.findViewById(R.id.first_sim_speedometer);
        secondsimpointerspeedometer = (PointerSpeedometer) lay.findViewById(R.id.second_sim_speedometer);
        customSpeedometer = lay.findViewById(R.id.first_sim_speedometers);
        secondtextview=lay.findViewById(R.id.secondtextview);
        secondlayoutone=lay.findViewById(R.id.secondlayout1);
        doublesimviewlayout=lay.findViewById(R.id.doublesimview);
        sim1_advace=lay.findViewById(R.id.sim1_adavance_mode);
        sim1_advance1=lay.findViewById(R.id.sim1_advancemode1);
        sim2_advace=lay.findViewById(R.id.sim2_advance_mode);
        sim2_advace1=lay.findViewById(R.id.sim2_advancemode1);
        mode_Txt_view=lay.findViewById(R.id.mode_text_view);
        double_sim_view=lay.findViewById(R.id.doublesimview);
        advmodeoptions=lay.findViewById(R.id.modeselection);
        double_simnetoperator=lay.findViewById(R.id.doublesimnetoperator);
        dual_simnettype=lay.findViewById(R.id.doublesimnettype);
        double_simoperator=lay.findViewById(R.id.doublesimoperator);
        dpuble_simnetworkstrength=lay.findViewById(R.id.doublesimnetstrength);
        double_simdatastate=lay.findViewById(R.id.doublesimedatastate);
        advmodeoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode){
                    advmodeoptions.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.advancedmode));
                    sim1_advace.setVisibility(View.VISIBLE);
                    sim1_advance1.setVisibility(View.VISIBLE);
                    sim2_advace.setVisibility(View.VISIBLE);
                    sim2_advace1.setVisibility(View.VISIBLE);
                    mode_Txt_view.setText("Advance mode");
                    mode=false;

                }else {
                    advmodeoptions.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.normalmode));
                    sim1_advace.setVisibility(View.GONE);
                    sim1_advance1.setVisibility(View.GONE);
                    sim2_advace.setVisibility(View.GONE);
                    sim2_advace1.setVisibility(View.GONE);
                    mode_Txt_view.setText("Normal mode");
                    mode=true;

                }
            }
        });
                secondlayouttwo=lay.findViewById(R.id.secondlayout2);
                single_sim_netoperator=lay.findViewById(R.id.singlesimnetoperator);
                single_nettype=lay.findViewById(R.id.singlesimnettype);
                single_sim_simoperator=lay.findViewById(R.id.singledimoperator);
                single_sim_netstrength=lay.findViewById(R.id.singlesimnetstrength);
                single_sim_datastate=lay.findViewById(R.id.singlesimedatastate);
                sim1signalimage=lay.findViewById(R.id.signalstrenth_img);
                sim2signalimage=lay.findViewById(R.id.sim2signalstrent);
                secondviewone=(LinearLayout) lay.findViewById(R.id.secondview1);
                secondviewtwo=(TextView) lay.findViewById(R.id.secondview2);
                singlesimview=(LinearLayout)lay.findViewById(R.id.singlesimview);
                sim_1_strength=lay.findViewById(R.id.sim_1_strength);
                sim_2_strength=lay.findViewById(R.id.sim2_strength);;
                firstsimsignallevelimgview = (ImageView) lay.findViewById(R.id.firstsim_signallevel_imageview);
                secondsimsignallevelimgview = (ImageView) lay.findViewById(R.id.secondsim_signallevel_imageview);
                firstsimdbmtxtview = (TextView) lay.findViewById(R.id.firstsim_dbm_txtview);
                secondsimdbmtxtview = (TextView) lay.findViewById(R.id.secondsim_dbm_txtview);
                firstsimasutxtview = (TextView) lay.findViewById(R.id.firstsim_asu_txtview);
                secondsimasutxtview = (TextView) lay.findViewById(R.id.second_sim_asu_txtview);
                phonetypetextview = (TextView) lay.findViewById(R.id.NetworkTypeTextView);
                networkoperatortypetextview = (TextView) lay.findViewById(R.id.NetworkOperatorTextView);
                simoperatortextview = (TextView) lay.findViewById(R.id.SimOperatorTextView);
                networkstrengthtextview = (TextView) lay.findViewById(R.id.NetworkStrengthTextView);
                datastatetextview = (TextView) lay.findViewById(R.id.DataStateTextView);
                signalleveltextview = (TextView) lay.findViewById(R.id.SignalLevelTextView);
                secsimphonetypetextview = (TextView) lay.findViewById(R.id.Secon_Sim_Net_Typ);
                secsimnetworkoperatortypetextview = (TextView) lay.findViewById(R.id.Second_Sim_Net_Op);
                secsimsimoperatortextview = (TextView) lay.findViewById(R.id.Second_Sim_Operator);
                secsimnetworkstrengthtextview = (TextView) lay.findViewById(R.id.Second_Sim_Net_Strn);
                secsimdatastatetextview = (TextView) lay.findViewById(R.id.Second_Sim_Data_State);
                secsimsignallevel = (TextView) lay.findViewById(R.id.Second_Sim_Signal_Level);
                asu_signal_first_txt=(ImageView) lay.findViewById(R.id.firstsim_asu_img);
                asu_signal_second_txt=(ImageView) lay.findViewById(R.id.secondsimasuvalue);
                telephonymanager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                secondsimtelephonymanager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                dataGetterSetterClass = ((MainActivity) getActivity()).sdataGetterSetterClass;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager smcount = SubscriptionManager.from(getContext());
            simcount = smcount.getActiveSubscriptionInfoCountMax();
             if (simcount==1){
                        secondviewone.setVisibility(View.GONE);
                        secondviewtwo.setVisibility(View.GONE);
                        secondviewthree.setVisibility(View.GONE);
                        singlesimview.setVisibility(View.VISIBLE);
                        Log.d("", "onCreateView:simcount  "+simcount);
           }
        }

        try {
            Class<?> simdetectclass = Class.forName("android.telephony.TelephonyManager");
            Method simStateMethod = simdetectclass.getDeclaredMethod("getSimState", int.class);
            int sim1state = (int) simStateMethod.invoke(telephonymanager, 0);
            switch (sim1state) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    signalleveltextview.setText("Sim not inserted");
                    sim1=0;
                   break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    signalleveltextview.setText("Sim network locked");
                    sim1=0;
                   break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    signalleveltextview.setText("Sim network locked");
                    sim1=0;
                   break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    signalleveltextview.setText("Sim network locked");
                    sim1=0;
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    sim1=1;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        FirstSimMethods();
                    }else {
                        FirstSimMethod();
                    }
                    // do something
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    signalleveltextview.setText("Sim unknown");
                    sim1=0;
                    // do something
                    break;
                case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                    signalleveltextview.setText("Sim read error");
                    sim1=0;
                    break;
                case TelephonyManager.SIM_STATE_PERM_DISABLED:
                    signalleveltextview.setText("Sim permenantly disabled");
                    sim1=0;
                    break;
                case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                    signalleveltextview.setText("Sim card restricted");
                    sim1=0;
                    break;
            }

            if (simcount == 2) {

                int sim2state = (int) simStateMethod.invoke(telephonymanager, 1);

                switch (sim2state) {
                    case TelephonyManager.SIM_STATE_ABSENT:
                        secsimsignallevel.setText("Sim not inserted");
                        sim2=0;
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                        secsimsignallevel.setText("Sim network locked");
                        sim2=0;
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                        secsimsignallevel.setText("Sim network locked");
                        sim2=0;
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                        secsimsignallevel.setText("Sim network locked");
                        sim2=0;
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_READY:
                        SeconSimMethod();
                        sim2=1;
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_UNKNOWN:
                        secsimsignallevel.setText("Sim unknown");
                        sim2=0;
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                        secsimsignallevel.setText("Sim read error");
                        sim2=0;
                        break;
                    case TelephonyManager.SIM_STATE_PERM_DISABLED:
                        secsimsignallevel.setText("Sim permenantly disabled");
                        sim2=0;
                        break;
                    case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                        secsimsignallevel.setText("Sim card restricted");
                        sim2=0;
                        break;
                }
            }else{
                secsimsignallevel.setText("Sim 2 not available");
                sim2=0;
                      }

            if (sim1!=0 && sim2!=0){

            Log.e("","");

            }else if (sim1==0 && sim2!=0){
              secondlayoutone.setVisibility(View.GONE);
             secondtextview.setVisibility(View.GONE);
             secondlayouttwo.setVisibility(View.GONE);
             doublesimviewlayout.setVisibility(View.VISIBLE);
                //show second only
            }else if (sim1!=0 && sim2==0){
                //show first sim
                secondlayouttwo.setVisibility(View.GONE);
                singlesimview.setVisibility(View.VISIBLE);
                secondviewtwo.setVisibility(View.GONE);
                secondviewone.setVisibility(View.GONE);


            }else if (sim1==0 && sim2==0){

                Log.e("","");
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

        return lay;
    }

    public void FirstSimMethod() {

        phonetype = telephonymanager.getNetworkType();
        carriername = telephonymanager.getNetworkOperatorName();
        simname = telephonymanager.getSimOperatorName();
        datastatestr = telephonymanager.getDataState();
        isroaming = telephonymanager.isNetworkRoaming();
        simcountrystr = telephonymanager.getSimCountryIso();
        networkcountry = telephonymanager.getNetworkCountryIso();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return false;
        }else{
            String getSimSerialNumber = telephonymanager.getSimSerialNumber();
            String getSimNumber = telephonymanager.getLine1Number();
        }

        dataGetterSetterClass.set_NetOperator(carriername);
        dataGetterSetterClass.set_NetCountry(networkcountry);
        dataGetterSetterClass.set_SimOperator(simname);
        dataGetterSetterClass.set_SimCountry(simcountrystr);

        if (!simname.equals("No service")) {
            if (isroaming) {

            } else {

            }
        switch (datastatestr) {
                case 0:
                    datastatetextview.setText("Disconnected");
                    secsimdatastatetextview.setText("Disconnected");
                    single_sim_datastate.setText("Disconnected");
                    double_simdatastate.setText("Disconnected");
                    break;
                case 1:
                    datastatetextview.setText("Connecting");
                    secsimdatastatetextview.setText("Connecting");
                    single_sim_datastate.setText("Connecting");
                    double_simdatastate.setText("Connecting");
                    break;
                case 2:
                    datastatetextview.setText("Connected");
                    secsimdatastatetextview.setText("Connected");
                    single_sim_datastate.setText("Connected");
                    double_simdatastate.setText("Connected");
                    break;
                case 3:
                    datastatetextview.setText("Suspended");
                    secsimdatastatetextview.setText("Suspended");
                    single_sim_datastate.setText("Suspended");
                    double_simdatastate.setText("suspended");
                    break;
            }

            simoperatortextview.setText(simname);
            single_sim_simoperator.setText(simname);
            networkoperatortypetextview.setText(carriername);
            single_sim_netoperator.setText(carriername);

            switch (telephonymanager.getNetworkType()) {
                case 0:
                    NetTypeStr = "Unknown";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);

                    single_nettype.setText(NetTypeStr);
                    break;
                case 1:
                    NetTypeStr = "GPRS";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 2:
                    NetTypeStr = "EDGE";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 3:
                    NetTypeStr = "UMTS";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 4:
                    NetTypeStr = "CDMA";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 5:
                    NetTypeStr = "EVDO_0";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 6:
                    NetTypeStr = "EVDO_A";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 7:
                    NetTypeStr = "1xRTT";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 8:
                    NetTypeStr = "HSDPA";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);

                    break;
                case 9:
                    NetTypeStr = "HSUPA";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 10:
                    NetTypeStr = "HSPA";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 11:
                    NetTypeStr = "iDen";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 12:
                    NetTypeStr = "EVDO_B";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 13:
                    NetTypeStr = "LTE";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 14:
                    NetTypeStr = "eHRPD";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;
                case 15:
                    NetTypeStr = "HSPA+";
                    dataGetterSetterClass.set_NetType(NetTypeStr);
                    phonetypetextview.setText(NetTypeStr);
                    single_nettype.setText(NetTypeStr);
                    break;

            }

            phonestatelistener = new PhoneStateListener() {
                public void onCallForwardingIndicatorChanged(boolean cfi) {
                }

                public void onCallStateChanged(int state, String incomingNumber) {
                }

                public void onCellLocationChanged(CellLocation location) {
                }

                public void onDataActivity(int direction) {
                }

                public void onDataConnectionStateChanged(int state) {
                }

                public void onMessageWaitIndicatorChanged(boolean mwi) {
                }

                public void onServiceStateChanged(ServiceState serviceState) {
                }

                public void onSignalStrengthsChanged(SignalStrength signalStrength) {

                    sstrength = signalStrength;
                    firstsimasutxtview.setText(String.valueOf(signalStrength.getGsmSignalStrength()));
                    int asu1=signalStrength.getGsmSignalStrength();

                    Log.e("asu signal 1",""+asu1);
                    if (asu1>1 & asu1<20){

                        asu_signal_first_txt.setImageResource(R.drawable.onestrengthsignal);
                    }else if (asu1>21 & asu1<40){

                        asu_signal_first_txt.setImageResource(R.drawable.twostrengthsignal);
                    }else if (asu1>41 & asu1<60){

                        asu_signal_first_txt.setImageResource(R.drawable.threestrengthsignal);

                    }else if (asu1>61 & asu1<80){
                        asu_signal_first_txt.setImageResource(R.drawable.fourstrengthsignal);

                    }else if (asu1<81 & asu1<100){
                        asu_signal_first_txt.setImageResource(R.drawable.fullstrengthsignal);


                    }
                    int rssi = -113 + 2 * signalStrength.getGsmSignalStrength();
                    firstsimdbmtxtview.setText(String.valueOf(rssi));
                    dataGetterSetterClass.set_NetStrength(String.valueOf(rssi));

                    if (rssi <= -100) {
                        qualitypercent = 0;
                    } else if (rssi >= -50) {
                        qualitypercent = 100;
                    } else {
                        qualitypercent = 2 * (rssi + 100);
                    }

                    networkstrengthtextview.setText(qualitypercent + "%");
                    single_sim_netstrength.setText(qualitypercent + "%");
                    pointerSpeedometer.speedTo(qualitypercent);
                    sim_1_strength.setText(String.valueOf(qualitypercent));
                    int speed=qualitypercent;
                    if (speed>0 && speed<5){
                        sim1signalimage.setImageResource(R.drawable.signalzero);
                    } else if (speed>5 && speed<=15){
                        sim1signalimage.setImageResource(R.drawable.signaltwo);
                    }
                    else if (speed>15 && speed<=25){
                        sim1signalimage.setImageResource(R.drawable.signalthree);
                    }
                    else if (speed>25 && speed<=35){
                        sim1signalimage.setImageResource(R.drawable.signalfour);
                    }else if (speed>35 && speed<=45){
                        sim1signalimage.setImageResource(R.drawable.signalfive);
                    }
                    else if (speed>45 && speed<=55){
                        sim1signalimage.setImageResource(R.drawable.signalficentre);
                    }
                    else if (speed>55 && speed<=65){
                        sim1signalimage.setImageResource(R.drawable.signalsixty);
                    }else  if (speed>65&& speed<=75){
                        sim1signalimage.setImageResource(R.drawable.signaleight);
                    }else  if (speed>75&& speed<=85){
                        sim1signalimage.setImageResource(R.drawable.signalnine);
                    }else  if (speed>85&& speed<=94){
                        sim1signalimage.setImageResource(R.drawable.signalninefive);
                    }else  if (speed>94&& speed<=100){
                        sim1signalimage.setImageResource(R.drawable.signalfull);
                    }


                    int signallevel = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        signallevel = signalStrength.getLevel();
                        switch (signallevel) {
                            case 0:
                                signalleveltextview.setText("Signal level Very poor");
                                firstsimsignallevelimgview.setImageResource(R.drawable.onestrengthsignal);
                                break;
                            case 1:
                                signalleveltextview.setText("Signal level poor");
                                firstsimsignallevelimgview.setImageResource(R.drawable.twostrengthsignal);
                                break;
                            case 2:
                                signalleveltextview.setText("Signal level moderate");
                                firstsimsignallevelimgview.setImageResource(R.drawable.threestrengthsignal);
                                break;
                            case 3:
                                signalleveltextview.setText("Signal level good");
                                firstsimsignallevelimgview.setImageResource(R.drawable.fourstrengthsignal);
                                break;
                            case 4:
                                signalleveltextview.setText("Signal level Excellent");
                                firstsimsignallevelimgview.setImageResource(R.drawable.fullstrengthsignal);
                                break;

                        }
                    }
                }

            };

            try {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    dataGetterSetterClass.set_TowerAddress("Requires location access");
                    dataGetterSetterClass.set_NetStrength("Requires location access");
                    signalleveltextview.setText("Requires location access");

                } else {
                    telephonymanager.listen(phonestatelistener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_CALL_STATE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void FirstSimMethods() {

        simcountrystr = telephonymanager.getSimCountryIso();
        networkcountry = telephonymanager.getNetworkCountryIso();
        datastatestr = telephonymanager.getDataState();
        switch (datastatestr) {
            case 0:
                datastatetextview.setText("Disconnected");
                secsimdatastatetextview.setText("Disconnected");
                single_sim_datastate.setText("Disconnected");
                double_simdatastate.setText("Disconnected");

                break;
            case 1:
                datastatetextview.setText("Connecting");
                secsimdatastatetextview.setText("Connecting");
                single_sim_datastate.setText("Connecting");
                double_simdatastate.setText("Connecting");
                break;
            case 2:
                datastatetextview.setText("Connected");
                secsimdatastatetextview.setText("Connected");
                single_sim_datastate.setText("Connected");
                double_simdatastate.setText("Connected");
                break;
            case 3:
                datastatetextview.setText("Suspended");
                secsimdatastatetextview.setText("Suspended");
                single_sim_datastate.setText("Suspended");
                double_simdatastate.setText("Suspended");
                break;
        }

        //Dual sim methods
        final Class<?> tmClassSM;
        Class<?> tClassSM;
        try {
            tmClassSM = Class.forName("android.telephony.SubscriptionManager");
            // Static method to return list of active subids
            Method method[] = tmClassSM.getDeclaredMethods();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

                Method methodsub = SubscriptionManager.class.getMethod("getActiveSubscriptionIdList");
                int[] ssubIdList = (int[]) methodsub.invoke(SubscriptionManager.from(getContext()));

                SubscriptionManager sm = SubscriptionManager.from(getContext());

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    signalleveltextview.setText("Requires read Phone state permission");

                } else {

                    List<SubscriptionInfo> subscriptions = sm.getActiveSubscriptionInfoList();

                    if (subscriptions != null) {

                        for (SubscriptionInfo subscriptioninfo : subscriptions) {

                            if (subscriptioninfo.getSimSlotIndex() == 0) {

                                tClassSM = Class.forName(telephonymanager.getClass().getName());
                                Method tmmethod = TelephonyManager.class.getMethod("getNetworkOperatorName", int.class);
                                Method tmnettypemethod = TelephonyManager.class.getMethod("getNetworkType", int.class);
                                Method tmisroaming = TelephonyManager.class.getMethod("isNetworkRoaming", int.class);
                                try {
                                    Method tmcountrymethod = TelephonyManager.class.getMethod("getNetworkCountryIso", int.class);

                                    dataGetterSetterClass.set_NetCountry(networkcountry);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                                dataGetterSetterClass.set_NetCountry(networkcountry);
                                isroaming = (Boolean) tmisroaming.invoke(telephonymanager, subscriptioninfo.getSubscriptionId());
                                carriername = (String) tmmethod.invoke(telephonymanager, subscriptioninfo.getSubscriptionId());
                                simname = (String) subscriptioninfo.getCarrierName();
                                int ssnettype = (int) tmnettypemethod.invoke(telephonymanager, subscriptioninfo.getSubscriptionId());
                                networkoperatortypetextview.setText(carriername);
                                simoperatortextview.setText(simname);
                                single_sim_simoperator.setText(simname);
                                single_sim_netoperator.setText(carriername);
                                dataGetterSetterClass.set_NetOperator(carriername);
                                dataGetterSetterClass.set_SimOperator(simname);
                                dataGetterSetterClass.set_SimCountry(simcountrystr);

                                if (!simname.equals("No service")) {

                                    if (isroaming) {

                                    } else {

                                    }

                                    switch (ssnettype) {
                                        case 0:
                                            NetTypeStr = "Unknown";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);

                                            break;
                                        case 1:
                                            NetTypeStr = "GPRS";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 2:
                                            NetTypeStr = "EDGE";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 3:
                                            NetTypeStr = "UMTS";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 4:
                                            NetTypeStr = "CDMA";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 5:
                                            NetTypeStr = "EVDO_0";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 6:
                                            NetTypeStr = "EVDO_A";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 7:
                                            NetTypeStr = "1xRTT";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 8:
                                            NetTypeStr = "HSDPA";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 9:
                                            NetTypeStr = "HSUPA";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 10:
                                            NetTypeStr = "HSPA";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 11:
                                            NetTypeStr = "iDen";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 12:
                                            NetTypeStr = "EVDO_B";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 13:
                                            NetTypeStr = "LTE";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 14:
                                            NetTypeStr = "eHRPD";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;
                                        case 15:
                                            NetTypeStr = "HSPA+";
                                            dataGetterSetterClass.set_NetType(NetTypeStr);
                                            phonetypetextview.setText(NetTypeStr);
                                            single_nettype.setText(NetTypeStr);
                                            break;

                                    }

                                    FirstMultiSimListener listener = new FirstMultiSimListener(subscriptioninfo.getSubscriptionId());
                                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        dataGetterSetterClass.set_TowerAddress("Requires location access");
                                        dataGetterSetterClass.set_NetStrength("Requires location access");
                                        signalleveltextview.setText("Requires location access");

                                    } else {
                                        telephonymanager.listen(listener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_CALL_STATE);
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


    public void SeconSimMethod() {

        //Dual sim methods
        final Class<?> tmClassSM;
        Class<?> tClassSM;
        try {
            tmClassSM = Class.forName("android.telephony.SubscriptionManager");
            // Static method to return list of active subids
            Method method[] = tmClassSM.getDeclaredMethods();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

                Method methodsub = SubscriptionManager.class.getMethod("getActiveSubscriptionIdList");
                int[] ssubIdList = (int[]) methodsub.invoke(SubscriptionManager.from(getContext()));

                SubscriptionManager sm = SubscriptionManager.from(getContext());

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    secsimsignallevel.setText("Requires read Phone state permission");

                } else {

                    List<SubscriptionInfo> subscriptions = sm.getActiveSubscriptionInfoList();

                    if (subscriptions != null) {

                        for (SubscriptionInfo subscriptioninfo : subscriptions) {

                            if (subscriptioninfo.getSimSlotIndex() == 1) {

                                tClassSM = Class.forName(telephonymanager.getClass().getName());
                                Method tmmethod = TelephonyManager.class.getMethod("getNetworkOperatorName", int.class);
                                Method tmnettypemethod = TelephonyManager.class.getMethod("getNetworkType", int.class);
                                Method tmisroaming = TelephonyManager.class.getMethod("isNetworkRoaming", int.class);
                                try {
                                    Method tmcountrymethod = TelephonyManager.class.getMethod("getNetworkCountryIso", int.class);
                                    dataGetterSetterClass.set_SecondSimNetCountry(secondsimnetworkcountry);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                                secondsimcountry = (String) subscriptioninfo.getCountryIso();
                                secsimisroaming = (Boolean) tmisroaming.invoke(telephonymanager, subscriptioninfo.getSubscriptionId());
                                secondsimcarriername = (String) tmmethod.invoke(telephonymanager, subscriptioninfo.getSubscriptionId());
                                secondsimname = (String) subscriptioninfo.getCarrierName();
                                int ssnettype = (int) tmnettypemethod.invoke(telephonymanager, subscriptioninfo.getSubscriptionId());
                                secsimnetworkoperatortypetextview.setText(secondsimcarriername);
                                double_simnetoperator.setText(secondsimcarriername);
                                secsimsimoperatortextview.setText(secondsimname);
                                double_simnetoperator.setText(secondsimname);
                               dataGetterSetterClass.set_SecondSimNetOperator(secondsimcarriername);
                                dataGetterSetterClass.set_SecondSimSimOperator(secondsimname);
                                dataGetterSetterClass.set_SecondSimSimCountry(secondsimcountry);

                                if (!secondsimname.equals("No service")) {

                                    if (secsimisroaming) {
                                       // secsimroamingtextview.setText("Yes");
                                    } else {
                                       // secsimroamingtextview.setText("No");
                                    }

                                    switch (ssnettype) {
                                        case 0:
                                            secondsimnettype = "Unknown";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);

                                            dual_simnettype.setText(secondsimnettype);

                                            break;
                                        case 1:
                                            secondsimnettype = "GPRS";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 2:
                                            secondsimnettype = "EDGE";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 3:
                                            secondsimnettype = "UMTS";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 4:
                                            secondsimnettype = "CDMA";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 5:
                                            secondsimnettype = "EVDO_0";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 6:
                                            secondsimnettype = "EVDO_A";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 7:
                                            secondsimnettype = "1xRTT";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 8:
                                            secondsimnettype = "HSDPA";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 9:
                                            secondsimnettype = "HSUPA";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 10:
                                            secondsimnettype = "HSPA";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 11:
                                            secondsimnettype = "iDen";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 12:
                                            secondsimnettype = "EVDO_B";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 13:
                                            secondsimnettype = "LTE";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 14:
                                            secondsimnettype = "eHRPD";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            break;
                                        case 15:
                                            secondsimnettype = "HSPA+";
                                            dataGetterSetterClass.set_SecondSimNetType(secondsimnettype);
                                            secsimphonetypetextview.setText(secondsimnettype);
                                            dual_simnettype.setText(secondsimnettype);
                                            // networkoperatortypetextview.setText("Net.type : "+NetTypeStr);
                                            break;

                                    }

                                    MultiSimListener listener = new MultiSimListener(subscriptioninfo.getSubscriptionId());
                                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        dataGetterSetterClass.set_SecondSimNetStrength("Requires location access");
                                        secsimsignallevel.setText("Requires location access");

                                    } else {
                                        telephonymanager.listen(listener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_CALL_STATE);
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
                secondsimasutxtview.setText(String.valueOf(signalStrength.getGsmSignalStrength()));
            int asu2=signalStrength.getGsmSignalStrength();
            Log.e("asu signal 1",""+asu2);
            if (asu2>1 && asu2<20){

                asu_signal_second_txt.setImageResource(R.drawable.onestrengthsignal);
            }else if (asu2>21 && asu2<40){

                asu_signal_second_txt.setImageResource(R.drawable.twostrengthsignal);
            }else if (asu2>41 && asu2<60){

                asu_signal_second_txt.setImageResource(R.drawable.threestrengthsignal);

            }else if (asu2>61 && asu2<80){
                asu_signal_second_txt.setImageResource(R.drawable.fourstrengthsignal);

            }else if (asu2>81 && asu2<100){
                asu_signal_second_txt.setImageResource(R.drawable.fullstrengthsignal);
            }
                int rssi = -113 + 2 * signalStrength.getGsmSignalStrength();

                if (rssi <= -100) {
                    qualitypercent = 0;
                } else if (rssi >= -50) {
                    qualitypercent = 100;
                } else {
                    qualitypercent = 2 * (rssi + 100);
                }

                secondsimdbmtxtview.setText(String.valueOf(rssi));
                secondsimpointerspeedometer.speedTo(qualitypercent);
                sim_2_strength.setText(String.valueOf(qualitypercent));
            int speed=qualitypercent;

            if (speed>0 && speed<5){
                sim2signalimage.setImageResource(R.drawable.signalzero);
            } else if (speed>5 && speed<=15){
                sim2signalimage.setImageResource(R.drawable.signaltwo);
            }
            else if (speed>15 && speed<=25){
                sim2signalimage.setImageResource(R.drawable.signalthree);
            }
            else if (speed>25 && speed<=35){
                sim2signalimage.setImageResource(R.drawable.signalfour);
            }else if (speed>35 && speed<=45){
                sim2signalimage.setImageResource(R.drawable.signalfive);
            }
            else if (speed>45 && speed<=55){
                sim2signalimage.setImageResource(R.drawable.signalficentre);
            }
            else if (speed>55 && speed<=65){
                sim2signalimage.setImageResource(R.drawable.signalsixty);
            }else  if (speed>65&& speed<=75){
                sim2signalimage.setImageResource(R.drawable.signaleight);
            }else  if (speed>75&& speed<=85){
                sim2signalimage.setImageResource(R.drawable.signalnine);
            }else  if (speed>85&& speed<=94){
                sim2signalimage.setImageResource(R.drawable.signalninefive);
            }else  if (speed>94&& speed<=100){
                sim2signalimage.setImageResource(R.drawable.signalfull);
            }
            dataGetterSetterClass.set_SecondSimNetStrength(String.valueOf(rssi));
                secsimnetworkstrengthtextview.setText(qualitypercent + "%");

                dpuble_simnetworkstrength.setText(qualitypercent+ "%");
                int signallevel = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    signallevel = signalStrength.getLevel();
                    switch (signallevel) {
                        case 0:
                            secsimsignallevel.setText("Signal level Very poor");
                            secondsimsignallevelimgview.setImageResource(R.drawable.onestrengthsignal);
                            break;
                        case 1:
                            secsimsignallevel.setText("Signal level poor");
                            secondsimsignallevelimgview.setImageResource(R.drawable.twostrengthsignal);
                            break;
                        case 2:
                            secsimsignallevel.setText("Signal level moderate");
                            secondsimsignallevelimgview.setImageResource(R.drawable.threestrengthsignal);
                            break;
                        case 3:
                            secsimsignallevel.setText("Signal level good");
                            secondsimsignallevelimgview.setImageResource(R.drawable.fourstrengthsignal);
                            break;
                        case 4:
                            secsimsignallevel.setText("Signal level Excellent");
                            secondsimsignallevelimgview.setImageResource(R.drawable.fullstrengthsignal);
                            break;

                    }
                }
        }

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
            // Handle the event here, subId indicates the subscription id if > 0
            firstsimasutxtview.setText(String.valueOf(signalStrength.getGsmSignalStrength()));

            int asu1=signalStrength.getGsmSignalStrength();

            Log.e("asu signal 1",""+asu1);


            if (asu1>1 && asu1<20){

                asu_signal_first_txt.setImageResource(R.drawable.onestrengthsignal);
            }else if (asu1>21 && asu1<40){

                asu_signal_first_txt.setImageResource(R.drawable.twostrengthsignal);
            }else if (asu1>41 && asu1<60){

                asu_signal_first_txt.setImageResource(R.drawable.threestrengthsignal);

            }else if (asu1>61 && asu1<80){
                asu_signal_first_txt.setImageResource(R.drawable.fourstrengthsignal);

            }else if (asu1>81 && asu1<100){
                asu_signal_first_txt.setImageResource(R.drawable.fullstrengthsignal);
            }
            int rssi = -113 + 2 * signalStrength.getGsmSignalStrength();

            if (rssi <= -100) {
                qualitypercent = 0;
            } else if (rssi >= -50) {
                qualitypercent = 100;
            } else {
                qualitypercent = 2 * (rssi + 100);
            }

            firstsimdbmtxtview.setText(String.valueOf(rssi));
            pointerSpeedometer.speedTo(qualitypercent);


            sim_1_strength.setText(String.valueOf(qualitypercent));

            int speed=qualitypercent;

            if (speed>0 && speed<5){
                sim1signalimage.setImageResource(R.drawable.signalzero);
            } else if (speed>5 && speed<=15){
                sim1signalimage.setImageResource(R.drawable.signaltwo);
            }
            else if (speed>15 && speed<=25){
                sim1signalimage.setImageResource(R.drawable.signalthree);
            }
            else if (speed>25 && speed<=35){
                sim1signalimage.setImageResource(R.drawable.signalfour);
            }else if (speed>35 && speed<=45){
                sim1signalimage.setImageResource(R.drawable.signalfive);
            }
            else if (speed>45 && speed<=55){
                sim1signalimage.setImageResource(R.drawable.signalficentre);
            }
            else if (speed>55 && speed<=65){
                sim1signalimage.setImageResource(R.drawable.signalsixty);
            }else  if (speed>65&& speed<=75){
                sim1signalimage.setImageResource(R.drawable.signaleight);
            }else  if (speed>75&& speed<=85){
                sim1signalimage.setImageResource(R.drawable.signalnine);
            }else  if (speed>85&& speed<=94){
                sim1signalimage.setImageResource(R.drawable.signalninefive);
            }else  if (speed>94&& speed<=100){
                sim1signalimage.setImageResource(R.drawable.signalfull);
            }
           dataGetterSetterClass.set_NetStrength(String.valueOf(rssi));
            networkstrengthtextview.setText(qualitypercent + "%");
            single_sim_netstrength.setText(qualitypercent + "%");
            int signallevel = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                signallevel = signalStrength.getLevel();
                switch (signallevel) {
                    case 0:
                        signalleveltextview.setText("Signal level Very poor");
                        firstsimsignallevelimgview.setImageResource(R.drawable.onestrengthsignal);
                        break;
                    case 1:
                        signalleveltextview.setText("Signal level poor");
                        firstsimsignallevelimgview.setImageResource(R.drawable.twostrengthsignal);
                        break;
                    case 2:
                        signalleveltextview.setText("Signal level moderate");
                        firstsimsignallevelimgview.setImageResource(R.drawable.threestrengthsignal);
                        break;
                    case 3:
                        signalleveltextview.setText("Signal level good");
                        firstsimsignallevelimgview.setImageResource(R.drawable.fourstrengthsignal);
                        break;
                    case 4:
                        signalleveltextview.setText("Signal level Excellent");
                        firstsimsignallevelimgview.setImageResource(R.drawable.fullstrengthsignal);
                        break;

                }
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void phoneStateStartUp() {
    }


}
