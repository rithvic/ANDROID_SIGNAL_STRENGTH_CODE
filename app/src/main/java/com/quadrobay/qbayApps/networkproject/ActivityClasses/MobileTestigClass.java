package com.quadrobay.qbayApps.networkproject.ActivityClasses;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.AudioManager;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quadrobay.qbayApps.networkproject.R;
import com.shuhart.stepview.StepView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import ch.halcyon.squareprogressbar.SquareProgressBar;

/**
 * Created by benchmark on 06/02/18.
 */

public class MobileTestigClass extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private Toolbar mobiletesttoolbar;
    Context context;
    View mainView;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    int progresslevel;
    public static final int VOLUME_DOWN_BUTTON_TEST = 0;
    public static final int VOLUME_UP_BUTTON_TEST = 1;
    public static final int LOCK_BUTTON_TEST = 2;
    public static final int BACK_BUTTON_TEST = 3;
    public static final int HOME_BUTTON_TEST = 4;
    public static final int RECENTS_BUTTON_TEST = 5;
    public static final int SCREEN_TEST = 6;
    public static final int BRIGHTNESS_TEST = 7;
    public static final int VIBRATION_TEST = 8;
    public static final int BACK_SPEAKER_TEST = 9;
    public static final int FRONT_SPEAKER_TEST = 10;
    public static final int MIC_TEST = 11;
    public static final int BACK_CAMERA_TEST = 12;
    public static final int FRONT_CAMERA_TEST = 13;
    public static final int BACK_FLASH_TEST = 14;
    public static final int FRONT_FLASH_TEST = 15;
    public static final int ACCELEROMETER_TEST = 16;
    public static final int COMPASS_TEST = 17;
    public static final int PROXIMITY_TEST = 18;
    public static final int GYROMETER_TEST = 19;
    public static final int SINGLE_TOUCH_TEST = 20;
    public static final int MULTI_TOUCH_TEST = 21;
    public static final int FINISH = 22;

    String rgblevel;
    int rgbvalue;
    private int currentbrightness;
    float upbrightness;
    private ContentResolver cResolver;
    private Window window;

    Boolean playing;
    Ringtone r;
    MediaPlayer mp;

   // RoundCornerProgressBar test_progress_view;
    StepView stepView;
    SquareProgressBar squareProgressBar;
    Button nextbutton, backbutton, resetbutton, redcolorbutton, greencolorbutton, bluecolorbutton, commonbutton;
    ImageButton brightnessincrease, brightnessdecrease;
    SeekBar brightnessSeekBar;
    ImageView testimageview;
    TextView status_text_view, guide_text;
    LinearLayout colorsbuttonlayout, brightnesslayout, mainbuttonlayout;

    private int hardkeysPosition;
    MyBroadCastReciever myBroadCastReciever;
    IntentFilter screenStateFilter;

    PopupWindow colorpopupWindow;
    View mcolorCustomView;
    DrawerLayout mDrayerLayout;

    //Camera Variables
    private static final String TAG = "AndroidCameraApi";
    private Button takePictureButton;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    CameraDevice.StateCallback stateCallback;
    Boolean isCameraOpened;
    int cameraType;
    private boolean hasFlash;
    private boolean isFlashOn;
    Camera camera;
    Camera.Parameters parameters;

    // Sensor values
    private int sensorProgress;
    private Boolean isSensorOpened;
    private SimulationView mSimulationView;
    private SensorManager mSensorManager;

    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    private Display mDisplay;
    private PowerManager.WakeLock mWakeLock;
    private FrameLayout sensorlayout, gyroscopelayout;

    private Sensor compass;
    private ImageView gyroscopeimage;
    private TextView compassAngle;
    private float currentDegree = 0f;
    private Compass gyroCompass;

    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;
    private Boolean isProximityRegistered;

    private Sensor mGyroscope;
    private Boolean isGyroscopeReegistered;

    //Touch declarations
    MySurfaceView mySurfaceView;
    private FrameLayout singleTouchLayout;
    PopupWindow touchPopUpWindow;
    private Boolean isTouchScreenOpened;
    private Boolean isSingleTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_testing_layout);

        context = this;
        mainView = (View) findViewById(R.id.testing_main_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mcolorCustomView = layoutInflater.inflate(R.layout.color_testing_layout,null);
        mcolorCustomView.setClipToOutline(true);

        myBroadCastReciever = new MyBroadCastReciever();

        hardkeysPosition = 0;

        //Get the content resolver
        cResolver = getContentResolver();
        //Get the current window
        window = getWindow();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        colorpopupWindow = new PopupWindow(mcolorCustomView,width,height);
        colorpopupWindow.setAnimationStyle(android.R.style.Animation_Toast);
        colorpopupWindow.setElevation(5);

        stepView = (StepView) findViewById(R.id.step_view);
        stepView.getState()
                .animationType(StepView.ANIMATION_ALL)
                // You should specify only stepsNumber or steps array of strings.
                // In case you specify both steps array is chosen.
                .steps(new ArrayList<String>() {{
                    add("Hard Keys");
                    add("Screen");
                    add("Calling");
                    add("Camera");
                    add("Sensors");
                    add("Touch");
                }})
                // You should specify only steps number or steps array of strings.
                // In case you specify both steps array is chosen.
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .typeface(ResourcesCompat.getFont(context, R.font.roboto_italic))
                // other state methods are equal to the corresponding xml attributes
                .commit();

        stepView.setVisibility(View.GONE);

        sensorlayout = (FrameLayout) findViewById(R.id.sensor_layout);
        gyroscopelayout = (FrameLayout) findViewById(R.id.gyroscope_layout);
        singleTouchLayout = (FrameLayout) findViewById(R.id.touch_layout);
        gyroscopeimage = (ImageView) findViewById(R.id.gyroscope_img_view);
        mainbuttonlayout = (LinearLayout) findViewById(R.id.main_button_layout);
        colorsbuttonlayout = (LinearLayout) findViewById(R.id.color_btn_layout);
        brightnesslayout = (LinearLayout) findViewById(R.id.brightness_layout);
        squareProgressBar = (SquareProgressBar) findViewById(R.id.sprogressbar);
        status_text_view = (TextView) findViewById(R.id.test_status);
        guide_text = (TextView) findViewById(R.id.text_guide);
       // testimageview = (ImageView) findViewById(R.id.test_indicator_imageview);
        nextbutton = (Button) findViewById(R.id.next_button);
        backbutton = (Button) findViewById(R.id.back_button);
        resetbutton = (Button) findViewById(R.id.reset_button);
        redcolorbutton = (Button) findViewById(R.id.red_button);
        greencolorbutton = (Button) findViewById(R.id.green_button);
        bluecolorbutton = (Button) findViewById(R.id.blue_button);
        commonbutton = (Button) findViewById(R.id.common_btn);
        brightnessincrease = (ImageButton) findViewById(R.id.brightness_increase_btn);
        brightnessdecrease = (ImageButton) findViewById(R.id.brightness_decrease_btn);
        brightnessSeekBar = (SeekBar) findViewById(R.id.brightness_seek_bar);
        textureView = (TextureView) findViewById(R.id.camera_View);

        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myBroadCastReciever, screenStateFilter);

        //SoundProperties
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        Uri notifications = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r1 = RingtoneManager.getRingtone(getApplicationContext(), notifications);
        mp = new MediaPlayer();
        try {
            mp.setDataSource(getApplicationContext(), notifications);
            mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        } catch (IOException e) {
            e.printStackTrace();
        }


        TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //open your camera here
                //openCamera();
            }
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // Transform you image captured size according to the surface width and height
            }
            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };
        textureView.setSurfaceTextureListener(textureListener);

        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {
                //This is called when the camera is open
                Log.e(TAG, "onOpened");
                cameraDevice = camera;
                createCameraPreview();
            }
            @Override
            public void onDisconnected(CameraDevice camera) {
                cameraDevice.close();
            }
            @Override
            public void onError(CameraDevice camera, int error) {
                cameraDevice.close();
                cameraDevice = null;
            }
        };

        brightnessSeekBar.setMax(255);
        brightnessSeekBar.setKeyProgressIncrement(1);

        squareProgressBar.setImage(R.drawable.mobiletestlogo);
        squareProgressBar.setProgress(0);
        squareProgressBar.setHoloColor(R.color.whitecolor);
        squareProgressBar.setWidth(2);

        progresslevel = 0;
        rgblevel = String.valueOf(0);

        // Get an instance of the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get an instance of the PowerManager
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);

        // Get an instance of the WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        // Create a bright wake lock
        mSimulationView = new SimulationView(context);
        mSimulationView.setBackgroundColor(getResources().getColor(R.color.whitecolor));
        sensorlayout.addView(mSimulationView);

        gyroCompass = new Compass(this);
        gyroCompass.arrowView = (ImageView) findViewById(R.id.gyroscope_img_view);

        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        isProximityRegistered = false;

        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        isGyroscopeReegistered = false;

        //Touch declarations
        mySurfaceView = new MySurfaceView(this);
        touchPopUpWindow = new PopupWindow(mySurfaceView,width,height);
        touchPopUpWindow.setAnimationStyle(android.R.style.Animation_Toast);
        touchPopUpWindow.setElevation(5);
        isTouchScreenOpened = false;
        singleTouchLayout.addView(mySurfaceView);

        stepView.setVisibility(View.INVISIBLE);

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (progresslevel){

                    case VOLUME_DOWN_BUTTON_TEST:

                        squareProgressBar.setProgress(15);
                        hardkeysPosition = 0;
                        stepView.go(0,true);
                        stepView.setVisibility(View.VISIBLE);
                        status_text_view.setVisibility(View.VISIBLE);
                        nextbutton.setText("Next");

                        squareProgressBar.setImage(R.drawable.volumedowntest);
                        guide_text.setText("Press volume down button to check whether it's working properly");
                        status_text_view.setText("Volume down button : Not Detected");
                        status_text_view.setBackgroundColor(getResources().getColor(R.color.RedColor));
                        backbutton.setVisibility(View.VISIBLE);
                        resetbutton.setVisibility(View.VISIBLE);
                        progresslevel++;

                        break;

                    case VOLUME_UP_BUTTON_TEST:

                        squareProgressBar.setProgress(30);
                        status_text_view.setVisibility(View.VISIBLE);
                        hardkeysPosition = 1;
                        status_text_view.setBackgroundColor(getResources().getColor(R.color.RedColor));
                        squareProgressBar.setImage(R.drawable.voulumeuptest);
                        guide_text.setText("Press volume up button to check whether it's working properly");
                        status_text_view.setText("Volume up button : Not Detected");
                        progresslevel++;

                        break;

                    case LOCK_BUTTON_TEST:

                        squareProgressBar.setProgress(45);
                        status_text_view.setVisibility(View.VISIBLE);
                        hardkeysPosition = 2;
                        guide_text.setText("Press Power button to check whether it's working properly");
                        status_text_view.setText("Power button : Not Detected");
                        status_text_view.setBackgroundColor(getResources().getColor(R.color.RedColor));
                        squareProgressBar.setImage(R.drawable.homebuttontest);
                        progresslevel++;
                        break;

                    case BACK_BUTTON_TEST:

                        squareProgressBar.setProgress(60);
                        status_text_view.setVisibility(View.VISIBLE);
                        hardkeysPosition = 3;
                        guide_text.setText("Press Back to check whether it's working properly");
                        status_text_view.setText("Back button : Not Detected");
                        status_text_view.setBackgroundColor(getResources().getColor(R.color.RedColor));
                        squareProgressBar.setImage(R.drawable.backbuttontest);
                        progresslevel++;
                        break;

                    case HOME_BUTTON_TEST:

                        squareProgressBar.setProgress(75);
                        status_text_view.setVisibility(View.VISIBLE);
                        hardkeysPosition = 4;
                        guide_text.setText("Press Home to check whether it's working properly");
                        status_text_view.setText("Home button : Not Detected");
                        status_text_view.setBackgroundColor(getResources().getColor(R.color.RedColor));
                        squareProgressBar.setImage(R.drawable.mainbuttontest);
                        progresslevel++;

                        break;

                    case RECENTS_BUTTON_TEST:

                        squareProgressBar.setProgress(100);
                        stepView.go(0,true);
                        status_text_view.setVisibility(View.VISIBLE);
                        colorsbuttonlayout.setVisibility(View.GONE);
                        hardkeysPosition = 5;
                        guide_text.setText("Press recents to check whether it's working properly");
                        status_text_view.setText("Recents button : Not Detected");
                        status_text_view.setBackgroundColor(getResources().getColor(R.color.RedColor));
                        squareProgressBar.setImage(R.drawable.recentsbuttontest);
                        progresslevel++;
                        break;

                    case SCREEN_TEST:

                        stepView.go(1,true);
                        squareProgressBar.setProgress(0);
                        brightnesslayout.setVisibility(View.GONE);
                        guide_text.setText("Check the RGB colors to test the screen");
                        status_text_view.setVisibility(View.GONE);
                        colorsbuttonlayout.setVisibility(View.VISIBLE);
                        squareProgressBar.setImage(R.drawable.rgbtest);
                        progresslevel++;
                        break;

                    case BRIGHTNESS_TEST:

                        if (r.isPlaying()){
                            r.stop();
                        }
                        stepView.go(1,true);
                        commonbutton.setVisibility(View.GONE);
                        squareProgressBar.setProgress(100);
                        guide_text.setText("Check the brightness level by increasing and decreasing");
                        colorsbuttonlayout.setVisibility(View.GONE);
                        brightnesslayout.setVisibility(View.VISIBLE);
                        squareProgressBar.setImage(R.drawable.brightnesstest);
                        try {
                            brightnessincrease.setOnClickListener(this);
                            brightnessdecrease.setOnClickListener(this);
                            currentbrightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
                            brightnessSeekBar.setProgress(currentbrightness);
                            brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                    upbrightness = progress / (float)255;
                                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                                    lp.screenBrightness = upbrightness;
                                    getWindow().setAttributes(lp);
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });
                            
                        } catch (Settings.SettingNotFoundException e) {
                            Log.e("Error", "Cannot access system brightness");
                            e.printStackTrace();
                        }
                        progresslevel++;
                        break;

                    case VIBRATION_TEST:

                        squareProgressBar.setProgress(25);
                        if (r.isPlaying()){
                            r.stop();
                        }
                        stepView.go(2,true);
                        commonbutton.setText("Vibrate");
                        guide_text.setText("Check whether the vibration is working or not");
                        squareProgressBar.setImage(R.drawable.vibrationtest);
                        brightnesslayout.setVisibility(View.GONE);
                        commonbutton.setVisibility(View.VISIBLE);
                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                // Vibrate for 500 milliseconds
                                vib.vibrate(500);

                            }
                        });
                        progresslevel++;
                        break;

                    case BACK_SPEAKER_TEST:

                        squareProgressBar.setProgress(50);
                        if (mp.isPlaying()){
                            mp.stop();
                        }
                        playing = false;
                        commonbutton.setText("Play sound");
                        guide_text.setText("Check whether the Speaker is working or not");
                        squareProgressBar.setImage(R.drawable.backspeakertest);

                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (playing){
                                    playing = false;
                                    r.stop();
                                    commonbutton.setText("Play sound");
                                }else {
                                    playing = true;
                                    commonbutton.setText("Stop");

                                    r.play();
                                }
                            }
                        });
                        progresslevel++;
                        break;

                    case FRONT_SPEAKER_TEST:

                        if (r.isPlaying()){
                            r.stop();
                        }
                        commonbutton.setText("Play Sound");
                        playing = false;
                        guide_text.setText("Check whether the EarPiece is working or not");
                        squareProgressBar.setImage(R.drawable.frontspeakertest);
                        squareProgressBar.setProgress(75);

                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!playing) {
                                    AudioManager myAudioManager;
                                    myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                    myAudioManager.setSpeakerphoneOn(false);
                                    myAudioManager.setMode(AudioManager.MODE_IN_CALL);
                                    try {
                                        mp.prepare();
                                        mp.start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    playing = true;
                                    commonbutton.setText("Stop");
                                }else {

                                    mp.stop();
                                    commonbutton.setText("Play Sound");
                                    playing = false;
                                }
                            }
                        });
                        progresslevel++;
                        break;
                    case MIC_TEST:

                        if (mp.isPlaying()){
                            mp.stop();
                            commonbutton.setText("Play Sound");
                        }
                        stepView.go(2,true);
                        squareProgressBar.setProgress(100);
                        guide_text.setText("Test your mic by say something");
                        squareProgressBar.setImage(R.drawable.mictest);
                        commonbutton.setText("Open mic");

                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                                try {
                                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                                } catch (ActivityNotFoundException a) {
                                 Log.e("Speech support","Speech not supported");
                                }
                            }
                        });
                        progresslevel++;
                        break;
                    case BACK_CAMERA_TEST:

                        stepView.go(3,true);
                        cameraType = 0;
                        isCameraOpened = false;
                        commonbutton.setText("Open camera");
                        guide_text.setText("Check whether the Back Camera is working or not");
                        squareProgressBar.setImage(R.drawable.cameratest);
                        squareProgressBar.setProgress(25);

                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (isCameraOpened){
                                    isCameraOpened = false;
                                    closeCamera();
                                    textureView.setVisibility(View.GONE);
                                    guide_text.setVisibility(View.VISIBLE);
                                    commonbutton.setText("Open Camera");
                                    stepView.setVisibility(View.VISIBLE);

                                }else {
                                    isCameraOpened = true;
                                    commonbutton.setText("Close Camera");
                                    textureView.setVisibility(View.VISIBLE);
                                    stepView.setVisibility(View.GONE);
                                    guide_text.setVisibility(View.GONE);
                                    openCamera();
                                }


                            }
                        });
                        progresslevel++;
                        break;
                    case FRONT_CAMERA_TEST:

                        cameraType = 1;
                        isCameraOpened = false;
                        guide_text.setText("Check whether the Front Camera is working or not");
                        squareProgressBar.setImage(R.drawable.frontcameratest);
                        squareProgressBar.setProgress(50);
                        commonbutton.setText("Open camera");

                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (isCameraOpened){
                                    isCameraOpened = false;
                                    closeCamera();
                                    textureView.setVisibility(View.GONE);
                                    guide_text.setVisibility(View.VISIBLE);
                                    commonbutton.setText("Open Camera");
                                    stepView.setVisibility(View.VISIBLE);

                                }else {
                                    isCameraOpened = true;
                                    commonbutton.setText("Close Camera");
                                    textureView.setVisibility(View.VISIBLE);
                                    stepView.setVisibility(View.GONE);
                                    guide_text.setVisibility(View.GONE);
                                    openCamera();
                                }


                            }
                        });
                        progresslevel++;
                        break;
                    case BACK_FLASH_TEST:

                        cameraType = 0;
                        isFlashOn = false;
                        guide_text.setText("Check whether the Flash is working or not");
                        squareProgressBar.setImage(R.drawable.flashtest);
                        squareProgressBar.setProgress(75);
                        commonbutton.setText("On");

                        hasFlash = getApplicationContext().getPackageManager()
                                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!hasFlash) {
                                    // device doesn't support flash
                                    // Show alert message and close the application
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                     //Setting message manually and performing action on button click
                                    builder.setMessage("This device doesn't support flash")
                                            .setCancelable(false)
                                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //  Action for 'NO' Button
                                                    dialog.cancel();
                                                }
                                            });

                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    alert.setTitle("Turning on flash error");
                                    alert.show();
                                }else {

                                    if (isFlashOn){

                                        isFlashOn = false;
                                        commonbutton.setText("on");
                                        turnOffFlashLight();

                                    }else {

                                        isFlashOn = true;
                                        commonbutton.setText("Off");
                                        turnOnFlashLight();

                                    }
                                }
                            }
                        });

                        progresslevel++;
                        break;

                    case FRONT_FLASH_TEST:

                        squareProgressBar.setProgress(100);
                        stepView.go(3,true);
                        CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        guide_text.setText("Check whether the Front Flash is working or not");
                        squareProgressBar.setImage(R.drawable.frontflashtest);
                        cameraType = 1;
                        String mCameraId = null;
                        try {
                            mCameraId = mCameraManager.getCameraIdList()[cameraType];
                            final Boolean camlist = mCameraManager.getCameraCharacteristics(mCameraId).get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            }
                            commonbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!camlist) {
                                        // device doesn't support flash
                                        // Show alert message and close the application
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        //Uncomment the below code to Set the message and title from the strings.xml file
                                        //Setting message manually and performing action on button click
                                        builder.setMessage("This device doesn't support front flash")
                                                .setCancelable(false)
                                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //  Action for 'NO' Button
                                                        dialog.cancel();
                                                    }
                                                });

                                        //Creating dialog box
                                        AlertDialog alert = builder.create();
                                        //Setting the title manually
                                        alert.setTitle("Turning on flash error");
                                        alert.show();
                                    }else {

                                        if (isFlashOn){

                                            isFlashOn = false;
                                            commonbutton.setText("on");
                                            turnOffFlashLight();

                                        }else {

                                            isFlashOn = true;
                                            commonbutton.setText("Off");
                                            turnOnFlashLight();

                                        }

                                    }
                                }
                            });


                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                        progresslevel++;
                        break;
                    case ACCELEROMETER_TEST:

                        stepView.go(4,true);
                        guide_text.setText("Tilt the mobile to check the accelerometer sensor");
                        squareProgressBar.setImage(R.drawable.accelerometertest);
                        squareProgressBar.setProgress(25);
                        isSensorOpened = false;
                        commonbutton.setText("Open accelerometer");

                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (isSensorOpened){

                                    isSensorOpened = false;
                                    commonbutton.setText("Open accelerometer");
                                    guide_text.setVisibility(View.VISIBLE);
                                    mainbuttonlayout.setVisibility(View.VISIBLE);
                                    squareProgressBar.setVisibility(View.VISIBLE);
                                    stepView.setVisibility(View.VISIBLE);
                                    sensorlayout.setVisibility(View.GONE);
                                    // Stop the simulation
                                    mSimulationView.stopSimulation();

                                   }else {
                                    isSensorOpened = true;
                                    commonbutton.setText("Close");
                                    guide_text.setVisibility(View.GONE);
                                    mainbuttonlayout.setVisibility(View.GONE);
                                    squareProgressBar.setVisibility(View.GONE);
                                    stepView.setVisibility(View.GONE);
                                    sensorlayout.setVisibility(View.VISIBLE);
                                    // instantiate our simulation view and set it as the activity's content
                                   // Start the simulation
                                    mSimulationView.startSimulation();
                                }

                            }
                        });
                        progresslevel++;
                        break;
                    case COMPASS_TEST:

                        if (isProximityRegistered){
                            mSensorManager.unregisterListener(MobileTestigClass.this);
                            isProximityRegistered = false;
                        }
                        guide_text.setText("Check the compass in the device");
                        sensorProgress = 1;
                        isSensorOpened = false;
                        squareProgressBar.setProgress(50);
                        status_text_view.setVisibility(View.GONE);
                        commonbutton.setText("Open Compass");
                        squareProgressBar.setImage(R.drawable.compasstest);
                        commonbutton.setVisibility(View.VISIBLE);
                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isSensorOpened){
                                    isSensorOpened = false;
                                    gyroscopelayout.setVisibility(View.GONE);
                                    guide_text.setVisibility(View.VISIBLE);
                                    mainbuttonlayout.setVisibility(View.VISIBLE);
                                    squareProgressBar.setVisibility(View.VISIBLE);
                                    stepView.setVisibility(View.VISIBLE);
                                    commonbutton.setText("Open Compass");
                                    gyroCompass.stop();

                                }else {
                                    isSensorOpened = true;
                                    gyroscopelayout.setVisibility(View.VISIBLE);
                                    guide_text.setVisibility(View.GONE);
                                    mainbuttonlayout.setVisibility(View.GONE);
                                    squareProgressBar.setVisibility(View.GONE);
                                    stepView.setVisibility(View.GONE);
                                    commonbutton.setText("Close");
                                    gyroCompass.start();
                                }
                            }
                        });
                        progresslevel++;
                        break;
                    case PROXIMITY_TEST:

                        commonbutton.setVisibility(View.GONE);
                        guide_text.setText("Cover device proximity sensor near call receiver with your hand");
                        if (isGyroscopeReegistered){
                            mSensorManager.unregisterListener(gyroListener);
                            isGyroscopeReegistered = false;
                        }
                        if (isProximityRegistered){
                            mSensorManager.unregisterListener(MobileTestigClass.this);
                            isProximityRegistered = false;
                        }
                        squareProgressBar.setProgress(75);
                        squareProgressBar.setImage(R.drawable.proximitytest);
                        status_text_view.setVisibility(View.VISIBLE);
                        if (!isProximityRegistered){
                            mSensorManager.registerListener(MobileTestigClass.this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
                            isProximityRegistered = true;
                        }else {

                        }
                        progresslevel++;
                        break;
                    case GYROMETER_TEST:

                        stepView.go(4,true);
                        PackageManager packageManager = getPackageManager();
                        boolean gyroExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

                        if (isProximityRegistered){
                            mSensorManager.unregisterListener(MobileTestigClass.this);
                            isProximityRegistered = false;
                        }
                        status_text_view.setVisibility(View.VISIBLE);
                        squareProgressBar.setProgress(100);
                        commonbutton.setVisibility(View.GONE);
                        squareProgressBar.setImage(R.drawable.gyrotest);
                        guide_text.setText("Rotate your device in X, Y, Z coorinate to check the gyroscope values");
                        if (gyroExists) {
                            if (!isGyroscopeReegistered) {
                                // Register the listener
                                mSensorManager.registerListener(gyroListener,
                                        mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                                isGyroscopeReegistered = true;
                            }
                        }else {
                            guide_text.setText("Gyroscope sensor is not available for your device");
                            status_text_view.setVisibility(View.GONE);
                        }
                        progresslevel++;
                        break;
                    case SINGLE_TOUCH_TEST:

                        if (isGyroscopeReegistered){
                            mSensorManager.unregisterListener(gyroListener);
                            isGyroscopeReegistered = false;
                        }
                        nextbutton.setText("Next");
                        stepView.go(5,true);
                        status_text_view.setVisibility(View.GONE);
                        isSingleTouch = true;
                        commonbutton.setVisibility(View.VISIBLE);
                        commonbutton.setText("Open Drawing screen");
                        squareProgressBar.setImage(R.drawable.singletouchtest);
                        squareProgressBar.setProgress(50);
                        guide_text.setText("Draw on the screen to test the single touch");

                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isTouchScreenOpened){
                                    isTouchScreenOpened = false;
                                    commonbutton.setText("Open Drawing screen");
                                    mainbuttonlayout.setVisibility(View.VISIBLE);
                                    stepView.setVisibility(View.VISIBLE);
                                    squareProgressBar.setVisibility(View.VISIBLE);
                                    guide_text.setVisibility(View.VISIBLE);
                                    singleTouchLayout.setVisibility(View.GONE);
                                    mySurfaceView.onPauseMySurfaceView();

                                }else {
                                    isTouchScreenOpened = true;
                                    commonbutton.setText("Close");
                                    mainbuttonlayout.setVisibility(View.GONE);
                                    stepView.setVisibility(View.GONE);
                                    status_text_view.setVisibility(View.GONE);
                                    squareProgressBar.setVisibility(View.GONE);
                                    guide_text.setVisibility(View.GONE);
                                    singleTouchLayout.setVisibility(View.VISIBLE);
                                    mySurfaceView.onResumeMySurfaceView();
                                }
                            }
                        });
                        progresslevel++;
                        break;
                    case MULTI_TOUCH_TEST:

                        isSingleTouch = false;
                        isTouchScreenOpened = false;
                        nextbutton.setText("Finish");
                        squareProgressBar.setProgress(100);
                        commonbutton.setVisibility(View.VISIBLE);
                        commonbutton.setText("Open Drawing screen");
                        squareProgressBar.setImage(R.drawable.multitouchtest);
                        guide_text.setText("Draw on the screen to test the multi touch");

                        commonbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isTouchScreenOpened){
                                    isTouchScreenOpened = false;
                                    commonbutton.setText("Open Drawing screen");
                                    mainbuttonlayout.setVisibility(View.VISIBLE);
                                   // status_text_view.setVisibility(View.VISIBLE);
                                    squareProgressBar.setVisibility(View.VISIBLE);
                                    guide_text.setVisibility(View.VISIBLE);
                                    singleTouchLayout.setVisibility(View.GONE);
                                    mySurfaceView.onPauseMySurfaceView();
                                    stepView.setVisibility(View.VISIBLE);

                                }else {
                                    isTouchScreenOpened = true;
                                    commonbutton.setText("Close");
                                    mainbuttonlayout.setVisibility(View.GONE);
                                    stepView.setVisibility(View.GONE);
                                    status_text_view.setVisibility(View.GONE);
                                    squareProgressBar.setVisibility(View.GONE);
                                    guide_text.setVisibility(View.GONE);
                                    singleTouchLayout.setVisibility(View.VISIBLE);
                                    mySurfaceView.onResumeMySurfaceView();
                                }
                            }
                        });
                        progresslevel++;
                        break;
                    case FINISH:

                        if (isProximityRegistered){
                            mSensorManager.unregisterListener(MobileTestigClass.this);
                            isProximityRegistered = false;
                        }
                        if (isGyroscopeReegistered){
                            mSensorManager.unregisterListener(gyroListener);
                            isGyroscopeReegistered = false;
                        }
                        if (mp.isPlaying()){
                            mp.stop();
                        }

                        if (r.isPlaying()){
                            r.stop();
                        }

                        colorsbuttonlayout.setVisibility(View.GONE);
                        brightnesslayout.setVisibility(View.GONE);
                        commonbutton.setVisibility(View.GONE);
                        progresslevel = 0;
                        guide_text.setText("Test your mobile status this will guide you through the series of testing screen");
                        status_text_view.setVisibility(View.GONE);
                        stepView.setVisibility(View.INVISIBLE);
                        squareProgressBar.setImage(R.drawable.mobiletestlogo);
                        squareProgressBar.setProgress(0);
                        commonbutton.setVisibility(View.GONE);
                        nextbutton.setText("Begin test");
                        break;
                }
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (progresslevel != 0) {
                    if (progresslevel == 1) {
                        progresslevel = 22;
                        nextbutton.performClick();
                    } else {
                        progresslevel--;
                        progresslevel--;
                        nextbutton.performClick();
                    }
                }
            }
        });

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progresslevel = 22;
                nextbutton.performClick();
            }
        });

        redcolorbutton.setOnClickListener(this);
        greencolorbutton.setOnClickListener(this);
        bluecolorbutton.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mobiletesttoolbar = (Toolbar) findViewById(R.id.mobiletest_details_toolbar);
        setSupportActionBar(mobiletesttoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Test My Mobile");
        }

    private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            Log.d("can write", "Can Write Settings: " + retVal);
            if(retVal){

                Log.d("can write", "Write allowed: " + retVal);
            }else{

            }
        }
        return retVal;
    }

    @Override
    protected void onUserLeaveHint()
    {
        Log.d("onUserLeaveHint","Home button pressed");
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

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
    protected void onPause() {
        super.onPause();
        if (hardkeysPosition == 2) {

          }else if (hardkeysPosition == 4) {

            status_text_view.setBackgroundColor(getResources().getColor(R.color.navigation_background));
            status_text_view.setText("Home button : Detected");

        }else if (hardkeysPosition == 5) {
            status_text_view.setBackgroundColor(getResources().getColor(R.color.navigation_background));
            status_text_view.setText("Recents button : Detected");

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("event",String.valueOf(event.getKeyCode()));
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){

            if (hardkeysPosition == 0){

                status_text_view.setBackgroundColor(getResources().getColor(R.color.navigation_background));
                status_text_view.setTextColor(getResources().getColor(R.color.whitecolor));
                status_text_view.setText("Volume down button : Detected");
            }

            //Do something
        } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){

            if (hardkeysPosition == 1) {

                status_text_view.setBackgroundColor(getResources().getColor(R.color.navigation_background));
                status_text_view.setText("Volume up button : Detected");
            }

        }else if ((keyCode == KeyEvent.KEYCODE_BACK)){

            if (hardkeysPosition == 3) {

                status_text_view.setBackgroundColor(getResources().getColor(R.color.navigation_background));
                status_text_view.setText("back button : Detected");
            }else if (progresslevel == BRIGHTNESS_TEST){

                if (colorpopupWindow.isShowing()){
                    colorpopupWindow.dismiss();
                }

            }
        }else if ((keyCode == KeyEvent.KEYCODE_POWER))
        {
            Log.e("","");

            event.startTracking();
            return true;
        }

        return true;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            Log.e("","");
            // Do something here...
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    public class MyBroadCastReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                if (hardkeysPosition == 2) {
                    status_text_view.setBackgroundColor(getResources().getColor(R.color.navigation_background));
                    status_text_view.setText("Power button : Detected");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.red_button:

                if (rgblevel.contains("1")){

                }else {
                    rgblevel = rgblevel + String.valueOf(1);
                    rgbvalue += 25;
                    squareProgressBar.setProgress(rgbvalue);
                }
                colorpopupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
                ((LinearLayout)colorpopupWindow.getContentView().findViewById(R.id.rgb_testing_layout)).setBackgroundColor(getResources().getColor(R.color.RedColor));
                break;
            case R.id.blue_button:
                if (rgblevel.contains("3")){

                }else {
                    rgblevel = rgblevel + String.valueOf(3);
                    rgbvalue += 25;
                    squareProgressBar.setProgress(rgbvalue);
                }
                colorpopupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
                ((LinearLayout)colorpopupWindow.getContentView().findViewById(R.id.rgb_testing_layout)).setBackgroundColor(getResources().getColor(R.color.BlueColor));
                break;
            case R.id.green_button:
                if (rgblevel.contains("2")){

                }else {
                    rgblevel = rgblevel + String.valueOf(2);
                    rgbvalue += 25;
                    squareProgressBar.setProgress(rgbvalue);

                }
                colorpopupWindow.showAtLocation(mainView, Gravity.CENTER,0,0);
                ((LinearLayout)colorpopupWindow.getContentView().findViewById(R.id.rgb_testing_layout)).setBackgroundColor(getResources().getColor(R.color.GreenColor));
                break;

        }

    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[cameraType];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }else {
                manager.openCamera(cameraId, stateCallback, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                   // Toast.makeText(AndroidCameraApi.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }
    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String mCameraId = mCameraManager.getCameraIdList()[cameraType];
                mCameraManager.setTorchMode(mCameraId, true);
            }else {

                camera = Camera.open();
                parameters = camera.getParameters();
                parameters.setFlashMode(parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String mCameraId = mCameraManager.getCameraIdList()[cameraType];
                mCameraManager.setTorchMode(mCameraId, false);
             //   playOnOffSound();
             //   mTorchOnOffButton.setImageResource(R.drawable.off);

            }else {
                parameters = camera.getParameters();
                parameters.setFlashMode(parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {

                status_text_view.setText("Proximity sensor : Object near");


            } else {
                status_text_view.setText("Proximity sensor : Object far");
            }
        }

    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            status_text_view.setText("X : "+ (int)x + " rad/s Y : "+(int)y+"rad/s Z : "+(int)z + " rad/s");
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //Sensor methods
    class SimulationView extends FrameLayout implements SensorEventListener {
        // diameter of the balls in meters
        private static final float sBallDiameter = 0.004f;
        private static final float sBallDiameter2 = sBallDiameter * sBallDiameter;

        private final int mDstWidth;
        private final int mDstHeight;

        private Sensor mAccelerometer;
        private long mLastT;

        private float mXDpi;
        private float mYDpi;
        private float mMetersToPixelsX;
        private float mMetersToPixelsY;
        private float mXOrigin;
        private float mYOrigin;
        private float mSensorX;
        private float mSensorY;
        private float mHorizontalBound;
        private float mVerticalBound;
        private final ParticleSystem mParticleSystem;
        /*
         * Each of our particle holds its previous and current position, its
         * acceleration. for added realism each particle has its own friction
         * coefficient.
         */
        class Particle extends View {
            private float mPosX = (float) Math.random();
            private float mPosY = (float) Math.random();
            private float mVelX;
            private float mVelY;

            public Particle(Context context) {
                super(context);
            }

            public Particle(Context context, AttributeSet attrs) {
                super(context, attrs);
            }

            public Particle(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
            }

           // @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public Particle(Context context, AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
                super(context, attrs, defStyleAttr, defStyleRes);
            }

            public void computePhysics(float sx, float sy, float dT) {

                final float ax = -sx/5;
                final float ay = -sy/5;

                mPosX += mVelX * dT + ax * dT * dT / 2;
                mPosY += mVelY * dT + ay * dT * dT / 2;

                mVelX += ax * dT;
                mVelY += ay * dT;
            }

            /*
             * Resolving constraints and collisions with the Verlet integrator
             * can be very simple, we simply need to move a colliding or
             * constrained particle in such way that the constraint is
             * satisfied.
             */
            public void resolveCollisionWithBounds() {
                final float xmax = mHorizontalBound;
                final float ymax = mVerticalBound;
                final float x = mPosX;
                final float y = mPosY;
                if (x > xmax) {
                    mPosX = xmax;
                    mVelX = 0;
                } else if (x < -xmax) {
                    mPosX = -xmax;
                    mVelX = 0;
                }
                if (y > ymax) {
                    mPosY = ymax;
                    mVelY = 0;
                } else if (y < -ymax) {
                    mPosY = -ymax;
                    mVelY = 0;
                }
            }
        }

        /*
         * A particle system is just a collection of particles
         */
        class ParticleSystem {
            static final int NUM_PARTICLES = 5;
            private Particle mBalls[] = new Particle[NUM_PARTICLES];

            ParticleSystem() {
                /*
                 * Initially our particles have no speed or acceleration
                 */
                for (int i = 0; i < mBalls.length; i++) {
                    mBalls[i] = new Particle(getContext());
                    mBalls[i].setBackgroundResource(R.drawable.sensorball);
                    mBalls[i].setLayerType(LAYER_TYPE_HARDWARE, null);
                    addView(mBalls[i], new ViewGroup.LayoutParams(mDstWidth, mDstHeight));
                }
            }

            /*
             * Update the position of each particle in the system using the
             * Verlet integrator.
             */
            private void updatePositions(float sx, float sy, long timestamp) {
                final long t = timestamp;
                if (mLastT != 0) {
                    final float dT = (float) (t - mLastT) / 1000.f /** (1.0f / 1000000000.0f)*/;
                    final int count = mBalls.length;
                    for (int i = 0; i < count; i++) {
                        Particle ball = mBalls[i];
                        ball.computePhysics(sx, sy, dT);
                    }
                }
                mLastT = t;
            }

            /*
             * Performs one iteration of the simulation. First updating the
             * position of all the particles and resolving the constraints and
             * collisions.
             */
            public void update(float sx, float sy, long now) {
                // update the system's positions
                updatePositions(sx, sy, now);

                // We do no more than a limited number of iterations
                final int NUM_MAX_ITERATIONS = 10;

                /*
                 * Resolve collisions, each particle is tested against every
                 * other particle for collision. If a collision is detected the
                 * particle is moved away using a virtual spring of infinite
                 * stiffness.
                 */
                boolean more = true;
                final int count = mBalls.length;
                for (int k = 0; k < NUM_MAX_ITERATIONS && more; k++) {
                    more = false;
                    for (int i = 0; i < count; i++) {
                        Particle curr = mBalls[i];
                        for (int j = i + 1; j < count; j++) {
                            Particle ball = mBalls[j];
                            float dx = ball.mPosX - curr.mPosX;
                            float dy = ball.mPosY - curr.mPosY;
                            float dd = dx * dx + dy * dy;
                            // Check for collisions
                            if (dd <= sBallDiameter2) {
                                /*
                                 * add a little bit of entropy, after nothing is
                                 * perfect in the universe.
                                 */
                                dx += ((float) Math.random() - 0.5f) * 0.0001f;
                                dy += ((float) Math.random() - 0.5f) * 0.0001f;
                                dd = dx * dx + dy * dy;
                                // simulate the spring
                                final float d = (float) Math.sqrt(dd);
                                final float c = (0.5f * (sBallDiameter - d)) / d;
                                final float effectX = dx * c;
                                final float effectY = dy * c;
                                curr.mPosX -= effectX;
                                curr.mPosY -= effectY;
                                ball.mPosX += effectX;
                                ball.mPosY += effectY;
                                more = true;
                            }
                        }
                        curr.resolveCollisionWithBounds();
                    }
                }
            }

            public int getParticleCount() {
                return mBalls.length;
            }

            public float getPosX(int i) {
                return mBalls[i].mPosX;
            }

            public float getPosY(int i) {
                return mBalls[i].mPosY;
            }
        }

        public void startSimulation() {
            /*
             * It is not necessary to get accelerometer events at a very high
             * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
             * automatic low-pass filter, which "extracts" the gravity component
             * of the acceleration. As an added benefit, we use less power and
             * CPU resources.
             */
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        public void stopSimulation() {
            mSensorManager.unregisterListener(this);
        }

        public SimulationView(Context context) {
            super(context);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mXDpi = metrics.xdpi;
            mYDpi = metrics.ydpi;
            mMetersToPixelsX = mXDpi / 0.0254f;
            mMetersToPixelsY = mYDpi / 0.0254f;

            // rescale the ball so it's about 0.5 cm on screen
            mDstWidth = (int) (sBallDiameter * mMetersToPixelsX + 0.5f);
            mDstHeight = (int) (sBallDiameter * mMetersToPixelsY + 0.5f);
            mParticleSystem = new ParticleSystem();

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inDither = true;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {

            mXOrigin = (w - mDstWidth) * 0.5f;
            mYOrigin = (h - mDstHeight) * 0.5f;
            mHorizontalBound = ((w / mMetersToPixelsX - sBallDiameter) * 0.5f);
            mVerticalBound = ((h / mMetersToPixelsY - sBallDiameter) * 0.5f);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;
            /*
             * record the accelerometer data, the event's timestamp as well as
             * the current time. The latter is needed so we can calculate the
             * "present" time during rendering. In this application, we need to
             * take into account how the screen is rotated with respect to the
             * sensors (which always return data in a coordinate space aligned
             * to with the screen in its native orientation).
             */

            switch (mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    mSensorX = event.values[0];
                    mSensorY = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    mSensorX = -event.values[1];
                    mSensorY = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    mSensorX = -event.values[0];
                    mSensorY = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    mSensorX = event.values[1];
                    mSensorY = -event.values[0];
                    break;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {

            final ParticleSystem particleSystem = mParticleSystem;
            final long now = System.currentTimeMillis();
            final float sx = mSensorX;
            final float sy = mSensorY;

            particleSystem.update(sx, sy, now);

            final float xc = mXOrigin;
            final float yc = mYOrigin;
            final float xs = mMetersToPixelsX;
            final float ys = mMetersToPixelsY;
            final int count = particleSystem.getParticleCount();
            for (int i = 0; i < count; i++) {
                /*
                 * We transform the canvas so that the coordinate system matches
                 * the sensors coordinate system with the origin in the center
                 * of the screen and the unit is the meter.
                 */
                final float x = xc + particleSystem.getPosX(i) * xs;
                final float y = yc - particleSystem.getPosY(i) * ys;
                particleSystem.mBalls[i].setTranslationX(x);
                particleSystem.mBalls[i].setTranslationY(y);
            }

            invalidate();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public class Compass implements SensorEventListener {
        private static final String TAG = "Compass";

        private SensorManager sensorManager;
        private Sensor gsensor;
        private Sensor msensor;
        private float[] mGravity = new float[3];
        private float[] mGeomagnetic = new float[3];
        private float azimuth = 0f;
        private float currectAzimuth = 0;

        public ImageView arrowView = null;

        public Compass(Context context) {
            sensorManager = (SensorManager) context
                    .getSystemService(Context.SENSOR_SERVICE);
            gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        public void start() {
            sensorManager.registerListener(this, gsensor,
                    SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, msensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }

        public void stop() {
            sensorManager.unregisterListener(this);
        }

        private void adjustArrow() {
            if (arrowView == null) {
                Log.i(TAG, "arrow view is not set");
                return;
            }

            Log.i(TAG, "will set rotation from " + currectAzimuth + " to "
                    + azimuth);

            Animation an = new RotateAnimation(-currectAzimuth, -azimuth,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            currectAzimuth = azimuth;

            an.setDuration(500);
            an.setRepeatCount(0);
            an.setFillAfter(true);

            arrowView.startAnimation(an);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            final float alpha = 0.97f;

            synchronized (this) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                            * event.values[0];
                    mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                            * event.values[1];
                    mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                            * event.values[2];
                }

                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                            * event.values[0];
                    mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                            * event.values[1];
                    mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                            * event.values[2];


                }

                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                        mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    azimuth = (float) Math.toDegrees(orientation[0]); // orientation
                    azimuth = (azimuth + 360) % 360;
                    adjustArrow();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    class MySurfaceView extends SurfaceView implements Runnable{

        //In this test, handle maximum of 2 pointer
        final int MAX_POINT_CNT = 2;

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float[] x = new float[MAX_POINT_CNT];
        float[] y = new float[MAX_POINT_CNT];
        boolean[] isTouch = new boolean[MAX_POINT_CNT];

        float[] x_last = new float[MAX_POINT_CNT];
        float[] y_last = new float[MAX_POINT_CNT];
        boolean[] isTouch_last = new boolean[MAX_POINT_CNT];


        Thread thread = null;
        SurfaceHolder surfaceHolder;
        volatile boolean running = false;

        volatile boolean touched = false;
        volatile float touched_x, touched_y;

        public MySurfaceView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            surfaceHolder = getHolder();

        }

        public void onResumeMySurfaceView(){
            running = true;
            thread = new Thread(this);
            thread.start();
        }

        public void onPauseMySurfaceView(){
            boolean retry = true;
            running = false;
            while(retry){
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while(running){
                if(surfaceHolder.getSurface().isValid()){
                    Canvas canvas = surfaceHolder.lockCanvas();
                    //... actual drawing on canvas

                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(1);

                    if(isTouch[0]){
                        if(isTouch_last[0]){
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(5);
                            paint.setColor(Color.RED);
                            canvas.drawLine(x_last[0], y_last[0], x[0], y[0], paint);
                        }
                    }
                    if(isTouch[1]){
                        if(isTouch_last[1]){
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(5);
                            paint.setColor(Color.BLUE);
                            if (!isSingleTouch) {
                                canvas.drawLine(x_last[1], y_last[1], x[1], y[1], paint);
                            }
                        }
                    }

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int pointerIndex = ((motionEvent.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)
                    >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            int pointerId = motionEvent.getPointerId(pointerIndex);
            int action = (motionEvent.getAction() & MotionEvent.ACTION_MASK);
            int pointCnt = motionEvent.getPointerCount();

            if (pointCnt <= MAX_POINT_CNT){
                if (pointerIndex <= MAX_POINT_CNT - 1){

                    for (int i = 0; i < pointCnt; i++) {
                        int id = motionEvent.getPointerId(i);
                        x_last[id] = x[id];
                        y_last[id] = y[id];
                        isTouch_last[id] = isTouch[id];
                        x[id] = motionEvent.getX(i);
                        y[id] = motionEvent.getY(i);
                    }

                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            isTouch[pointerId] = true;
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            isTouch[pointerId] = true;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            isTouch[pointerId] = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            isTouch[pointerId] = false;
                            isTouch_last[pointerId] = false;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            isTouch[pointerId] = false;
                            isTouch_last[pointerId] = false;
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            isTouch[pointerId] = false;
                            isTouch_last[pointerId] = false;
                            break;
                        default:
                            isTouch[pointerId] = false;
                            isTouch_last[pointerId] = false;
                    }
                }
            }

            return true;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    guide_text.setText(result.get(0));
                }
                break;
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }else {
                openCamera();
            }
        }
    }
}
