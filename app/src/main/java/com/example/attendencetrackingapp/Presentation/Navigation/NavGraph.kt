package com.example.attendencetrackingapp.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendencetrackingapp.Presentation.Screens.LocationScreen
import com.example.attendencetrackingapp.Presentation.Screens.LoginScreen
import com.example.attendencetrackingapp.Presentation.Screens.SignUpScreen
import com.example.attendencetrackingapp.Presentation.Screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) {
            Splash(navController)
        }
        composable(Routes.BottomNav.route) {
            BottomNavigationScreen(navController = navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController)
        }
        composable(Routes.Signup.route) {
            SignUpScreen(navController)
        }
        composable(Routes.MapScreen.route){
            LocationScreen()
        }
    }
}