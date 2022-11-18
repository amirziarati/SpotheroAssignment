package com.spothero.challenge.data

import com.spothero.challenge.data.model.Spot
import kotlinx.coroutines.flow.Flow

interface SpotRepo {
    val latestSpots: Flow<List<Spot>>
    fun getFreshSpots()
}