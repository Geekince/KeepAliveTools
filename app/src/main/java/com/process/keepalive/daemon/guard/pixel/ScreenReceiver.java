package com.process.keepalive.daemon.guard.pixel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.process.keepalive.daemon.guard.LogUtils;

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            LogUtils.i("Keep","ScreenReceiver 开屏");
            ScreenManager.getInstance().stop(context);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            // 屏幕熄灭，开启 SinglePixelActivity
            LogUtils.i("Keep","ScreenReceiver 锁屏");
            ScreenManager.getInstance().start(context);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            LogUtils.i("Keep","ScreenReceiver 解锁");
            ScreenManager.getInstance().stop(context);
        }
    }

}
