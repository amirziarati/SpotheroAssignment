package com.spothero.challenge.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spothero.challenge.data.model.Spot
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.spothero.challenge.data.SpotHeroApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainViewModel = MainViewModel(SpotHeroApi(this))
            MainScreen(mainViewModel.selectedSpot)
        }
    }

    @Composable
    fun MainScreen(selectedSpotState: MutableStateFlow<Spot?>) {
        val selectedSpot = selectedSpotState.collectAsState(initial = null).value
        if (selectedSpot == null) {
            SpotListScreen(mainViewModel.latestSpots, mainViewModel.selectedSpot)
        } else {
            SpotDetailsScreen(selectedSpot)
        }
    }

    @Composable
    fun SpotListScreen(spots: Flow<List<Spot>>, selectedSpotState: MutableStateFlow<Spot?>) {
        val selectedSpot = selectedSpotState.collectAsState(initial = null).value
        if (selectedSpot != null)
            return
        val items = spots.collectAsState(initial = emptyList()).value
        LazyColumn {
            items(
                items = items,
                itemContent = {
                    SpotItem(it)
                })
        }
    }

    @Composable
    fun SpotItem(spot: Spot) {
        Column(modifier = Modifier.clickable {
            val a = 2
            mainViewModel.selectedSpot.value = spot
        }) {
            Row(modifier = Modifier.padding(10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:/" + spot.facilityPhoto)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Parking Spot Image",
                    modifier = Modifier
                        .height(72.dp)
                        .wrapContentWidth()
                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                )

                Column(
                    modifier = Modifier.height(72.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = spot.address.street)
                    Text(text = spot.distance)
                    Text(text = spot.price.toString())
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
        }

    }

    @Composable
    fun SpotDetailsScreen(spot: Spot) {
        Column {
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:/" + spot.facilityPhoto)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Parking Spot Image",
                    modifier = Modifier
                        .height(72.dp)
                        .wrapContentWidth()
                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                )

                Column(
                    modifier = Modifier.height(72.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = spot.address.street)
                    Text(text = spot.distance)
                    Text(text = spot.price.toString())
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callSpotsApi(
        onSuccess: (spots: List<Spot>) -> Unit,
        onFail: (throwable: Throwable) -> Unit
    ) {
        SpotHeroApi(this).getSpotsObservable()
            .subscribeOn(Schedulers.io())
            .map { unsorted ->
                unsorted.sortedBy { it.price }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { spots ->
                    onSuccess(spots)
                },
                onFail
            )
    }

    override fun onBackPressed() {
        if (mainViewModel.selectedSpot.value != null) {
            mainViewModel.selectedSpot.value = null
        } else {
            super.onBackPressed()
        }
    }
}
