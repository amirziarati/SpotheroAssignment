package com.spothero.challenge.data.remote

import android.content.Context
import com.spothero.challenge.data.model.Spot
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.*
import io.reactivex.Single
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * API used to retrieve SpotHero spots.
 * Uses Ktor with OkHttp
 *
 * @param context Context for MockClient
 * @see HttpClient
 */
class SpotHeroApi(context: Context) {
    companion object {
        private val BASE_URL = "http://api.spothero.com/v1"
        private val PHOTO_LOCATION = "//android_asset/"
    }

    private val client = HttpClient(OkHttp) {
        engine {
            addInterceptor(MockClient(context))
        }
        install(ContentNegotiation) { json() }

    }

    private val scope = context

    /**
     * Retrieve all available spots.
     * Suspended function for use with coroutines.
     *
     * @return List of Spots
     * @see Spot
     */
    suspend fun getSpots(): List<Spot> = client.endpointGet("/spots")
        .body<List<Spot>>().map { spot ->
            spot.apply {
                facilityPhoto = PHOTO_LOCATION + facilityPhoto
            }
        }

    /**
     * Retrieve all available spots.
     * Suspended function for use with coroutines.
     *
     * @return List of Spots
     * @see Spot
     */
    suspend fun getSpotsWithDelay(): List<Spot> = client.endpointGet("/spots")
        .body<List<Spot>>().apply {
            delay(3000) // to simulate slow api call
        }
        .map { spot ->
            spot.apply {
                facilityPhoto = PHOTO_LOCATION + facilityPhoto
            }
        }

    /**
     * Retrieve all available spots.
     *
     * @return Rx Single that emits list of Spots
     * @see Spot
     */
    fun getSpotsObservable() = Single.create<List<Spot>> { emitter ->
        runBlocking {
            launch(Dispatchers.IO) {
                emitter.onSuccess(getSpots())
            }
        }
    }

    /**
     * Retrieve all available spots.
     *
     * @return a Flow that emits list of Spots
     * @see Spot
     */
    fun getSpotsFlow(): Flow<List<Spot>> = flow {
        runBlocking {
            launch(Dispatchers.IO) {
                emit(getSpots())
            }
        }
    }

    /**
     * Retrieve one Spot by ID.
     * Suspended function for use with coroutines.
     *
     * @param spotId ID of spot to retrieve
     * @return Spot with matching ID. Null if no spot found.
     * @see Spot
     */
    suspend fun getSpotById(spotId: Int): Spot? = getSpots().firstOrNull {
        it.id == spotId
    }

    /**
     * Retrieve one Spot by ID.
     *
     * @param spotId ID of spot to retrieve
     * @return Rx Single that emits Spot with matching ID. Emits error if no matching spot is found.
     * @see Spot
     */
    fun getSpotByIdObservable(spotId: Int) = Single.create<Spot> { emitter ->
        runBlocking {
            launch(Dispatchers.IO) {
                val spot = getSpotById(spotId)
                if (spot != null) {
                    emitter.onSuccess(spot)
                    return@launch
                }
                emitter.onError(Throwable("No spot found matching ID $spotId"))
            }
        }
    }

    suspend private fun HttpClient.endpointGet(endpoint: String) = this.get(BASE_URL + endpoint)
}