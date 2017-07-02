package com.github.jasonwangdev.capture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import com.github.jasonwangdev.capture.utils.FileUtils;
import com.github.jasonwangdev.capture.utils.HardwareUtils;
import com.github.jasonwangdev.capture.utils.PermissionResult;
import com.github.jasonwangdev.capture.utils.PermissionUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by Jason on 2017/7/2.
 */

public class ImageCapture {

    private static final String CAMERA_HARDWARE = PackageManager.FEATURE_CAMERA;

    private static final String STORAGE_PERMISSIONS = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final String FOLDER_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
    private static final String FILE_NAME_FORMAT = "IMG_%tC%<ty%<tm%<td-%<tL.jpg";

    private static final int REQUEST_CAMERA = 0x01;

    private OnCaptureListener listener;

    private Fragment fragment;
    private File file;


    public void capture(Fragment fragment) {
        this.fragment = fragment;

        if (!HardwareUtils.checkHardware(fragment, CAMERA_HARDWARE))
        {
            if (null != listener)
                listener.onCaptureError(Error.CAMERA_NOT_SUPPORT);

            return;
        }

        if (!PermissionUtils.checkPermission(fragment, STORAGE_PERMISSIONS))
            PermissionUtils.requestPermission(fragment, STORAGE_PERMISSIONS);
        else
            startCapture();
    }

    public void setOnCaptureListener(OnCaptureListener listener) {
        this.listener = listener;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<PermissionResult> permissionResults = PermissionUtils.getPermissionResults(fragment, requestCode, permissions, grantResults);
        for(PermissionResult permissionResult : permissionResults)
        {
            if (!permissionResult.isGrant())
            {
                if (STORAGE_PERMISSIONS.equals(permissionResult.getPermission()))
                {
                    if (null != listener)
                        listener.onCaptureError(permissionResult.isChoseNeverAskAgain() ? Error.STORAGE_PERMISSION_NEVER_DENIED : Error.STORAGE_PERMISSION_DENIED);

                    return;
                }
            }
        }

        startCapture();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CAMERA == requestCode)
        {
            if (Activity.RESULT_OK == resultCode)
            {
                if (null != listener)
                    listener.onCaptured(file);
            }
        }
    }


    private void startCapture() {
        file = FileUtils.createFile(FOLDER_PATH, String.format(FILE_NAME_FORMAT, new Date().getTime()));
        if (null == file)
        {
            if (null != listener)
                listener.onCaptureError(Error.FILE_FAILED);
        }

        openCamera();
    }

    private void openCamera() {
        if (null == fragment || null == file)
            return;

        Uri uri;
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                uri = FileProvider.getUriForFile(fragment.getContext(),
                                              fragment.getContext().getPackageName() + ".fileProvider",
                                              file);
            else
                uri = Uri.fromFile(file);
        }
        catch (NullPointerException e)
        {
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        fragment.startActivityForResult(intent, REQUEST_CAMERA);
    }

}
