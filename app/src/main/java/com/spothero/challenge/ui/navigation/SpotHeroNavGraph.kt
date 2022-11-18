package com.spothero.challenge.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.spothero.challenge.AppDiContainer
import com.spothero.challenge.ui.SpotHeroAppBar
import com.spothero.challenge.ui.spotdetails.SpotDetailsScreen
import com.spothero.challenge.ui.spotdetails.SpotDetailsViewModel
import com.spothero.challenge.ui.spotlist.SpotListScreen
import com.spothero.challenge.ui.spotlist.SpotListViewModel
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotHeroNavGraph(
    appDiContainer: AppDiContainer,
    navController: NavHostController = rememberNavController(),
    startDestination: String = SpotHeroDestinations.HOME_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {


        composable(SpotHeroDestinations.HOME_ROUTE) {
            val spotListViewModel: SpotListViewModel = viewModel(
                factory = SpotListViewModel.provideFactory(
                    appDiContainer.spotRepo
                )
            )
            Scaffold(topBar = {
                SpotHeroAppBar(title = "Parking Spots", navController)
            }, content = { paddings ->
                Column(modifier = Modifier.padding(paddings)) {
                    SpotListScreen(spotListViewModel) {
                        navController.navigate(SpotHeroDestinations.DETAIL_ROUTE + "/" + it.id)
                    }
                }
            })
        }



        composable(
            SpotHeroDestinations.DETAIL_ROUTE_WITH_ARGS,
            arguments = listOf(navArgument(SpotHeroDestinations.DETAIL_ROUTE_SPOT_ID_ARG) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val spotId =
                backStackEntry.arguments?.getInt(SpotHeroDestinations.DETAIL_ROUTE_SPOT_ID_ARG)

            spotId?.let {
                val spotDetailsViewModel: SpotDetailsViewModel = viewModel(
                    factory = SpotDetailsViewModel.provideFactory(
                        appDiContainer.spotRepo,
                        spotId
                    )
                )

                val streetAddress = spotDetailsViewModel.viewState.collectAsState()
                    .value.theSpot?.address?.street ?: "Parking Spot details"

                Scaffold(topBar = {
                    SpotHeroAppBar(
                        title = streetAddress,
                        navController
                    )
                }, content = { paddings ->
                    Column(modifier = Modifier.padding(paddings)) {
                        SpotDetailsScreen(spotDetailsViewModel)
                    }
                })
            }

        }


    }
}