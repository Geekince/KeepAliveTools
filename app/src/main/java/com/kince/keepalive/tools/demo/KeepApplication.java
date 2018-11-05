package com.kince.keepalive.tools.demo;

import android.app.Application;

import com.kince.keepalive.tools.DaemonEnv;

public class KeepApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DaemonEnv.initialize(this, DemoService.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
    }

}
