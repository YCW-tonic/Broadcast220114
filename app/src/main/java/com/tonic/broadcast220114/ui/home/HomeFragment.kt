package com.tonic.broadcast220114.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tonic.broadcast220114.MainActivity.Companion.appInfoDataList
import com.tonic.broadcast220114.R
import com.tonic.broadcast220114.data.AppInfoData
import com.tonic.broadcast220114.data.AppInfoDataAdapter
import com.tonic.broadcast220114.data.Constants
import com.tonic.broadcast220114.databinding.FragmentHomeBinding
import com.tonic.broadcast220114.persistence.AppInfo

class HomeFragment : Fragment() {
    private val mTAG = HomeFragment::class.java.name
    private var homeFragmentContext: Context? = null
    private var mReceiver: BroadcastReceiver? = null
    private var isRegister = false
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var listView: ListView?= null
    private var appInfoDataAdapter: AppInfoDataAdapter? = null
    //var appInfoDataList = ArrayList<AppInfoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(mTAG, "onCreate")

        homeFragmentContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(mTAG, "onCreateView")
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val data1 = AppInfoData("Broadcast220114", "com.tonic.broadcast220114.ui.home","sqlLiteTest")
//        val data2 = AppInfoData("MagtonicWarehouse", "com.magtonic.magtonicwarehouse","warehouse")
//        appInfoDataList.add(data1)
//        appInfoDataList.add(data2)

        val listView1 = _binding!!.listView1
        appInfoDataAdapter = AppInfoDataAdapter(homeFragmentContext as Context, R.layout.fragment_home_item, appInfoDataList)
        listView1.adapter = appInfoDataAdapter

        listView1.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            Log.e(mTAG, "position=$position")
            showDeleteDataDialog(position)
            true
        }

        val btnAdd = _binding!!.btnShowDialog
        btnAdd.setOnClickListener {
            showAddDataDialog()
        }

        //broadcast receiver
        val filter: IntentFilter
        @SuppressLint("CommitPrefEdits")
        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != null) {
                    if (intent.action!!.equals(Constants.ACTION.ACTION_ADD_DATA_SUCCESS, ignoreCase = true)) {
                        Log.d(mTAG, "ACTION_ADD_DATA_SUCCESS")
                        appInfoDataAdapter!!.notifyDataSetChanged()
                    }
                    else if (intent.action!!.equals(Constants.ACTION.ACTION_DELETE_DATA_SUCCESS, ignoreCase = true)) {
                        Log.d(mTAG, "ACTION_DELETE_DATA_SUCCESS")
                        appInfoDataAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        }

        if (!isRegister) {
            filter = IntentFilter()
            filter.addAction(Constants.ACTION.ACTION_ADD_DATA_SUCCESS)
            filter.addAction(Constants.ACTION.ACTION_DELETE_DATA_SUCCESS)
            homeFragmentContext!!.registerReceiver(mReceiver, filter)
            isRegister = true
            Log.d(mTAG, "registerReceiver mReceiver")
        }

        return root
    }

    override fun onDestroyView() {
        if (isRegister && mReceiver != null) {
            try {
                homeFragmentContext!!.unregisterReceiver(mReceiver)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            isRegister = false
            mReceiver = null
            Log.d(mTAG, "unregisterReceiver mReceiver")
        }
        super.onDestroyView()
        _binding = null
    }
    private fun showDeleteDataDialog(position:Int){
        Log.e(mTAG, "=== showDeleteDialog start ===")

        val promptView = View.inflate(homeFragmentContext, R.layout.add_dialog, null)

        val alertDialogBuilder = AlertDialog.Builder(homeFragmentContext).create()
        alertDialogBuilder.setView(promptView)

        //final EditText editFileName = (EditText) promptView.findViewById(R.id.editFileName);

        val textViewSupplierDialog1 = promptView.findViewById<TextView>(R.id.textViewSupplierDialog1)
        val textViewSupplierDialog2 = promptView.findViewById<TextView>(R.id.textViewSupplierDialog2)
        val editTextAppName = promptView.findViewById<EditText>(R.id.editTextAppName)
        val editTextPackageName = promptView.findViewById<EditText>(R.id.editTextPackageName)
        val editTextDescription = promptView.findViewById<EditText>(R.id.editTextDescription)
        val textViewAppName = promptView.findViewById<TextView>(R.id.textViewAppName)

        textViewSupplierDialog1.text = "Delete"
        textViewSupplierDialog2.text = "Do you want to delete this data?"
        editTextAppName.visibility=View.GONE
        editTextPackageName.visibility=View.GONE
        editTextDescription.visibility=View.GONE
        textViewAppName.visibility=View.VISIBLE
        textViewAppName.text = appInfoDataList[position].getAppName()

        //editTextSupplierName.inputType = InputType.TYPE_CLASS_TEXT

        val btnCancel = promptView.findViewById<Button>(R.id.btnSupplierDialogCancel)
        val btnConfirm = promptView.findViewById<Button>(R.id.btnSupplierDialogConfirm)

        alertDialogBuilder.setCancelable(false)
        btnCancel!!.setOnClickListener {
            alertDialogBuilder.dismiss()
        }
        btnConfirm!!.setOnClickListener {
            val deleteIntent = Intent()
            deleteIntent.action = Constants.ACTION.ACTION_DELETE_DATA_ACTION
            deleteIntent.putExtra("POSITION", position)
            homeFragmentContext!!.sendBroadcast(deleteIntent)


            alertDialogBuilder.dismiss()
        }
        alertDialogBuilder.show()
    }
    private fun showAddDataDialog() {

        Log.e(mTAG, "=== showAddDialog start ===")



        // get prompts.xml view
        /*LayoutInflater layoutInflater = LayoutInflater.from(Nfc_read_app.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);*/
        val promptView = View.inflate(homeFragmentContext, R.layout.add_dialog, null)

        val alertDialogBuilder = AlertDialog.Builder(homeFragmentContext).create()
        alertDialogBuilder.setView(promptView)

        //final EditText editFileName = (EditText) promptView.findViewById(R.id.editFileName);

        val editTextAppName = promptView.findViewById<EditText>(R.id.editTextAppName)
        val editTextPackageName = promptView.findViewById<EditText>(R.id.editTextPackageName)
        val editTextDescription = promptView.findViewById<EditText>(R.id.editTextDescription)

        //editTextSupplierName.inputType = InputType.TYPE_CLASS_TEXT

        val btnCancel = promptView.findViewById<Button>(R.id.btnSupplierDialogCancel)
        val btnConfirm = promptView.findViewById<Button>(R.id.btnSupplierDialogConfirm)

        alertDialogBuilder.setCancelable(false)
        btnCancel!!.setOnClickListener {
            alertDialogBuilder.dismiss()
        }
        btnConfirm!!.setOnClickListener {
            //Log.e(mTAG, "AppName=${editTextAppName.text}, PackageName=${editTextPackageName.text}, Description=${editTextDescription.text}")
            val addIntent = Intent()
            addIntent.action = Constants.ACTION.ACTION_ADD_DATA_ACTION
            addIntent.putExtra("APP_NAME", editTextAppName.text.toString())
            addIntent.putExtra("PACKAGE_NAME", editTextPackageName.text.toString())
            addIntent.putExtra("DESCRIPTION", editTextDescription.text.toString())
            homeFragmentContext!!.sendBroadcast(addIntent)

            alertDialogBuilder.dismiss()
        }
        alertDialogBuilder.show()
    }

}