package com.peerbitskuldeep.gmapdemo1.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.peerbitskuldeep.gmapdemo1.R

class CustomInfoWindowAdapter(var context: Context): GoogleMap.InfoWindowAdapter {

    private val contententView = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window, null)

    override fun getInfoWindow(marker: Marker): View? {
        renderViews(marker,contententView)
        return contententView
    }

    override fun getInfoContents(marker: Marker): View? {
        renderViews(marker,contententView)
        return contententView
    }

    private fun renderViews(marker: Marker?, contentView: View)
    {
        val title = marker?.title
        val description = marker?.snippet

        val titleTV = contentView.findViewById<TextView>(R.id.title_textView)
        titleTV.text = title

        val descTV = contentView.findViewById<TextView>(R.id.desc_tv)
        descTV.text = description
    }

}