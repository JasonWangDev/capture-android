package com.github.jasonwangdev.capture.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

/**
 * Created by Jason on 2017/7/2.
 */

public class HardwareUtils {

    public static boolean checkHardware(Fragment fragment, String feature) {
        return checkHardware(fragment.getContext(), feature);
    }

    public static boolean checkHardware(Context context, String feature) {
        if (null == context || null == feature)
            return false;

        PackageManager pm = context.getPackageManager();

        return pm.hasSystemFeature(feature);
    }

}
