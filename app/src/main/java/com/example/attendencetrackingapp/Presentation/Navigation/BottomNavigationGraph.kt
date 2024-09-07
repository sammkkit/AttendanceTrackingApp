package com.example.attendencetrackingapp.Presentation.Navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.attendencetrackingapp.Presentation.Components.BottomNavigationBar
import com.example.attendencetrackingapp.Presentation.Screens.HomeScreen
import com.example.attendencetrackingapp.Presentation.Screens.ProfileScreen
import com.example.attendencetrackingapp.Presentation.Screens.ReportScreen

@Composable
fun BottomNavigationScreen(
    navController: NavHostController
){
    val BottomNavController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(BottomNavController) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            NavHost(
                navController = BottomNavController,
                startDestination = Routes.Home.route
            ) {
                composable(Routes.Home.route) {
                    HomeScreen(navController=navController)
                }
                composable(Routes.Profile.route) {
                    ProfileScreen(navController = navController)
                }
                composable(Routes.Report.route) {
                    ReportScreen(navController = navController)
                }
            }

        }

    }
}