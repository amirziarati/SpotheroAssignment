package com.spothero.challenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spothero.challenge.data.model.Spot

@Database(entities = [Spot::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun spotDao(): SpotDao
}