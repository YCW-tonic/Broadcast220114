package com.tonic.broadcast220114.persistence

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AppInfo.TABLE_NAME)
class AppInfo(AppName: String, PackageName: String, Description: String) {
    companion object {
        const val TABLE_NAME = "AppInfoList"
    }

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private var AppName: String

    @ColumnInfo(name = "PackageName")
    private var PackageName: String? = ""

    @ColumnInfo(name = "Description")
    private var Description: String? = ""

    init {
        this.AppName = AppName
        this.PackageName = PackageName
        this.Description = Description
    }

    fun getAppName(): String{
        return AppName
    }

    fun setAppName(AppName: String){
        this.AppName = AppName
    }

    fun getPackageName(): String{
        return PackageName as String
    }

    fun setPackageName(PackageName: String){
        this.PackageName = PackageName
    }

    fun getDescription(): String{
        return Description as String
    }

    fun setDescription(Description: String){
        this.Description = Description
    }
}