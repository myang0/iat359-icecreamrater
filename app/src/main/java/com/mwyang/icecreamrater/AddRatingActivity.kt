package com.mwyang.icecreamrater

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

    private lateinit var location: LatLng

    private var rating: Int = -1
    private var stars: MutableList<ImageView> = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.addMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        for (i in 1..5) {
            var starId: String = "ratingStar$i"

            var resId: Int = resources.getIdentifier(starId, "id", packageName)

            var star: ImageView = findViewById<ImageView>(resId)
            star.setOnClickListener { onStarClick(i) }

            stars += star
        }

        binding.confirmButton.setOnClickListener { onConfirmButtonClicked() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setOnMapClickListener { position: LatLng -> onMapClick(position) }

        val vancouver = LatLng(49.2577143,-123.1939434)
        map.moveCamera(CameraUpdateFactory.newLatLng(vancouver))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(vancouver, 10.0f))
    }

    private fun onMapClick(position: LatLng) {
        map.clear()

        map.addMarker(MarkerOptions().position(position))
        map.moveCamera(CameraUpdateFactory.newLatLng(position))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10.0f))

        location = position
    }

    private fun onStarClick(index: Int) {
        rating = index + 1

        for (i in 0..4) {
            var star: ImageView = stars[i]

            if (i < index) {
                star.setImageResource(R.drawable.filledstar)
            } else {
                star.setImageResource(R.drawable.unfilledstar)
            }
        }
    }

    private fun onConfirmButtonClicked() {
        var missingFields: MutableList<String> = mutableListOf<String>()

        if (binding.shopNameInput.text.toString() == "") {
            missingFields.add("Shop Name")
        }

        if (!::location.isInitialized) {
            missingFields.add("Shop Location")
        }

        if (rating == -1) {
            missingFields.add("Rating")
        }

        if (missingFields.size > 0) {
            var missingFieldsAsString: String = missingFields.joinToString(separator=", ")

            Toast.makeText(
                this,
                "Rating unable to be created. Please fill in the following fields: $missingFieldsAsString",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(this, "Rating Created!", Toast.LENGTH_SHORT).show()
        }
    }
}