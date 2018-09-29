package com.quadrobay.qbayApps.networkproject.Services;

import android.telephony.PhoneStateListener;

/**
 * Created by benchmark on 17/10/17.
 */

public interface MultiSimTelephonyManager {

    public void listen(PhoneStateListener listener, int events);

}
