package com.github.jasonwangdev.capture.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.jasonwangdev.capture.Error;
import com.github.jasonwangdev.capture.ImageCapture;
import com.github.jasonwangdev.capture.OnCaptureListener;
import com.github.jasonwangdev.capture.VideoCapture;

import java.io.File;

/**
 * Created by Jason on 2017/7/2.
 */

public class DemoFragment extends Fragment implements OnCaptureListener, View.OnClickListener {

    ImageCapture imageCapture;
    VideoCapture videoCapture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageCapture = new ImageCapture();
        imageCapture.setOnCaptureListener(this);

        videoCapture = new VideoCapture();
        videoCapture.setOnCaptureListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo, container, false);

        view.findViewById(R.id.btn_image).setOnClickListener(this);
        view.findViewById(R.id.btn_video).setOnClickListener(this);

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        imageCapture.onRequestPermissionsResult(requestCode, permissions, grantResults);
        videoCapture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageCapture.onActivityResult(requestCode, resultCode, data);
        videoCapture.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCaptureError(Error error) {
        Toast.makeText(this.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCaptured(File file) {
        String str = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        if (str.contains("jpg"))
            ((ImageView) getView().findViewById(R.id.iv)).setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        else
        {
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
            ((ImageView) getView().findViewById(R.id.iv)).setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_image:
                imageCapture.capture(this);
                break;

            case R.id.btn_video:
                videoCapture.capture(this);
                break;
        }
    }

}
