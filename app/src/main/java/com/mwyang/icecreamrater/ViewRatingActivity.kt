package com.mwyang.icecreamrater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mwyang.icecreamrater.database.AppDatabase
import com.mwyang.icecreamrater.database.Shop
import com.mwyang.icecreamrater.databinding.ActivityViewRatingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewRatingActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityViewRatingBinding

    var db: AppDatabase? = null

    private var shopId: Int? = 0
    private lateinit var shop: Shop

    private var stars: MutableList<ImageView> = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        shopId = intent.getStringExtra("shopId")?.toInt()

        for (i in 1..5) {
            var starId = "viewStar$i"
            var resId: Int = resources.getIdentifier(starId, "id", packageName)
            var star: ImageView = findViewById(resId)
            stars += star
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.viewMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                deleteShop()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        CoroutineScope(Dispatchers.IO).launch {
            getShop()
        }
    }

    private suspend fun getShop() {
        var shopList: List<Shop> = db!!.shopDao().getById(shopId!!)

        withContext(Dispatchers.Main) {
            shop = shopList[0]

            binding.viewTitle.text = shop.name

            val location = LatLng(shop.latitude.toDouble(), shop.longitude.toDouble())
            mMap.addMarker(MarkerOptions().position(location))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))

            for (i in 0..4) {
                var star: ImageView = stars[i]

                if (i < shop.rating) {
                    star.setImageResource(R.drawable.filledstar)
                } else {
                    star.setImageResource(R.drawable.unfilledstar)
                }
            }

            binding.notes.text = shop.notes
        }
    }

    private suspend fun deleteShop() {
        db!!.shopDao().delete(shop)

        withContext(Dispatchers.Main) {
            finish()
        }
    }
}