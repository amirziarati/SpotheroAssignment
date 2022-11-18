package com.spothero.challenge.data

import com.spothero.challenge.data.local.SpotDao
import com.spothero.challenge.data.model.Spot
import com.spothero.challenge.data.remote.SpotHeroApi
import kotlinx.coroutines.*

class SpotRepo(private val spotHeroApi: SpotHeroApi, private val spotDao: SpotDao) {

    val latestSpots = spotDao.getAllSpotsFlow()

    init {
        getFreshSpots()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getFreshSpots() {
        // fire and forget.
        // the changes will be picked up by latestSpots variable and propagate to viewModel
        GlobalScope.launch(Dispatchers.IO) {
            // call the getSpotsWithDelay() so we can see what happens if API is slow
            // slow api call shouldn't affect app performance since we show DB data immediately
            val spotsFromServer = spotHeroApi.getSpotsWithDelay().toTypedArray()
            spotDao.deleteAll()
            spotDao.insertAll(*spotsFromServer)
        }
    }

    suspend fun getSpotById(spotId: Int): Spot? {
        return spotHeroApi.getSpotById(spotId)
    }
}