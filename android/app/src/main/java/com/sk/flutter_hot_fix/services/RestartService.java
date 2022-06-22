package com.sk.flutter_hot_fix.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class RestartService extends Service {
//关闭应用后多久重新启动

    private static long stopDelayed = 2000;

    private Handler handler;

    private String packageName;

    public void KillSelfService() {
        handler = new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        packageName = intent.getStringExtra("packageName");

        handler.postDelayed(new Runnable() {
            @Override

            public void run() {
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(packageName);

                startActivity(LaunchIntent);

                RestartService.this.stopSelf();

            }

        }, stopDelayed);

        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

}

