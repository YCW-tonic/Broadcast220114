package com.tonic.broadcast220114.data

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tonic.broadcast220114.R
import java.util.*

class AppInfoDataAdapter(context: Context?, resource: Int, objects: ArrayList<AppInfoData>) :
    ArrayAdapter<AppInfoData>(context as Context, resource, objects){
    private val mTAG = AppInfoDataAdapter::class.java.name
    private val layoutResourceId: Int = resource

    private var inflater : LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val items: ArrayList<AppInfoData> = objects
    private val mContext = context

    override fun getCount(): Int {
        return items.size
    }
    override fun getItem(position: Int): AppInfoData {
        return items[position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null || convertView.tag == null) {
            view = inflater.inflate(layoutResourceId, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }


        val appInfoData = items[position]
        holder.itemHeader.text = appInfoData.getAppName()
        holder.itemContent.text = appInfoData.getPackageName()


        return view
    }
    class ViewHolder (view: View) {
        var itemHeader: TextView = view.findViewById(R.id.appNameItemHeader)
        var itemContent: TextView = view.findViewById(R.id.packageNameItemContent)
    }
}