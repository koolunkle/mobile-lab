package com.udemy.a7minutesworkout

import android.app.Application

class WorkoutApp : Application() {
    val database by lazy {
        HistoryDatabase.getInstance(this)
    }
}