package com.words.ui.application

import android.app.Application
import android.content.Context


class CustomApplication: Application() {
    companion object {
        lateinit var instance: CustomApplication
        val context: Context get() = instance
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}