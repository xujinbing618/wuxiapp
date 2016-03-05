package com.magus.enviroment.ep.util;

import java.io.File;

/**
 * Author huarizhong
 * Date 2015/1/28
 * Time 11:00
 */
public abstract class MyDownloadHandler {

    public abstract void onSuccess(File file);

    public abstract void onFailed();
}
