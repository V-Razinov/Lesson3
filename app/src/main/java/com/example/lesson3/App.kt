package com.example.lesson3

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        StrictMode.setVmPolicy(VmPolicy.Builder().penaltyLog().build())
        context = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
}