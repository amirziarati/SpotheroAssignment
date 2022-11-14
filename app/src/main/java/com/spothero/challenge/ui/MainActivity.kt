package com.spothero.challenge.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spothero.challenge.data.model.Address
import com.spothero.challenge.data.model.Spot
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import com.spothero.challenge.data.SpotHeroApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private val spots = mutableStateListOf<Spot>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotList(spots)
        }
        callSpotsApi(
            onSuccess = { it ->
               spots.addAll(it)
            },
            onFail = { throwable ->
                Log.e("FirstFragment", throwable.message ?: "onError")
            })

    }

    @Composable
    fun SpotList(spots: List<Spot>) {
        LazyColumn {
            items(
                items = spots,
                itemContent = {
                    SpotItem(it)
                })
        }
    }

    @Composable
    fun SpotItem(spot: Spot) {
        Column {
            Row(modifier = Modifier.padding(10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(spot.facilityPhoto)
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

    @Preview
    @Composable
    fun PreviewMessageCard() {
        SpotItem(
            Spot(
                1,
                Address("85 queens wharf rd", "toronto", "ontario"),
                "best place in world",
                "24 mi",
                "file:///android_asset/facility_photo_1.jpg",
                2000
            )
        )
    }

    @Preview
    @Composable
    fun PreviewSpotList() {
        val spot = Spot(
            1,
            Address("85 queens wharf rd", "toronto", "ontario"),
            "best place in world",
            "24 mi",
            "file:///android_asset/facility_photo_1.jpg",
            2000
        )
        SpotList(spots = mutableListOf(spot, spot, spot, spot, spot, spot))
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
}
