package com.spothero.challenge.ui

import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spothero.challenge.data.SpotHeroApi
import com.spothero.challenge.data.model.Spot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class MainViewModel(spotHeroApi: SpotHeroApi, refreshStrategy: RefreshStrategy? = null) :
    ViewModel() {

    var viewState = MutableStateFlow(MainViewState(emptyList<Spot>(), null))

    var latestSpots = flowOf(emptyList<Spot>())
    var selectedSpot = MutableStateFlow<Spot?>(null)

    init {
        viewModelScope.launch {
            latestSpots = flow {
                while (true) {
                    val latestNews = spotHeroApi.getSpots()
                    emit(latestNews) // Emits the result of the request to the flow
                    refreshStrategy?.let {
                        delay(it.timeUnit.toMillis(it.duration)) // Suspends the coroutine for some time
                    }
                }
            }
        }
    }

    fun selectSpot(spot: Spot) {
        viewState.update { it.copy(selectedSpot = spot) }
    }

    fun onBackPressed() {
        if(viewState.value.selectedSpot != null) {
            viewState.update { it.copy(selectedSpot = null) }
        }
    }
}

data class MainViewState(val latestSpots: List<Spot>, val selectedSpot: Spot?)