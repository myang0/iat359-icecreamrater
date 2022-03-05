package com.mwyang.icecreamrater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mwyang.icecreamrater.databinding.ActivityAddRatingBinding

class AddRatingActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityAddRatingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.addMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val vancouver = LatLng(49.2577143,-123.1939434)
        map.addMarker(MarkerOptions().position(vancouver).title("Marker in Vancouver"))
        map.moveCamera(CameraUpdateFactory.newLatLng(vancouver))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(vancouver, 12.0f))
    }
}