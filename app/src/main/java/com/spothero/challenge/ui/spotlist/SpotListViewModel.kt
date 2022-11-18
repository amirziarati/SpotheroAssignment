package com.spothero.challenge.ui.spotlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spothero.challenge.data.SpotRepo
import com.spothero.challenge.data.model.Spot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class SpotListViewModel(private val spotRepo: SpotRepo) :
    ViewModel() {

    private val viewModelState = MutableStateFlow(SpotListViewState(emptyList<Spot>()))
    var viewState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    init {
        viewModelScope.launch {
            spotRepo.latestSpots.collect { newSpots ->
                viewModelState.update {
                    it.copy(
                        latestSpots = newSpots.sortedBy { spot -> spot.price },
                        isRefreshing = false
                    )
                }
            }
        }
    }

    fun pullToRefresh() {
        viewModelState.update { it.copy(isRefreshing = true) }
        spotRepo.getFreshSpots()
    }

    companion object {
        fun provideFactory(
            spotRepo: SpotRepo
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SpotListViewModel(spotRepo) as T
            }
        }
    }
}

data class SpotListViewState(
    val latestSpots: List<Spot>,
    val isRefreshing: Boolean = false
)