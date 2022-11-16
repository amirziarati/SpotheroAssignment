package com.spothero.challenge.ui.spotlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spothero.challenge.data.model.Spot
import com.spothero.challenge.*
import com.spothero.challenge.R

@Composable
fun SpotItem(spot: Spot, navigateToDetails: (spot: Spot) -> Unit) {

    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .clickable {
            navigateToDetails(spot)
        }
        .padding(10.dp)
    ) {
        val (image, textsContainer) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:/" + spot.facilityPhoto)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.parking_spot_image_description),
            modifier = Modifier
                .height(72.dp)
                .wrapContentWidth()
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )


        Column(
            modifier = Modifier
                .wrapContentHeight()
                .constrainAs(textsContainer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    linkTo(image.end, parent.end, bias = 0f)
                    width = Dimension.fillToConstraints
                },
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = spot.address.street,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
            )
            Text(
                text = spot.distance,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 16.spToDp())
                    .fillMaxWidth()
            )
            Text(
                text = "$${spot.price}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.ptToSp(),
                modifier = Modifier
                    .padding(start = 20.ptToDp())
                    .fillMaxWidth()
            )
        }
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .width(1.dp)
    )
}
