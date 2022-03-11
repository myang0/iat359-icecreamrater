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
import com.google.android.gms.maps.model.MarkerOptions
import com.mwyang.icecreamrater.database.AppDatabase
import com.mwyang.icecreamrater.database.Shop
import com.mwyang.icecreamrater.databinding.ActivityMapsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
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

//        val vancouver = LatLng(49.2577143,-123.1939434)
//        map.addMarker(MarkerOptions().position(vancouver).title("Marker in Vancouver"))
//        map.moveCamera(CameraUpdateFactory.newLatLng(vancouver))
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(vancouver, 12.0f))

        CoroutineScope(Dispatchers.IO).launch {
            getShops()
        }
    }

    private fun navigateToAddPage() {
        val intent = Intent(this, AddRatingActivity::class.java)

        startActivity(intent)
    }

    private suspend fun getShops() {
        var shopList: List<Shop> = db!!.shopDao().getAll()

        withContext(Dispatchers.Main) {
            for (s: Shop in shopList) {
                Log.i("map", s.name)
            }
        }
    }
}