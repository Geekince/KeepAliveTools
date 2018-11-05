package com.kince.keepalive.tools.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kince.keepalive.tools.DaemonEnv;
import com.kince.keepalive.tools.IntentWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                DemoService.sShouldStopService = false;
                DaemonEnv.startServiceMayBind(DemoService.class);
                break;
            case R.id.btn_white:
                IntentWrapper.whiteListMatters(this, "轨迹跟踪服务的持续运行");
                break;
            case R.id.btn_stop:
                DemoService.stopService();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }

}
