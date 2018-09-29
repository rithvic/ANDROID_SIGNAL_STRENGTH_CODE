package com.quadrobay.qbayApps.networkproject.Services;

import android.content.Context;
import android.telephony.PhoneStateListener;

/**
 * Created by benchmark on 21/10/17.
 */

public class CustomPhoneStateListener extends PhoneStateListener {

    Context mContext;
    public static String LOG_TAG = "CustomPhoneStateListener";

    public CustomPhoneStateListener(Context context) {
        mContext = context;
    }
}
