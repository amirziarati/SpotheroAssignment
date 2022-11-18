package com.spothero.challenge.data.local

import androidx.room.*
import com.spothero.challenge.data.model.Spot
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotDao {
    @Query("SELECT * FROM spot")
    fun getAllSpots(): List<Spot>

    @Query("SELECT * FROM spot")
    fun getAllSpotsFlow(): Flow<List<Spot>>

    @Query("SELECT * FROM spot WHERE id IN (:spotIds)")
    fun loadAllByIds(spotIds: IntArray): List<Spot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg spots: Spot)

    @Delete
    fun delete(spot: Spot)

    @Query("DELETE FROM spot")
    fun deleteAll()
}