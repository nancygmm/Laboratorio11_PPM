package com.example.lab11_ppm

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.example.lab11_ppm.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    val PERMISSION_ID = 42

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(allPermissionsGrantedGPS()){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            leerUbicacionActual()
        } else{
            ActivityCompat.requestPermissions(this,arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID)
        }

    }

    private fun allPermissionsGrantedGPS() = Companion.REQUIRED_PERMISSIONS_GPS.all{
        ContextCompat.checkSelfPermission(baseContext,it)== PackageManager.PERMISSION_GRANTED
    }

    private fun leerUbicacionActual(){
        if (checkPermissions()){
            if(isLocationEnabled()){
                if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                        var location: Location? = task.result
                        if(location==null){
                            requestNewLocationData()
                        } else{
                            binding.tv1.text = "LATITUD = "+location.latitude.toString()
                            binding.tv2.text = "LONGITUD = "+location.longitude.toString()
                        }
                    }

                }
            } else{
                Toast.makeText(this,"Activar Ubicacion",Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                this.finish()
            }
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
        }
    }

    companion object{
        private val REQUIRED_PERMISSIONS_GPS= arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}