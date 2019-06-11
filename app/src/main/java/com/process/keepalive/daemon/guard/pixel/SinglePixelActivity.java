package com.process.keepalive.daemon.guard.pixel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.process.keepalive.daemon.guard.DaemonEnv;
import com.process.keepalive.daemon.guard.LogUtils;
import com.process.keepalive.daemon.guard.WatchDogService;

public class SinglePixelActivity extends Activity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SinglePixelActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        // 设置左上角
        window.setGravity(Gravity.TOP | Gravity.START);
        WindowManager.LayoutParams params = window.getAttributes();
        // 坐标
        params.x = 0;
        params.y = 0;
        // 设置 1 像素
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);

        ScreenManager.getInstance().setKeepLiveActivity(this);
    }

    @Override
    protected void onDestroy() {
        DaemonEnv.startServiceMayBind(WatchDogService.class);
        LogUtils.i("Keep","SinglePixelActivity onDestroy");
        super.onDestroy();
    }

}
