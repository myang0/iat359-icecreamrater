package com.mwyang.icecreamrater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mwyang.icecreamrater.database.AppDatabase
import com.mwyang.icecreamrater.database.Shop
import com.mwyang.icecreamrater.databinding.ActivityMapsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.addButton.setOnClickListener {
            navigateToAddPage()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        CoroutineScope(Dispatchers.IO).launch {
            getShops()
        }

        map.setOnInfoWindowClickListener(this)
    }

    private fun navigateToAddPage() {
        val intent = Intent(this, AddRatingActivity::class.java)

        startActivity(intent)
    }

    private suspend fun getShops() {
        var shopList: List<Shop> = db!!.shopDao().getAll()

        withContext(Dispatchers.Main) {
            var totalLatitude = 0.0
            var totalLongitude = 0.0

            for (s: Shop in shopList) {
                totalLatitude += s.latitude
                totalLongitude += s.longitude

                val location = LatLng(s.latitude.toDouble(), s.longitude.toDouble())

                var marker = map.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(s.name)
                )

                marker?.tag = s.id
            }

            var averageLatitude: Double = totalLatitude / shopList.size.toDouble()
            var averageLongitude: Double = totalLongitude / shopList.size.toDouble()

            val averageLocation = LatLng(averageLatitude, averageLongitude)

            map.moveCamera(CameraUpdateFactory.newLatLng(averageLocation))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(averageLocation, 9.5f))
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        Log.i("info window", "${marker.tag}")
    }

    override fun onResume() {
        CoroutineScope(Dispatchers.IO).launch {
            getShops()
        }

        super.onResume()
    }
}