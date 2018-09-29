package com.quadrobay.qbayApps.networkproject.TabPages;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.quadrobay.qbayApps.networkproject.R;
import com.quadrobay.qbayApps.networkproject.Services.ConnectivityReceiver;
import com.quadrobay.qbayApps.networkproject.Services.NetworkSniffTask;
import com.quadrobay.qbayApps.networkproject.SingletonClass.AppController;
import com.race604.drawable.wave.WaveDrawable;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.model.SpeedTestMode;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by benchmark on 27/10/17.
 */

public class FifthTabPage extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener, NetworkSniffTask.deviceListCallBack {

    private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;
    private TextView mTextView;
    int secured=0;

    Context context;
    ImageView advance_mode,speedrangelevel;

    TextView mode_Txt_view;

    ImageView needle;
    boolean click=true;

    Button wifisecuritycheck;
    TextView statusTextView, downLoadTextView, uploadTextView, pingTextView, teststatustxtv, totalSpeedTxtView;
    Button testButton;
    Boolean internetConnectivityStatus;

    WaveDrawable waveDrawable;
    ImageView waveimage;
    LinearLayout lay;
    String configSecurity;
    String response = null;
    String sdPath;
    String ssid;

    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_EAP = 3;

    TextView connectedDevicesTxtView;
    AVLoadingIndicatorView devicesLoadingBar;

    boolean mode=true;

    /**
     * default scale for BigDecimal.
     */
    private static final int DEFAULT_SCALE = 4;

    LinearLayout linearlayout22;


    /**
     * default rounding mode for BigDecimal.
     */
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    /**
     * conversion const for per second value.
     */
    private static final BigDecimal VALUE_PER_SECONDS = new BigDecimal(1000);

    /**
     * conversion const for M per second value.
     */
    private static final BigDecimal MEGA_VALUE_PER_SECONDS = new BigDecimal(1000000);

    /**
     * log report separator.
     */
    public static final String LOG_REPORT_SEPARATOR = "--------------------------------------------------------";

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    IntentFilter intentFilter;
    MyReceiver receiver;

    int colorOff = 0xFF666666;


    public FifthTabPage() {

    }
    WifiManager wifiManager;



    TextView wifi_sec_text,dns_sec_text,arp_sec_text,tls_sec_text;

    WifiInfo wifiInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        lay = (LinearLayout) inflater.inflate(R.layout.fiffth_tab_page, container, false);

        context = getActivity();

        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mListener = new ConnectionChangedListener();
        mConnectionClassManager.register(mListener);


        teststatustxtv = (TextView) lay.findViewById(R.id.test_status_txt_view);
        statusTextView = (TextView) lay.findViewById(R.id.Internet_Name);
        pingTextView = (TextView) lay.findViewById(R.id.PingValueTextView);
        downLoadTextView = (TextView) lay.findViewById(R.id.DownloadValueTextView);
        uploadTextView = (TextView) lay.findViewById(R.id.UploadValueTextView);
        testButton = (Button) lay.findViewById(R.id.TestButton);

        waveimage = (ImageView) lay.findViewById(R.id.wave_img);

        connectedDevicesTxtView = (TextView) lay.findViewById(R.id.connected_devices_textview);
        devicesLoadingBar = lay.findViewById(R.id.devices_loading_progressbar);

        advance_mode=lay.findViewById(R.id.advancemode);


        linearlayout22=lay.findViewById(R.id.linear22);
        wifisecuritycheck=lay.findViewById(R.id.Securitycheckbutton);

        speedrangelevel=lay.findViewById(R.id.speed_range);


        mode_Txt_view=lay.findViewById(R.id.mode_text_view);

        ImageView myImageView = lay.findViewById(R.id.needle);

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(00.0f, -35.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.50f,
                RotateAnimation.RELATIVE_TO_SELF, 1.00f);


        animRotate.setRepeatCount(0);
        animRotate.setFillAfter(true);
        animRotate.setFillEnabled(true);
        animRotate.setInterpolator(new OvershootInterpolator());
        animSet.addAnimation(animRotate);
        myImageView.startAnimation(animSet);




        advance_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devicesLoadingBar.show();
                String devicecount=AppController.getInstance().getConnectedDevice();

                if(devicecount!=null){
                    devicesLoadingBar.hide();
                    connectedDevicesTxtView.setText("Connected Devices : "+devicecount);
                }

                new NetworkSniffTask(context, new NetworkSniffTask.deviceListCallBack() {
                    @Override
                    public void onGetDeviceList(int deviceCount) {
                        devicesLoadingBar.hide();
                        connectedDevicesTxtView.setText("Connected Devices : "+String.valueOf(deviceCount));
                    }

                    @Override
                    public void onStartGetDeviceList() {
                        devicesLoadingBar.show();

                    }
                }).execute();
                if (mode){

                    advance_mode.setImageDrawable(getActivity().getDrawable(R.drawable.advancedmode));

                    mode_Txt_view.setText("Advance mode");

                    linearlayout22.setVisibility(View.VISIBLE);

                    mode=false;
                }else {
                    advance_mode.setImageDrawable(getActivity().getDrawable(R.drawable.normalmode));

                    mode_Txt_view.setText("Normal mode");
                    linearlayout22.setVisibility(View.GONE);
                    mode=true;
                }
                }
        });

        devicesLoadingBar.show();
        String devicecount=AppController.getInstance().getConnectedDevice();

        if(devicecount!=null){
            devicesLoadingBar.hide();
            connectedDevicesTxtView.setText("Connected Devices : "+devicecount);
            Log.e("",devicecount);
        }


        wifisecuritycheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSecurityDetials();
                devicesLoadingBar.show();
                String devicecount=AppController.getInstance().getConnectedDevice();

                if(devicecount!=null){
                    devicesLoadingBar.hide();
                    connectedDevicesTxtView.setText("Connected Devices : "+devicecount);
                }
                new NetworkSniffTask(context, new NetworkSniffTask.deviceListCallBack() {
                    @Override
                    public void onGetDeviceList(int deviceCount) {
                        devicesLoadingBar.hide();
                        connectedDevicesTxtView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        connectedDevicesTxtView.setText("Connected Devices : "+String.valueOf(deviceCount));
                    }

                    @Override
                    public void onStartGetDeviceList() {
                        devicesLoadingBar.show();
                    }
                });

            }
        });

        wifi_sec_text=lay.findViewById(R.id.wifi_sec_txt);
        dns_sec_text=lay.findViewById(R.id.dns_sec_txt);
        arp_sec_text=lay.findViewById(R.id.arp_sec_txt);
        tls_sec_text=lay.findViewById(R.id.tls_sec_txt);
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new MyReceiver();

        internetConnectivityStatus = checkConnection();
        if (internetConnectivityStatus){
           statusTextView.setText(GetConnectedNetwork());
        }else {
            statusTextView.setText("No Internet Connection");
            connectedDevicesTxtView.setText("Not Connceted");
        }

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                needle=lay.findViewById(R.id.needle);
                ImageView myImageView = lay.findViewById(R.id.needle);
                AnimationSet animSet = new AnimationSet(true);
                animSet.setInterpolator(new DecelerateInterpolator());
                animSet.setFillAfter(true);
                animSet.setFillEnabled(true);

                final RotateAnimation animRotate = new RotateAnimation(00.0f, -20.0f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.50f,
                        RotateAnimation.RELATIVE_TO_SELF, 1.00f);

                animRotate.setRepeatCount(0);
                animRotate.setFillAfter(true);
                animRotate.setFillEnabled(true);
                animRotate.setInterpolator(new OvershootInterpolator());
                 animRotate.setDuration(1500);
                animSet.addAnimation(animRotate);
                myImageView.startAnimation(animSet);

                if (click){
                    testButton.setBackgroundColor(getActivity().getResources().getColor(R.color.newblue));
                    testButton.setClickable(false);
                    testButton.setText("Testing");
                }
                internetConnectivityStatus = checkConnection();
                if (internetConnectivityStatus) {
                    uploadTextView.setText("Uploading...");
                    downLoadTextView.setText("Downloading.....");
                    pingTextView.setText("Pinging..");
                    teststatustxtv.setText("Testing Ping.......");
                    speedrangelevel.setImageDrawable(getActivity().getDrawable(R.drawable.secondspeed));
                    waveimage.setImageDrawable(getResources().getDrawable(R.drawable.testping));
                    ping("");

                            waveDrawable = new WaveDrawable(getActivity(), R.drawable.testdownload);
                            waveimage.setImageDrawable(waveDrawable);
                            waveDrawable.setWaveAmplitude(0);
                            teststatustxtv.setText("Testing Download speed.......");
                              speedrangelevel.setImageDrawable(getActivity().getDrawable(R.drawable.fullspeed));
                            new SpeedTestTask().execute();

                    uploadTextView.setText("Uploading .....");
                }
            }
        });


        return lay;
    }

    private void getSecurityDetials() {
         wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
         wifiInfo = wifiManager.getConnectionInfo();
         if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
             ssid = wifiInfo.getSSID();
            Log.e("",ssid);
         }
            List<WifiConfiguration> wifiConfiguration=wifiManager.getConfiguredNetworks();
           if (ssid==null){
           connectedDevicesTxtView.setVisibility(View.VISIBLE);
            Log.e("","");

           }else {

        configSecurity=getWifiSecureType();
        if (configSecurity!=null){
        if (configSecurity.contains("OPEN")) {
        wifi_sec_text.setText("Not secured");
        dns_sec_text.setText("Not secured");
        tls_sec_text.setText("Not secured");
        arp_sec_text.setText("Not secured");

       } else if (configSecurity.contains("loc")) {

        wifi_sec_text.setText("Please enable location");
        dns_sec_text.setText("Please enable location");
        tls_sec_text.setText("Please enable location");
        arp_sec_text.setText("Please enable location");
    }
    else {
         wifi_sec_text.setText("Secured");
         dns_sec_text.setText("Secured");
         tls_sec_text.setText("Secured");
         arp_sec_text.setText("Secured");
         }
        }
        }
    }


    String getWifiSecureType(){

    WifiManager wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    List<ScanResult> networkList = wifi.getScanResults();

    WifiInfo wi = wifi.getConnectionInfo();
    String currentSSID = wi.getBSSID();


    if (networkList != null) {
        for (ScanResult network : networkList) {

            if (network.BSSID.contains(currentSSID)) {
                //get capabilities of current connection
                String Capabilities = network.capabilities;
                Log.d("sdsd", network.SSID + " capabilities : " + Capabilities);

                if (Capabilities.contains("WPA2")) {

                    return "WPA2";
                } else if (Capabilities.contains("WPA")) {
                    return "WPA";
                } else if (Capabilities.contains("WEP")) {

                    return "WEP";
                }
            }

        }
        }

    else {

        return "loc";
    }

    return  "loc";
    }


    private Boolean checkConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }
    public String GetConnectedNetwork(){
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);

        String status = null;
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "WIFI Internet Connection";
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile Data Internet Connection";
            }
        } else {
            status = "No Internet Connection";
        }
        return status;
    }

    @Override
    public void onGetDeviceList(int deviceCount) {
        devicesLoadingBar.hide();
        connectedDevicesTxtView.setText("Connected Devices : "+String.valueOf(deviceCount));
    }

    @Override
    public void onStartGetDeviceList() {

        devicesLoadingBar.show();
    }

    public class SpeedTestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            SpeedTestSocket speedTestSocket = new SpeedTestSocket();

            // add a listener to wait for speedtest completion and progress
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    // called when download/upload is finished

                    long tm =  report.getReportTime();
                    Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());

                    DecimalFormat df = new DecimalFormat("#.##");
                    final String dx=df.format(report.getTransferRateBit().divide(MEGA_VALUE_PER_SECONDS));
                    double t = Double.parseDouble(dx);
                    final double tt = t*8;

                    System.out.println("packetSize     : " + report.getTotalPacketSize() + " octet(s)");
                    System.out.println("transfer rate  : " + report.getTransferRateBit() + " bit/second   | " +
                            report.getTransferRateBit().divide(VALUE_PER_SECONDS, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
                            + " Kbit/second  | " + dx + " Mbit/second");
                    System.out.println("transfer rate  : " + report.getTransferRateOctet() + " octet/second | " +
                            report.getTransferRateOctet().divide(VALUE_PER_SECONDS, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
                            + " Koctet/second | " + report.getTransferRateOctet().divide(MEGA_VALUE_PER_SECONDS, DEFAULT_SCALE,
                            DEFAULT_ROUNDING_MODE) + " " +
                            "Moctet/second");

                    long takenTime = report.getReportTime() - report.getStartTime();
                    long s = takenTime / 1000;
                    double speed = report.getTotalPacketSize() / s;


                    double mps = (double) takenTime / 1000;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            downLoadTextView.setText(String.valueOf(tt) + " Mb/second");

                                      waveDrawable = new WaveDrawable(getActivity(), R.drawable.testupload);
                                    waveimage.setImageDrawable(waveDrawable);
                                    waveDrawable.setWaveAmplitude(0);
                            speedrangelevel.setImageDrawable(getActivity().getDrawable(R.drawable.fourspeed));
                            teststatustxtv.setText("Testing Upload speed.......");
                                    new SpeedUploadTestTask().execute();

                        }
                    });
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                    // called when a download/upload error occur
                    System.out.println("[PROGRESS] progress : " + errorMessage + "%");
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    // called to notify download/upload progress
                    final float finpercent = percent;

                    Log.v("speedtest", "[PROGRESS] progress : " + percent + "%");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            double cq1 = ConnectionClassManager.getInstance().getDownloadKBitsPerSecond();
                            teststatustxtv.setText("Testing Download speed "+ String.valueOf(finpercent) +"% ...");
                            downLoadTextView.setText(String.valueOf(finpercent));


                            int value = (int) finpercent;
                            int maxValue = 4000;
                            int angleDifference = 50;
                            int endingAngle = (int) (((float) value / maxValue) * (angleDifference));
                            ImageView myImageView = lay.findViewById(R.id.needle);
                            AnimationSet animSet = new AnimationSet(true);
                            animSet.setInterpolator(new DecelerateInterpolator());
                            animSet.setFillAfter(true);
                            animSet.setFillEnabled(true);

                            final RotateAnimation animRotate = new RotateAnimation(00.0f, -30+endingAngle,
                                    RotateAnimation.RELATIVE_TO_SELF, 0.50f,
                                    RotateAnimation.RELATIVE_TO_SELF, 1.00f);
                            animRotate.setRepeatCount(0);
                            animRotate.setFillAfter(true);
                            animRotate.setFillEnabled(true);
                            animRotate.setInterpolator(new OvershootInterpolator());
                            animRotate.setDuration(2000);
                            animSet.addAnimation(animRotate);
                            myImageView.startAnimation(animSet);
                            speedrangelevel.setImageDrawable(getActivity().getDrawable(R.drawable.fullspeed));
                            waveDrawable.setLevel(Math.round(finpercent)*100);
                            waveDrawable.setWaveLength(Math.round(finpercent)*100);
                        }
                    });
                    Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }
            });

         //   speedTestSocket.startDownload("http://findyourcarapp.com/tesla_model_s_tesla_model_s_gray_79777_3840x2160.jpg");
            speedTestSocket.startDownload("http://findyourcarapp.com/test1Mb.db");
         //   speedTestSocket.startDownload("http://findyourcarapp.com/1mb.PDF");

            return null;
        }
    }

    public class SpeedUploadTestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            SpeedTestSocket speedTestSocket = new SpeedTestSocket();

            // add a listener to wait for speedtest completion and progress
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    // called when download/upload is finished
                    Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());

                    DecimalFormat df = new DecimalFormat("#.##");
                    final String dx=df.format(report.getTransferRateBit().divide(MEGA_VALUE_PER_SECONDS));
                    double t = Double.parseDouble(dx);
                    final double tt = t*8;

                    System.out.println("packetSize     : " + report.getTotalPacketSize() + " octet(s)");
                    System.out.println("transfer rate  : " + report.getTransferRateBit() + " bit/second   | " +
                            report.getTransferRateBit().divide(VALUE_PER_SECONDS, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
                            + " Kbit/second  | " + dx + " Mbit/second");
                    System.out.println("transfer rate  : " + report.getTransferRateOctet() + " octet/second | " +
                            report.getTransferRateOctet().divide(VALUE_PER_SECONDS, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
                            + " Koctet/second | " + report.getTransferRateOctet().divide(MEGA_VALUE_PER_SECONDS, DEFAULT_SCALE,
                            DEFAULT_ROUNDING_MODE) + " " +
                            "Moctet/second");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ConnectionQuality cq = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
                            double cq1 = ConnectionClassManager.getInstance().getDownloadKBitsPerSecond();
                            uploadTextView.setText(tt + " Mb/second");
                            ImageView myImageView = lay.findViewById(R.id.needle);
                            AnimationSet animSet = new AnimationSet(true);
                            animSet.setInterpolator(new DecelerateInterpolator());
                            animSet.setFillAfter(true);
                            animSet.setFillEnabled(true);

                            final RotateAnimation animRotate = new RotateAnimation(-20.0f, -35.0f,
                                    RotateAnimation.RELATIVE_TO_SELF, 0.50f,
                                    RotateAnimation.RELATIVE_TO_SELF, 1.00f);


                            animRotate.setRepeatCount(0);
                            animRotate.setFillAfter(true);
                            animRotate.setFillEnabled(true);
                            animRotate.setInterpolator(new OvershootInterpolator());
                            animRotate.setDuration(1000);
                           animSet.addAnimation(animRotate);

                          myImageView.startAnimation(animSet);

                            teststatustxtv.setText("Test Complete");
                            waveimage.setImageDrawable(getResources().getDrawable(R.drawable.testcomplete));
                            speedrangelevel.setImageDrawable(getActivity().getDrawable(R.drawable.nospeed));
                            testButton.setText("Test Again");
                            testButton.setClickable(true);
                            testButton.setBackgroundColor(getActivity().getResources().getColor(R.color.buttonbackground));

                        }
                    });
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                    // called when a download/upload error occur
                    System.out.println("[PROGRESS] progress : " + errorMessage + "%");
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    final float finpercent = percent;
                    // called to notify download/upload progress

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            double cq1 = ConnectionClassManager.getInstance().getDownloadKBitsPerSecond();
                            teststatustxtv.setText("Testing Upload speed "+ String.valueOf(finpercent) +"% ...");
                            uploadTextView.setText(String.valueOf(finpercent));
                            speedrangelevel.setImageDrawable(getActivity().getDrawable(R.drawable.sixspeed));
                            waveDrawable.setLevel(Math.round(finpercent)*100);
                            waveDrawable.setWaveLength(Math.round(finpercent)*100);
                            testButton.setEnabled(true);


                        }
                    });
                    Log.v("speedtest", "[PROGRESS] progress : " + percent + "%");
                    Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }
            });

            // speedTestSocket.startDownload("http://2.testdebit.info/fichiers/1Mo.dat");
           // speedTestSocket.startDownload("http://findyourcarapp.com/tesla_model_s_tesla_model_s_gray_79777_3840x2160.jpg");
            speedTestSocket.startUpload("http://findyourcarapp.com/Upload/upload.php", 100000);

            return null;
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        internetConnectivityStatus = isConnected;
        if (internetConnectivityStatus){
            statusTextView.setText(GetConnectedNetwork());

        }else {
            statusTextView.setText("No Internet Connection");
        }
    }

    public void ping(String url) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    String str = "null";


                    int timeout = 2000;
                    long currentTime = System.currentTimeMillis();
                    InetAddress[] addresses = InetAddress.getAllByName("www.java2s.com");
                    currentTime = System.currentTimeMillis() - currentTime;
                    str = currentTime + " ms";
                    for (InetAddress address : addresses) {
                        if (address.isReachable(timeout)){


                            pingTextView.setText(str);
                        System.out.printf("%s is reachable%n", address);
                    }else
                            System.out.printf("%s could not be contacted%n", address);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    public class ImageUploadTask extends AsyncTask<String, Void, String> {

        long startTime = 0;
        long endTime = 0;
        private long takenTime = 0;

        @Override
        protected String doInBackground(String... strings) {
            long lengthbmp;
            startTime = System.currentTimeMillis();
            int serverResponseCode = 0;
            HttpURLConnection connection;
            DataOutputStream dataOutputStream;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";


            int bytesRead,bytesAvailable,bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File selectedFile = new File(sdPath);



            String[] parts = sdPath.split("/");
            final String fileName = parts[parts.length-1];

            if (!selectedFile.isFile()){

                return response;
            }else{
                try{
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    URL url = new URL(strings[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);//Allow Inputs
                    connection.setDoOutput(true);//Allow Outputs
                    connection.setUseCaches(false);//Don't use a cached Copy
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("uploaded_file",sdPath);

                    //creating new dataoutputstream
                    dataOutputStream = new DataOutputStream(connection.getOutputStream());

                    //writing bytes to data outputstream
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + sdPath + "\"" + lineEnd);

                    dataOutputStream.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();
                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];
                    lengthbmp = bufferSize;
                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);

                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0){
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer,0,bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable,maxBufferSize);
                        bytesRead = fileInputStream.read(buffer,0,bufferSize);
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    serverResponseCode = connection.getResponseCode();
                    String serverResponseMessage = connection.getResponseMessage();

                   // Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    //response code of 200 indicates the server status OK
                    if(serverResponseCode == 200){

                        response = "Success";
                        endTime = System.currentTimeMillis();
                        return lengthbmp + "";
                    }

                    //closing the input and output streams
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();



                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (MalformedURLException e) {
                    e.printStackTrace();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        protected void onPostExecute(String result) {

            if (result != null) {
                long dataSize = Integer.parseInt(result) / 1024;
                takenTime = endTime - startTime;
                double s = (double) takenTime / 1000;
                double speed = dataSize / s;
                Log.d("onPostUploadExecute: ", "" + new DecimalFormat("##.##").format(speed) + "kb/second");

                long kpdataSize = Integer.parseInt(result) / 1024;
                long mpdataSize = kpdataSize / 1024;
                takenTime = endTime - startTime;
                double mps = (double) takenTime / 1000;
                double mpspeed = mpdataSize / mps;
                Log.d("onPostUploadExecuteMB: ", "" + new DecimalFormat("##.##").format(mpspeed) + "mb/second");
            }
        }
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String actionOfIntent = intent.getAction();
            boolean isConnected = checkConnection();
            if(actionOfIntent.equals(CONNECTIVITY_ACTION)){
                if(isConnected){
                    statusTextView.setText(GetConnectedNetwork());

                }else{
                    statusTextView.setText("No Internet Connection");
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mConnectionClassManager.register(mListener);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        }else {

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        }
    }


    /**
     * Listener to update the UI upon connectionclass change.
     */
    private class ConnectionChangedListener
            implements ConnectionClassManager.ConnectionClassStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    totalSpeedTxtView.setText(mConnectionClass.toString());
                }
            });
        }
    }
}
