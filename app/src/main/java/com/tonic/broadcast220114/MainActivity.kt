package com.tonic.broadcast220114

import android.content.Context
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