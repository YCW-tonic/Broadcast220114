package com.tonic.broadcast220114.persistence

import androidx.room.*
import com.tonic.broadcast220114.persistence.AppInfo

@Dao
interface AppInfoDao {
    @Query("SELECT * FROM " + AppInfo.TABLE_NAME)
    fun getAll(): List<AppInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appInfo: AppInfo)

    @Update
    fun update(appInfo: AppInfo)

    @Query("DELETE FROM " + AppInfo.TABLE_NAME + " WHERE 1")
    fun clearTable()
}