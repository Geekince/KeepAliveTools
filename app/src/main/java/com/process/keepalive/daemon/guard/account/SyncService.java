package com.process.keepalive.daemon.guard.account;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.process.keepalive.daemon.guard.DaemonEnv;
import com.process.keepalive.daemon.guard.LogUtils;

/**
 *
 */
public class SyncService extends Service {
    private static final String TAG = "SyncService";

    private SyncAdapter syncAdapter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        syncAdapter = new SyncAdapter(this, true);
    }

    class SyncAdapter extends AbstractThreadedSyncAdapter {
        public SyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            // 拉活逻辑
            LogUtils.i(TAG, "onPerformSync");
            DaemonEnv.startServiceMayBind(DaemonEnv.sServiceClass);
        }
    }

}