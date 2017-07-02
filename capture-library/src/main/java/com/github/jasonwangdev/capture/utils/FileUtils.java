package com.github.jasonwangdev.capture.utils;

import java.io.File;

/**
 * Created by Jason on 2017/7/2.
 */

public class FileUtils {

    public static File createFile(String path, String name) {
        if (null == path || null == name)
            return null;

        File folder = new File(path);

        if (!folder.exists())
        {
            if (!folder.mkdirs())
                return null;
        }

        File file = new File(folder.getPath() + File.separator + name);

        return file;
    }

}
