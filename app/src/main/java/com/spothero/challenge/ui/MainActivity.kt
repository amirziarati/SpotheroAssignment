package com.spothero.challenge.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.spothero.challenge.SpotHeroApplication
import com.spothero.challenge.ui.navigation.SpotHeroDestinations
import com.spothero.challenge.ui.navigation.SpotHeroNavGraph


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diContainer = (application as SpotHeroApplication).container

        setContent {
            val navController = rememberNavController()
            SpotHeroNavGraph(diContainer, navController, SpotHeroDestinations.HOME_ROUTE)
        }
    }
}
