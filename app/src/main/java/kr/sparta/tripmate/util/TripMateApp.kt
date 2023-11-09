package kr.sparta.tripmate.util

import android.app.Application

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