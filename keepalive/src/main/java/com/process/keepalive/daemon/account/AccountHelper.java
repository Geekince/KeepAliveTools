package com.process.keepalive.daemon.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

/**
 * 初始化使用：
 * AccountHelper.addAccount();
 * AccountHelper.autoSync();
 */
public class AccountHelper {
    public static final String ACCOUNT_TYPE = "com.process.keepalive.daemon.account.authenticator";
    public static final String ACCOUNT_PROVIDER = "com.process.keepalive.daemon.account.sync_provider";
    private static Account account;

    public static void addAccount(Context context, String name) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length > 0) {
            return;
        }
        account = new Account(name, ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(account, name, new Bundle());
    }

    public static void autoSync(String name) {
        Account account = new Account(name, ACCOUNT_TYPE);
        ContentResolver.setIsSyncable(account, ACCOUNT_PROVIDER, 1);
        ContentResolver.setSyncAutomatically(account, ACCOUNT_PROVIDER, true);
        ContentResolver.addPeriodicSync(account, ACCOUNT_PROVIDER, new Bundle(), 1);
    }

}