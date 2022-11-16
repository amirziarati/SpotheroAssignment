package com.spothero.challenge.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.spothero.challenge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotHeroAppBar(title: String, navController: NavController) {
    TopAppBar(
            title = {
                Text(text = title)
            },
            navigationIcon =
            {
                val shouldShowBackIcon = navController.previousBackStackEntry != null
                val ic = if (shouldShowBackIcon) Icons.Filled.ArrowBack else Icons.Default.MoreVert
                IconButton(onClick = {
                    if (shouldShowBackIcon)
                        navController.navigateUp()
                    else {
                        //TO-DO: open navigation drawer
                    }
                }) {
                    Icon(
                            imageVector = ic,
                            tint = colorResource(id = R.color.actionBarTitleColor),
                            contentDescription = "Back"
                    )
                }
            }, colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = colorResource(id = R.color.actionBarColor),
            titleContentColor = colorResource(id = R.color.actionBarTitleColor),
    )
    )


}