package com.github.jasonwangdev.capture.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2017/7/2.
 */

public class PermissionUtils {

    private static final int PERMISSION_REQUEST = 0x01;


    public static boolean checkPermission(Fragment fragment, String permission) {
        return checkPermission(fragment.getContext(), permission);
    }

    public static boolean checkPermission(Context context, String permission) {
        if (null == context || null == permission)
            return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int result = ContextCompat.checkSelfPermission(context, permission);

            return result == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


    public static void requestPermission(Fragment fragment, String permission) {
        requestPermission(fragment, new String[]{permission});
    }

    public static void requestPermission(Fragment fragment, String[] permissions) {
        if (null == fragment || null == permissions || permissions.length <= 0)
            return;

        fragment.requestPermissions(permissions, PERMISSION_REQUEST);
    }


    public static List<PermissionResult> getPermissionResults(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        List<PermissionResult> permissionResults = new ArrayList<>();

        if (null != fragment &&
            null != permissions &&
            null != grantResults &&
            permissions.length > 0 &&
            permissions.length == grantResults.length)
        {
            if (PERMISSION_REQUEST == requestCode)
            {
                for (int index = 0 ; index < permissions.length ; index++)
                    permissionResults.add(new PermissionResult(permissions[index],
                                                               grantResults[index] == PackageManager.PERMISSION_GRANTED,
                                                               !fragment.shouldShowRequestPermissionRationale(permissions[index])));
            }
        }

        return permissionResults;
    }

}
