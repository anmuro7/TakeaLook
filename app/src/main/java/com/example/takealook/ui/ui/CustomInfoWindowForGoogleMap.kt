package com.example.takealook.ui.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.takealook.R
import com.example.takealook.data.model.Optica
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.infowindow_adapter, null)
    private lateinit var opticMarker:Optica


    private fun rendowWindowText(marker: Marker, view: View){

        opticMarker=marker.tag as Optica

        //find views
        val opticName:TextView=view.findViewById(R.id.opticName_infowindow)
        val opticaddress:TextView=view.findViewById(R.id.opticAddress_infowindow)
        val opticphone:TextView=view.findViewById(R.id.opticphone_infowindow)
        val opticemail:TextView=view.findViewById(R.id.opticemail_infowindow)
        val opticweb:TextView=view.findViewById(R.id.opticweb_infowindow)

        //put data on views
        opticName.text=opticMarker.Nombre
        opticaddress.text="Dirección: ${opticMarker.Direccion}, ${opticMarker.CP}, ${opticMarker.Localidad}"
        opticphone.text="Teléfono: ${opticMarker.Telefono}"
        opticemail.text="Email: ${opticMarker.Email}"
        opticweb.text="Web: ${opticMarker.Web}"

    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}
