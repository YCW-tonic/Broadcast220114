package com.tonic.broadcast220114.data

class AppInfoData(appName: String, packageName: String, description: String) {
    private var appName: String? = appName
    private var packageName: String? = packageName
    private var description: String? = description

    fun getAppName(): String? {
        return appName
    }

    fun setAppName(appName: String) {
        this.appName = appName
    }

    fun getPackageName(): String? {
        return packageName
    }

    fun setPackageName(packageName: String) {
        this.packageName = packageName
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }
}