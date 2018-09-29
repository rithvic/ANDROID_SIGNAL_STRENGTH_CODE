package com.quadrobay.qbayApps.networkproject.WidgetClass;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.quadrobay.qbayApps.networkproject.ActivityClasses.MainActivity;
import com.quadrobay.qbayApps.networkproject.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    PhoneStateListener phoneStateListener;
    TelephonyManager telephonyManager;
    WifiManager wifiManager;
    String percentage,secondpercentage = "0%";
    String sim1operator,sim2operator = "Unknown";
    int simcount;
    int strength,secondstrength = 0;
    RemoteViews remoteViews;
    int rssi,level;

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("appWidgets Length", String.valueOf(appWidgetIds.length));

        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {

            final int widgetId = appWidgetIds[i];
            //String number = String.format("%03d", (new Random().nextInt(900) + 100));

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

                SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
                simcount = subscriptionManager.getActiveSubscriptionInfoCountMax();
                Log.e("SimCount", String.valueOf(simcount));
            }

            try {
                Class<?> simDetectClass = Class.forName("android.telephony.TelephonyManager");
                Method detectMethod = simDetectClass.getDeclaredMethod("getSimState",int.class);

                Field[] fields = simDetectClass.getFields();
                Log.e("Fields", String.valueOf(fields));

                int sim1state = (int) detectMethod.invoke(telephonyManager,0);

                switch (sim1state) {
                    case TelephonyManager.SIM_STATE_ABSENT:
                        remoteViews.setViewVisibility(R.id.SIM1_view, View.GONE);
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                        remoteViews.setViewVisibility(R.id.SIM1_view, View.GONE);
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                        remoteViews.setViewVisibility(R.id.SIM1_view, View.GONE);
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                        remoteViews.setViewVisibility(R.id.SIM1_view, View.GONE);
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_READY:
                        //FirstSimMethod();
                        phoneStateListener = new PhoneStateListener(){
                            public void onCallForwardingIndicatorChanged(boolean cfi){}
                            public void onCallStateChanged(int state, String incomingNumber){}
                            public void onCellLocationChanged(CellLocation location){}
                            public void onDataActivity(int direction){}
                            public void onDataConnectionStateChanged(int state){}
                            public void onMessageWaitIndicatorChanged(boolean mwi){}
                            public void onServiceStateChanged(ServiceState serviceState){}
                            public void onSignalStrengthsChanged(SignalStrength signalStrength){

                                int rssi = -113 + 2 * signalStrength.getGsmSignalStrength();
                                Log.e("Math", String.valueOf(Math.abs(rssi)));
                                strength = Math.abs(rssi);
                                percentage = String.valueOf(Math.abs(rssi)) + "%";
                                sim1operator = telephonyManager.getSimOperatorName();
                                widgetView(context,appWidgetManager,widgetId);
                            }
                        };

                        try{
                            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_CALL_STATE );
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_UNKNOWN:
                        remoteViews.setViewVisibility(R.id.SIM1_view, View.GONE);
                        // do something
                        break;
                    case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                        remoteViews.setViewVisibility(R.id.SIM1_view, View.GONE);
                        break;
                    case TelephonyManager.SIM_STATE_PERM_DISABLED:
                        remoteViews.setViewVisibility(R.id.SIM1_view, View.GONE);
                        break;
                    case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                        remoteViews.setViewVisibility(R.id.SIM1_view, View.GONE);
                        break;
                }

                //SIM 2 Information
                if (simcount == 2) {

                    int sim2state = (int) detectMethod.invoke(telephonyManager, 1);

                    switch (sim2state) {
                        case TelephonyManager.SIM_STATE_ABSENT:
                            remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                            remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                            remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                            remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_READY:
                            //SeconSimMethod();

                            //Dual sim methods
                            final Class<?> tmClassSM;
                            Class<?> tClassSM;
                            try {
                                tmClassSM = Class.forName("android.telephony.SubscriptionManager");
                                // Static method to return list of active subids
                                Method method[] = tmClassSM.getDeclaredMethods();
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                                    Method methodsub = SubscriptionManager.class.getMethod("getActiveSubscriptionIdList");
                                    int[] ssubIdList = (int[])methodsub.invoke(SubscriptionManager.from(context));

                                    SubscriptionManager sm = SubscriptionManager.from(context);
                                    List<SubscriptionInfo> subscriptions = sm.getActiveSubscriptionInfoList();

                                    if (subscriptions != null) {

                                        for (SubscriptionInfo subscriptioninfo : subscriptions) {

                                            if (subscriptioninfo.getSimSlotIndex() == 1) {
                                                sim2operator = (String) subscriptioninfo.getCarrierName();
                                                MultiSimWidgetListener listener = new MultiSimWidgetListener(subscriptioninfo.getSubscriptionId());
                                                telephonyManager.listen(listener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_CALL_STATE);
                                                widgetView(context,appWidgetManager,widgetId);
                                            }
                                        }
                                    }
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_UNKNOWN:
                            remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                            remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
                            break;
                        case TelephonyManager.SIM_STATE_PERM_DISABLED:
                            remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
                            break;
                        case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                            remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
                            break;
                    }
                }else{
                    remoteViews.setViewVisibility(R.id.SIM2_view, View.GONE);
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

        }


    }

    private void widgetView(Context context, AppWidgetManager appWidgetManager, int widgetId) {

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        remoteViews.setTextViewText(R.id.sim1_operator,sim1operator);
        remoteViews.setTextViewText(R.id.tv, percentage);
        remoteViews.setProgressBar(R.id.progressBar,100, strength,false);
        remoteViews.setTextViewText(R.id.sim2_operator,sim2operator);
        remoteViews.setTextViewText(R.id.tv1, secondpercentage);
        remoteViews.setProgressBar(R.id.progressBar1,100, secondstrength,false);

        if (wifiManager.isWifiEnabled()){
            rssi = wifiManager.getWifiState();
            switch (rssi){
                case 0:
                    remoteViews.setTextViewText(R.id.signal_level,"Very Poor");
                    break;
                case 1:
                    remoteViews.setTextViewText(R.id.signal_level,"Poor");
                    break;
                case 2:
                    remoteViews.setTextViewText(R.id.signal_level,"Moderate");
                    break;
                case 3:
                    remoteViews.setTextViewText(R.id.signal_level,"Good");
                    break;
                case 4:
                    remoteViews.setTextViewText(R.id.signal_level,"Excellent");
                    break;
            }
        }else {
            remoteViews.setTextViewText(R.id.signal_level,"Offline");
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_view,pendingIntent);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    public class MultiSimWidgetListener extends PhoneStateListener {

        private Field subIdField;
        private Integer subId = -1;

        public MultiSimWidgetListener (Integer subId) {
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
            int rssi = -113 + 2 * signalStrength.getGsmSignalStrength();
            Log.e("Math_Second", String.valueOf(Math.abs(rssi)));
            secondstrength = Math.abs(rssi);
            secondpercentage = String.valueOf(Math.abs(rssi)) + "%";
        }

    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}