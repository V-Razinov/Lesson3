package com.example.lesson3.other.utils

import android.content.Context
import android.net.ConnectivityManager

val Context.isNetworkAvailable: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
        ?.activeNetwork != null