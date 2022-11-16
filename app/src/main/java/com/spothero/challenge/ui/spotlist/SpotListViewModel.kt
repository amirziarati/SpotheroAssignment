package com.spothero.challenge.ui.spotlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spothero.challenge.data.SpotHeroApi
import com.spothero.challenge.data.model.Spot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class SpotListViewModel(spotHeroApi: SpotHeroApi) :
    ViewModel() {

    private val viewModelState = MutableStateFlow(SpotListViewState(emptyList<Spot>()))
    var viewState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    init {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(latestSpots = spotHeroApi.getSpots().sortedBy { spot -> spot.price })
            }
        }
    }

    companion object {
        fun provideFactory(
            spotHeroApi: SpotHeroApi,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SpotListViewModel(spotHeroApi) as T
            }
        }
    }
}

data class SpotListViewState(val latestSpots: List<Spot>)