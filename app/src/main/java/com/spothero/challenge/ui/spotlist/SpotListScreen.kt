package com.spothero.challenge.ui.spotlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.spothero.challenge.data.model.Spot

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpotListScreen(viewModel: SpotListViewModel, navigateToDetails: (spot: Spot) -> Unit) {
    val viewState = viewModel.viewState.collectAsState().value

    val ptrState = rememberPullRefreshState(viewState.isRefreshing, {viewModel.pullToRefresh()})

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(ptrState)) {
        LazyColumn(state = rememberLazyListState()) {
            items(
                items = viewState.latestSpots,
                key = { item -> item.id },
                itemContent = {
                    SpotItem(it, navigateToDetails)
                })
        }
        PullRefreshIndicator(viewState.isRefreshing, ptrState, Modifier.align(Alignment.TopCenter))
    }

}