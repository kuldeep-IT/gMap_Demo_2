package com.peerbitskuldeep.gmapdemo1

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.peerbitskuldeep.gmapdemo1.adapter.CustomInfoWindowAdapter
import com.peerbitskuldeep.gmapdemo1.advance.CameraAndViewPort
import com.peerbitskuldeep.gmapdemo1.databinding.ActivityMapsBinding
import java.lang.Exception
import java.time.LocalDate

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val cameraAndViewPort by lazy {
        CameraAndViewPort()
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //current Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_types_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.hybrid_map_menu -> { mMap.mapType = GoogleMap.MAP_TYPE_HYBRID }
            R.id.satelite_map_menu -> { mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE }
            R.id.terrain_map_menu -> { mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN }
            R.id.normal_map_menu -> { mMap.mapType = GoogleMap.MAP_TYPE_NORMAL }
            R.id.none_map_menu -> { mMap.mapType = GoogleMap.MAP_TYPE_NONE }

//            https://mapstyle.withgoogle.com/
            R.id.retro_menu -> { mapStyleRetro(mMap) }
            R.id.night_menu -> { mapStyleNight(mMap) }
        }

        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val shivEngg = LatLng(21.19858056099887, 72.85978032049988)
        /*var marker = mMap.addMarker(
           MarkerOptions().position(shivEngg)
               .title("Marker in Shiv Engineering")
               .snippet("Kind for more info please visit Webiste...")
               .draggable(true)
              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
              //vector asset to bitmap
//               .icon(fromVectorToBitmap(R.drawable.locat, Color.parseColor("#FF4655")))
            )*/

        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))

//        marker.tag = "Your Marker"

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shivEngg,15f))

//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraAndViewPort.surat))

        mMap.uiSettings.apply {

            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = true

        }
        mMap.setOnMarkerClickListener(this)

        //Marker Drag Listner
        mMap.setOnMarkerDragListener(this)

        mapLongClicked()
        mapSingleClicked()

        //current Location
        setUpMap()

    }

    private fun setUpMap()
    {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),2000)

            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location->

            lastLocation = location
            var currentLatLong = LatLng(location.latitude, location.longitude)

            mMap.addMarker(MarkerOptions().position(currentLatLong).title("You are here!").snippet("Your current Location!").draggable(true))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 17f))

        }
    }

    private fun mapStyleRetro(googleMap: GoogleMap)
    {
        try {

            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.style
                )
            )
            if (!success)
            {
                Toast.makeText(this, "Something went wrong to load map!", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e: Exception)
        {
            d("MAP",e.toString())
        }
    }

    private fun mapStyleNight(googleMap: GoogleMap)
    {
        try
        {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.style_night
                )
            )
            if (!success)
            {
                Toast.makeText(this, "Something went wrong to load map!", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e: Exception)
        {
            d("MAP",e.toString())
        }
    }

    //map clicked
    private fun mapSingleClicked()
    {
        mMap.setOnMapClickListener {

            Toast.makeText(this, "${it.latitude}, ${it.longitude}", Toast.LENGTH_SHORT).show()


        }
    }

    private fun mapLongClicked()
    {
        mMap.setOnMapLongClickListener {

            mMap.addMarker(MarkerOptions().position(it).title("U Clicked!")
                .zIndex(1f))

        }
    }


    //onMarker Clicked
    override fun onMarkerClick(mark: Marker): Boolean {

        mMap.animateCamera(CameraUpdateFactory.zoomTo(18f),2000,object : GoogleMap.CancelableCallback {
            override fun onFinish() {

            }

            override fun onCancel() {

            }
        })
        //show snippet message
        mark.showInfoWindow()

        return true
    }

    //Marker Drag Listner
    override fun onMarkerDragStart(p0: Marker) {
        Log.d("Drag","DragStart")
    }

    override fun onMarkerDrag(p0: Marker) {
        Log.d("Drag","MarkerDrag")
    }

    override fun onMarkerDragEnd(p0: Marker) {
        Log.d("Drag","DragEnd")
        Toast.makeText(this, "${p0.position.latitude} , ${p0.position.longitude} \n ${p0.position.apply { title }}", Toast.LENGTH_SHORT).show()
    }

    //vectorToBitmapMarker
    private fun fromVectorToBitmap(id: Int, color: Int): BitmapDescriptor
    {
        val vectorDrawable: Drawable? = ResourcesCompat.getDrawable(resources, id, null)

        if (vectorDrawable == null)
        {
            d("VTOB","Resource not found!")
            return BitmapDescriptorFactory.defaultMarker()
        }

        val bitmap = Bitmap.createBitmap(

            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888

        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0,0,canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)

    }
}