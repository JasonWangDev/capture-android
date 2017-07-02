package com.github.jasonwangdev.capture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
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

public class VideoCapture {

    private static final String CAMERA_HARDWARE = PackageManager.FEATURE_CAMERA;

    private static final String STORAGE_PERMISSIONS = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final String FOLDER_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath();
    private static final String FILE_NAME_FORMAT = "VIDEO_%tC%<ty%<tm%<td-%<tL.mp4";

    private static final int REQUEST_CAMERA = 0x02;

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

    public Bitmap getThumbnail() {
        return file.exists() ? ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND) : null;
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
            // 部分裝置如三星手機設置 MediaStore.EXTRA_OUTPUT 參數時會有衝突，導致第三方攝影 APP 在
            // 錄製完畢後無法正常返回，需要使用者按下 Back 鍵，也因此 resultCode 永遠為 Cancel ，所
            // 改採用檔案是否存在來做為判斷基準
            if (file.exists())
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

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        fragment.startActivityForResult(intent, REQUEST_CAMERA);
    }

}
