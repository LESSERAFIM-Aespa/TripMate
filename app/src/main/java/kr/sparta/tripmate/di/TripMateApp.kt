package kr.sparta.tripmate.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TripMateApp : Application(){
    companion object {
        @Volatile
        private lateinit var app: TripMateApp

        @JvmStatic
        fun getApp() : TripMateApp {
            return app
        }
    }

    override fun onCreate() {
        app = this
        super.onCreate()
    }
}