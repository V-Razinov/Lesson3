package com.example.lesson3.other.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Router {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var finishActivity: () -> Unit
    private var containerId: Int = -1

    fun init(fragmentManager: FragmentManager, containerId: Int, finishActivity: () -> Unit) {
        this.fragmentManager = fragmentManager
        this.finishActivity = finishActivity
        this.containerId = containerId
    }

    fun navigateTo(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun back() {
        if (fragmentManager.backStackEntryCount == 1) {
            finishActivity()
        } else {
            fragmentManager.popBackStack()
        }
    }
}