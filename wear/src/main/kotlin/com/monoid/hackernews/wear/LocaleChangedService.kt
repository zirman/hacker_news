package com.monoid.hackernews.wear

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.monoid.hackernews.common.view.updateAndPushDynamicShortcuts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocaleChangedService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateAndPushDynamicShortcuts(MainActivity::class.java)
        stopSelf()
        return START_NOT_STICKY
    }
}
