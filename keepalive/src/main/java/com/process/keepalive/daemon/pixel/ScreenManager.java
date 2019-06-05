package com.process.keepalive.daemon.pixel;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

/**
 *
 */
public class ScreenManager {
    private static final ScreenManager ourInstance = new ScreenManager();
    private WeakReference<SinglePixelActivity> reference;
    private ScreenReceiver receiver;

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private ScreenManager() {
    }

    public void register(Context context) {
        if (receiver == null) {
            receiver = new ScreenReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            context.registerReceiver(receiver, filter);
        }
    }

    public void unRegister(Context context) {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }

    public void setKeepLiveActivity(SinglePixelActivity activity) {
        reference = new WeakReference<>(activity);
    }

    public void start(Context context) {
        SinglePixelActivity.launch(context);
    }

    public void stop(Context context) {
        if (reference != null && reference.get() != null) {
            reference.get().finish();
        }
    }

}
