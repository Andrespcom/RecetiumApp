package com.example.recetium.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.recetium.ui.screens.card.CardScreen
import com.example.recetium.ui.screens.config.ConfigScreen
import com.example.recetium.ui.screens.config.ConfigViewModel
import com.example.recetium.ui.screens.home.HomeScreen
import com.example.recetium.ui.screens.home.HomeViewModel
import com.example.recetium.ui.screens.login.LoginScreen
import com.example.recetium.ui.screens.login.LoginViewModel
import com.example.recetium.ui.screens.post.PostRecetasScreen
import com.example.recetium.ui.screens.post.PostRecetasViewModel
import com.example.recetium.ui.screens.search.SearchScreen
import com.example.recetium.ui.screens.search.SearchViewModel

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(route = AppScreens.LoginScreen.route) {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(viewModel = loginViewModel, navController = navController, homeViewModel = homeViewModel)
        }
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(route = AppScreens.ConfigScreen.route) {
            val configViewModel: ConfigViewModel = viewModel()
            ConfigScreen(navController = navController, viewModel = configViewModel)
        }
        composable(route = AppScreens.SearchScreen.route) {
            val searchViewModel: SearchViewModel = viewModel()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }
        composable(route = AppScreens.CardScreen.route) {
            CardScreen(navController = navController)
        }
        composable(AppScreens.PostRecetasScreen.route) {
            val postRecetasViewModel: PostRecetasViewModel = viewModel()
            PostRecetasScreen(
                viewModel = postRecetasViewModel,
                homeViewModel = homeViewModel,
                onPostSuccess = { navController.popBackStack() }
            )
        }
    }
}