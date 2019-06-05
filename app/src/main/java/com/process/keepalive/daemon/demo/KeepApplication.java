package com.process.keepalive.daemon.demo;

import android.app.Application;

import com.process.keepalive.daemon.DaemonEnv;

public class KeepApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DaemonEnv.initialize(this, DemoService.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
    }

}
