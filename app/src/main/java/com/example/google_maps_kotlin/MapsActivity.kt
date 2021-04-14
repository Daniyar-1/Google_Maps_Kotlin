package com.example.google_maps_kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

const val REQUEST_LOCATION_PERMISSION = 1

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latitude = 42.8723523
        val longitude = 74.598986
        val zoomLevel = 18f
        val overlaySize = 10f


        val homeLatLng = LatLng(latitude, longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        mMap.addMarker(MarkerOptions().position(homeLatLng))

        val googleOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.location))
            .position(homeLatLng, overlaySize)
        mMap.addGroundOverlay(googleOverlay)

        setPointClick(mMap)
        enableMyLocation()

        val bishkek = LatLng(42.8723523, 74.598986)
        mMap.addMarker(MarkerOptions().position(bishkek).title("Bishkek"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bishkek, 15f))

        //Adding Only One Marker manually with AutoAnimateCamera
        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            val location = LatLng(latLng!!.latitude, latLng.longitude)

            mMap.addMarker(
                MarkerOptions()
                    .position(location)
            )
        }
    }

    private fun setPointClick(map: GoogleMap) {
        map.setOnPoiClickListener {
            val pointMarker = map.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .title(it.name)
            )
            pointMarker.showInfoWindow()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }
}

//Adding Markers with LatLng
/*   mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
       override fun onMapClick(latLng: LatLng?) {
           val snippet = latLng?.let {
               String.format(
                       Locale.getDefault(),
                       "Lat: %1$.5f, Long: %2$.5f",
                       it.latitude,
                       it.longitude
               )
           }
           mMap.addMarker(
                   latLng?.let {
                       MarkerOptions()
                               .position(it)
                               .title(getString(R.string.title_activity_maps))
                               .snippet(snippet)
                   }
           )
       }
   })*/
