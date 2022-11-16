package com.spothero.challenge.ui.spotdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spothero.challenge.R

@Composable
fun SpotDetailsScreen(viewModel: SpotDetailsViewModel) {
    ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
        viewModel.viewState.collectAsState().value.theSpot?.let { spot ->
            val (image, textsContainer, button) = createRefs()

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:/" + spot.facilityPhoto).crossfade(true).build(),
                contentDescription = stringResource(id = R.string.parking_spot_image_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                    },
            )

            Column(
                modifier = Modifier
                    .constrainAs(textsContainer) {
                        linkTo(image.bottom, button.top, bias = 0f)
                        height = Dimension.fillToConstraints
                    }
                    .padding(16.dp),
            ) {
                Text(text = spot.address.street, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = spot.distance,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = spot.description,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.buttonColor)
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(text = "BOOK FOR $${spot.price}")
            }
        }
    }

}