package com.spothero.challenge.ui.spotdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spothero.challenge.data.SpotRepoImpl
import com.spothero.challenge.data.model.Spot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpotDetailsViewModel(spotRepo: SpotRepoImpl, spotId: Int) :
    ViewModel() {

    private val viewModelState = MutableStateFlow(SpotDetailsViewModelState(null))
    var viewState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    init {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(theSpot = spotRepo.getSpotById(spotId))
            }
        }
    }

    companion object {
        fun provideFactory(
            spotRepo: SpotRepoImpl,
            spotId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SpotDetailsViewModel(spotRepo, spotId) as T
            }
        }
    }
}

data class SpotDetailsViewModelState(val theSpot: Spot?)