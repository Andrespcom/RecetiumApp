package com.example.recetium.ui.navigation

sealed class AppScreens(val route: String){
    object LoginScreen: AppScreens("login_screen")
    object HomeScreen: AppScreens("home_screen")
    object ConfigScreen : AppScreens("config_screen")
    object SearchScreen : AppScreens("search_screen")
    object CardScreen : AppScreens("card_screen")
    object PostRecetasScreen : AppScreens("post_recetas_screen")
    object RegisterScreen : AppScreens("register_screen")
}
