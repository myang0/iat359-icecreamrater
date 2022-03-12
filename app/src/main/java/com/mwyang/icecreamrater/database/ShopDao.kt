package com.mwyang.icecreamrater.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mwyang.icecreamrater.database.Shop

@Dao
interface ShopDao {
    @Query("SELECT * FROM shop")
    fun getAll(): List<Shop>

    @Query("SELECT * FROM shop WHERE id = :id")
    fun getById(id: Int): List<Shop>

    @Query("SELECT * FROM shop WHERE id in (:ids)")
    fun loadAllByIds(ids: IntArray): List<Shop>

    @Insert
    fun insert(shop: Shop)

    @Delete
    fun delete(shop: Shop)
}