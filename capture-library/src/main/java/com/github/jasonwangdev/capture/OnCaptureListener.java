package com.github.jasonwangdev.capture;

import java.io.File;

/**
 * Created by Jason on 2017/7/2.
 */

public interface OnCaptureListener {

    void onCaptureError(Error error);
    void onCaptured(File file);

}
