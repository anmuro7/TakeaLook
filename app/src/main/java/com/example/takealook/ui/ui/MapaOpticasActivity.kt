package com.example.takealook.ui.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.takealook.R
import com.example.takealook.Viewmodel.MapaOpticasViewModel
import com.example.takealook.Viewmodel.ViewModelFactory
import com.example.takealook.data.model.Optica
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapaOpticasActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation:Location

    companion object{
            private const val  LOCATION_PERMISSION_REQUEST_CODE=1
        }


    private lateinit var mMap: GoogleMap
    private var mapReady=false

    private lateinit var mapViewModel: MapaOpticasViewModel




    fun makeIntent(context: Context?): Intent? {
        return Intent(context, MapaOpticasActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_mapa_opticas)
        this.setTitle("Ópticas colaboradoras")
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mapViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(MapaOpticasViewModel::class.java)
    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        //call a function to enable location of device
        setUpMap()
        //call a function to place the markes of optics
        placeMarker()
        //enable zoom controls
        mMap.uiSettings.isZoomControlsEnabled=true


    }

    //Ask for location permissions and place the camera in the user's position
    private  fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !== PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
            mMap.isMyLocationEnabled=true
            fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location->
                if(location!=null){
                    lastLocation=location
                    val currentLatLong=LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
                }
            }
    }

    //places the markers obtained by the viewmodel on the map
    private fun placeMarker(){
        mapViewModel.loadOptics().observe(this, Observer { optics ->
            if (optics != null) {
                for (optica in optics) {
                    //put geopoint on map
                    val latLng = LatLng(optica.coordenadas!!.latitude, optica.coordenadas!!.longitude)
                    if (optica.verify) {
                        if (optica.coordenadas != null) {
                            val marker: Marker = mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(optica.Nombre)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.smalliconmarker))

                            )
                            //save optica in marker to customwindowadapter
                            marker.tag=optica

                            //custom infowindow
                           mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))
                            //when touch on  custominfowindow
                            mMap.setOnInfoWindowClickListener {
                                var opticMarker=it.tag as Optica
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("${opticMarker.Nombre}")
                                builder.setMessage("¿Quieres ver el stock disponible en ${opticMarker.Nombre}?")
                                builder.setPositiveButton("Si") { dialog, which ->
                                    //go to stock of this optic
                                    val intent = Intent(this, OpticStockActivity::class.java).apply {
                                        putExtra("opticID", opticMarker.opticaId)
                                        putExtra("opticName",opticMarker.Nombre)
                                    }
                                    startActivity(intent)
                                }
                                builder.setNegativeButton("No") { dialog, which -> }
                                builder.show()
                            }

                        }
                    }


                }

            }
            //if there is no data
            else {
                val toast = Toast.makeText(applicationContext,
                        "Actualmente no hay òpticas participantes en Take a Look", Toast.LENGTH_LONG)
                toast.show()
            }
        })
    }




}


