package com.tonic.broadcast220114.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tonic.broadcast220114.MainActivity.Companion.appInfoDataList
import com.tonic.broadcast220114.R
import com.tonic.broadcast220114.data.AppInfoData
import com.tonic.broadcast220114.data.AppInfoDataAdapter
import com.tonic.broadcast220114.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val mTAG = HomeFragment::class.java.name
    private var homeFragmentContext: Context? = null

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

        val btnAdd = _binding!!.btnShowDialog
        btnAdd.setOnClickListener {
            showAddSupplierDialog()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddSupplierDialog() {

        Log.e(mTAG, "=== showAddSupplierDialog start ===")



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
            Log.e(mTAG, "AppName=${editTextAppName.text}, PackageName=${editTextPackageName.text}, Description=${editTextDescription.text}")

        }
        alertDialogBuilder.show()
    }

}