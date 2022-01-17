package com.tonic.broadcast220114

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tonic.broadcast220114.data.AppInfoData
import com.tonic.broadcast220114.data.Constants
import com.tonic.broadcast220114.databinding.ActivityMainBinding
import com.tonic.broadcast220114.persistence.AppInfo
import com.tonic.broadcast220114.persistence.AppInfoDB
import com.tonic.broadcast220114.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private val mTAG = MainActivity::class.java.name
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var db: AppInfoDB? = null
    private var mContext: Context? = null
    companion object{
        @JvmStatic var appInfoDataList = ArrayList<AppInfoData>()
    }
    private var mReceiver: BroadcastReceiver? = null
    private var isRegister = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = applicationContext

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val migration12 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //database.execSQL("ALTER TABLE '"+History.TABLE_NAME+"' ADD COLUMN 'timeStamp' LONG NOT NULL DEFAULT 0")
            }
        }
        //load db
        db = Room.databaseBuilder(mContext as Context, AppInfoDB::class.java, AppInfoDB.DATABASE_NAME)
            .allowMainThreadQueries()
            .addMigrations(migration12)
            .build()
        //supplierDataList = db!!.supplierDataDao().getAll() as ArrayList<SupplierData>
        //insert
        val appinfo = AppInfo("MagtonicWarehouse", "com.magtonic.magtonicwarehouse.ui","sqlLiteTest")
        db!!.appInfoDao().insert(appinfo)

        //search
        var appInfoList = db!!.appInfoDao().getAll() as ArrayList<AppInfo>
        Log.e(mTAG, "size: ${appInfoList.size}")
        for (appInfoItem in appInfoList) {
            val appInfo = AppInfoData(appInfoItem.getAppName(),appInfoItem.getPackageName(),appInfoItem.getDescription())
            appInfoDataList.add(appInfo)
        }


        //broadcast receiver
        val filter: IntentFilter
        @SuppressLint("CommitPrefEdits")
        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != null) {
                    if (intent.action!!.equals(Constants.ACTION.ACTION_ADD_DATA_ACTION, ignoreCase = true)) {
                        Log.d(mTAG, "ACTION_ADD_DATA_ACTION")

                        val appName = intent.getStringExtra("APP_NAME")
                        val packageName = intent.getStringExtra("PACKAGE_NAME")
                        val description = intent.getStringExtra("DESCRIPTION") as String
                        Log.e(mTAG, "AppName=$appName, PackageName=$packageName, Description=$description")
                        if(appName!=null && packageName != null){
                            //add Data to sqlite
                            val appinfo = AppInfo(appName, packageName, description)
                            db!!.appInfoDao().insert(appinfo)

                            //add Data to list
                            val appInfo = AppInfoData(appName,packageName,description)
                            appInfoDataList.add(appInfo)

                            val successIntent = Intent()
                            successIntent.action = Constants.ACTION.ACTION_ADD_DATA_SUCCESS
                            mContext!!.sendBroadcast(successIntent)
                        }
                    }
                    else if (intent.action!!.equals(Constants.ACTION.ACTION_DELETE_DATA_ACTION, ignoreCase = true)) {
                        Log.d(mTAG, "ACTION_DELETE_DATA_ACTION")

                        val position = intent.getIntExtra("POSITION",-1)
                        Log.e(mTAG, "position=$position")
                        if(position>=0){
                            val appName = appInfoDataList[position].getAppName() as String
                            val ret = db!!.appInfoDao().delete(appName)
                            Log.e(mTAG, "Delete $ret line")
                            if(ret>0){
                                val successIntent = Intent()
                                successIntent.action = Constants.ACTION.ACTION_DELETE_DATA_SUCCESS
                                mContext!!.sendBroadcast(successIntent)
                            }
                            appInfoDataList.removeAt(position)
                        }
                    }
                }
            }
        }

        if (!isRegister) {
            filter = IntentFilter()
            filter.addAction(Constants.ACTION.ACTION_ADD_DATA_ACTION)
            filter.addAction(Constants.ACTION.ACTION_DELETE_DATA_ACTION)
            mContext!!.registerReceiver(mReceiver, filter)
            isRegister = true
            Log.d(mTAG, "registerReceiver mReceiver")
        }
    }
    override fun onDestroy() {
        Log.i(mTAG, "onDestroy")
        if (isRegister && mReceiver != null) {
            try {
                mContext!!.unregisterReceiver(mReceiver)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            isRegister = false
            mReceiver = null
            Log.d(mTAG, "unregisterReceiver mReceiver")
        }

        super.onDestroy()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}