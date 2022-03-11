package com.mwyang.icecreamrater.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shop(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "longitude") val longitude: Float,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "notes") val notes: String?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
