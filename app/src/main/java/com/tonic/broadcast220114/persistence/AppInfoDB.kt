package com.tonic.broadcast220114.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppInfo::class], version = 2, exportSchema = true)
abstract class AppInfoDB : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "appInfo.db"
    }

    abstract fun appInfoDao(): AppInfoDao
}