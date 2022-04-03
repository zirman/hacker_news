package com.monoid.hackernews.ui.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.bottomSheet
import com.monoid.hackernews.MainNavigation
import com.monoid.hackernews.R
import com.monoid.hackernews.Stories
import com.monoid.hackernews.Username
import com.monoid.hackernews.api.ItemId
import com.monoid.hackernews.ui.util.WindowSize
import com.monoid.hackernews.navigation.LoginAction
import com.monoid.hackernews.repo.AskStoryRepo
import com.monoid.hackernews.repo.BestStoryRepo
import com.monoid.hackernews.repo.FavoriteStoryRepo
import com.monoid.hackernews.repo.JobStoryRepo
import com.monoid.hackernews.repo.NewStoryRepo
import com.monoid.hackernews.repo.ShowStoryRepo
import com.monoid.hackernews.repo.TopStoryRepo
import com.monoid.hackernews.repo.UserStoryRepo
import com.monoid.hackernews.ui.home.HomeScreen
import com.monoid.hackernews.ui.login.LoginContent
import com.monoid.hackernews.ui.reply.ReplyContent

@Composable
fun MainNavigationComponent(
    mainState: MainState,
    windowSize: WindowSize,
    mainNavController: NavHostController,
    onLoginError: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    // bug workaround for bottom sheets not updating
    val windowSizeState: State<WindowSize> =
        rememberUpdatedState(windowSize)

    AnimatedNavHost(
        navController = mainNavController,
        startDestination = MainNavigation.Home.route,
        modifier = modifier,
    ) {
        composable(
            route = MainNavigation.Home.route,
            arguments = MainNavigation.Home.arguments,
            enterTransition = MainNavigation.Home.enterTransition,
            exitTransition = MainNavigation.Home.exitTransition,
            popEnterTransition = MainNavigation.Home.popEnterTransition,
            popExitTransition = MainNavigation.Home.popExitTransition,
        ) { navBackStackEntry ->
            val stories: Stories =
                MainNavigation.Home.argsFromRoute(navBackStackEntry)

            HomeScreen(
                mainState = mainState,
                mainNavController = mainNavController,
                windowSize = windowSize,
                title = stringResource(
                    id = when (stories) {
                        Stories.Top ->
                            R.string.top_stories
                        Stories.New ->
                            R.string.new_stories
                        Stories.Best ->
                            R.string.best_stories
                        Stories.Ask ->
                            R.string.ask_hacker_news
                        Stories.Show ->
                            R.string.show_hacker_news
                        Stories.Job ->
                            R.string.jobs
                        Stories.Favorite ->
                            R.string.favorites
                    }
                ),
                orderedItemRepo = remember(stories) {
                    when (stories) {
                        Stories.Top ->
                            TopStoryRepo(
                                httpClient = mainState.httpClient,
                                topStoryDao = mainState.topStoryDao,
                            )
                        Stories.New ->
                            NewStoryRepo(
                                httpClient = mainState.httpClient,
                                newStoryDao = mainState.newStoryDao,
                            )
                        Stories.Best ->
                            BestStoryRepo(
                                httpClient = mainState.httpClient,
                                bestStoryDao = mainState.bestStoryDao,
                            )
                        Stories.Ask ->
                            AskStoryRepo(
                                httpClient = mainState.httpClient,
                                askStoryDao = mainState.askStoryDao,
                            )
                        Stories.Show ->
                            ShowStoryRepo(
                                httpClient = mainState.httpClient,
                                showStoryDao = mainState.showStoryDao,
                            )
                        Stories.Job ->
                            JobStoryRepo(
                                httpClient = mainState.httpClient,
                                jobStoryDao = mainState.jobStoryDao,
                            )
                        Stories.Favorite ->
                            FavoriteStoryRepo(
                                favoriteDao = mainState.favoriteDao,
                            )
                    }
                },
            )
        }

        composable(
            route = MainNavigation.User.route,
            arguments = MainNavigation.User.arguments,
            enterTransition = MainNavigation.User.enterTransition,
            exitTransition = MainNavigation.User.exitTransition,
            popEnterTransition = MainNavigation.User.popEnterTransition,
            popExitTransition = MainNavigation.User.popExitTransition,
        ) { navBackStackEntry ->
            val username: Username =
                MainNavigation.User.argsFromRoute(navBackStackEntry = navBackStackEntry)

            HomeScreen(
                mainState = mainState,
                mainNavController = mainNavController,
                windowSize = windowSize,
                title = username.string,
                orderedItemRepo = remember(mainState, username) {
                    UserStoryRepo(
                        httpClient = mainState.httpClient,
                        userDao = mainState.userDao,
                        itemDao = mainState.itemDao,
                        username = username,
                    )
                },
            )
        }

        bottomSheet(
            route = MainNavigation.Login.route,
            arguments = MainNavigation.Login.arguments,
        ) { navBackStackEntry ->
            val loginAction: LoginAction =
                MainNavigation.Login.argsFromRoute(navBackStackEntry = navBackStackEntry)

            LoginContent(
                loginAction = loginAction,
                httpClient = mainState.httpClient,
                windowSizeState = windowSizeState,
                onLogin = { mainNavController.navigateUp() },
                onLoginError = onLoginError,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(state = rememberScrollState()),
            )
        }

        bottomSheet(
            route = MainNavigation.Reply.route,
            arguments = MainNavigation.Reply.arguments,
        ) { navBackStackEntry ->
            val itemId: ItemId =
                MainNavigation.Reply.argsFromRoute(navBackStackEntry = navBackStackEntry)

            ReplyContent(
                itemId = itemId,
                mainState = mainState,
                windowSizeState = windowSizeState,
                onSuccess = { mainNavController.navigateUp() },
                onError = onLoginError,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(state = rememberScrollState()),
            )
        }
    }
}