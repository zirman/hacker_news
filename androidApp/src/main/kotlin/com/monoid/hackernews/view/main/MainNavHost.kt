package com.monoid.hackernews.view.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.monoid.hackernews.common.navigation.Route
import com.monoid.hackernews.view.home.HomeScaffold

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
    val mainNavController = rememberNavController()
    NavHost(
        navController = mainNavController,
        startDestination = Route.Home,
        modifier = modifier,
    ) {
        composable<Route.Home> {
            HomeScaffold()
        }
    }
}
