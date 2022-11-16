package com.spothero.challenge.ui.spotlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.spothero.challenge.data.model.Spot

@Composable
fun SpotListScreen(viewModel: SpotListViewModel, navigateToDetails: (spot: Spot) -> Unit) {
    val items = viewModel.viewState.collectAsState().value.latestSpots
    LazyColumn(state = rememberLazyListState()) {
        items(
            items = items,
            key = { item -> item.id },
            itemContent = {
                SpotItem(it, navigateToDetails)
            })
    }
}