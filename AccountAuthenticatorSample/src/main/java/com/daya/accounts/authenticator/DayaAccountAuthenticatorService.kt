package com.daya.accounts.authenticator

import android.app.Service
import android.content.Intent
import android.os.IBinder

class DayaAccountAuthenticatorService : Service() {

    private lateinit var authenticator: DayaAccountAuthenticator

    override fun onCreate() {
        super.onCreate()
        authenticator = DayaAccountAuthenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator.iBinder
    }
}