package com.tunebrains.planner5dtest

import android.app.Application
import org.koin.core.context.startKoin

class PlannerApp:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {

        }
    }
}