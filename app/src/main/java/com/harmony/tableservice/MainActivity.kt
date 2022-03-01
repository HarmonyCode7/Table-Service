package com.harmony.tableservice

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import android.Manifest
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions


const val LOGIN_MESSAGE = "com.harmony.tableservice.MESSAGE"

class MainActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var map: GoogleMap
    private val TAG = MainActivity::class.java.simpleName

    companion object {
        private val MY_PERMISSION_FINE_LOCATION = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun sendMessage(view: View) {
        val message = "Welcome User"
        val intent = Intent(this, LoginActivity::class.java).apply{
            putExtra(LOGIN_MESSAGE, message)
        }
        startActivity(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map = googleMap
        try {
            val success: Boolean = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json)
            );
            if(!success) {
                Log.e(TAG, "")
            }
        }catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
        val trentBridge = LatLng(52.9387031,-1.1357277)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(trentBridge, 16.94F))
        map.uiSettings.isZoomControlsEnabled = true

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true;
        }
        else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_FINE_LOCATION)
            }
        }
        //map.setOnMarkerClickListener(this)
    }

    //override fun onMarkerClick(p0: Marker?): Boolean = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            MY_PERMISSION_FINE_LOCATION -> if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map.isMyLocationEnabled = true
                }
            }
            else {
                Toast.makeText(applicationContext, "This app requires access to location", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}