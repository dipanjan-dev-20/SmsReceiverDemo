package com.android.smsdemo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Sms::class), version = 1)
abstract class SmsDatabase : RoomDatabase() {
    abstract fun userDao(): SmsDao
}