package com.software.xdtextbookgo.service;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by huang on 2016/5/23.
 * 初始化接入账户的pid和key
 */
public class XDtextbookGOApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "OgPNedh1WSsDsQrDRs7cJhez-gzGzoHsz", "AOSIx7gHLoAhN3UyC6N20O4p");
    }
}
