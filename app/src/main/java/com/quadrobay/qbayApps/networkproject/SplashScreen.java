package com.quadrobay.qbayApps.networkproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.quadrobay.qbayApps.networkproject.ActivityClasses.MainActivity;
import com.quadrobay.qbayApps.networkproject.Services.MyServicesLocation;
import com.quadrobay.qbayApps.networkproject.Services.NetworkSniffTask;

/**
 * Created by benchmark on 12/10/17.
 */

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        new NetworkSniffTask(SplashScreen.this, new NetworkSniffTask.deviceListCallBack() {
            @Override
            public void onGetDeviceList(int deviceCount) {

            }

            @Override
            public void onStartGetDeviceList() {
            }
        }).execute();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
