package com.spothero.challenge

import android.content.Context
import androidx.room.Room
import com.spothero.challenge.data.remote.SpotHeroApi
import com.spothero.challenge.data.SpotRepo
import com.spothero.challenge.data.local.AppDatabase


/**
 * app level di.
 */
interface AppDiContainer {
    val spotRepo: SpotRepo
}

class AppDiContainerImpl(private val applicationContext: Context) : AppDiContainer {

    val spotHeroApi: SpotHeroApi by lazy {
        SpotHeroApi(applicationContext)
    }

    override val spotRepo: SpotRepo by lazy {
        SpotRepo(spotHeroApi, db.spotDao())
    }

    val db: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }


}
