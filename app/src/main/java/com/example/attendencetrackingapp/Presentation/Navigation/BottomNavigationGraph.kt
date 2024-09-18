package com.example.attendencetrackingapp.Presentation.Navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
                    AnimatedScreenTransition {
                        HomeScreen(navController=BottomNavController)
                    }
                }
                composable(Routes.Profile.route) {
                    AnimatedScreenTransition {

                        ProfileScreen(navController = BottomNavController)
                    }
                }
                composable(Routes.Report.route) {
                    AnimatedScreenTransition {

                        ReportScreen(navController = BottomNavController)
                    }
                }
            }

        }

    }
}
@Composable
fun AnimatedScreenTransition(content: @Composable () -> Unit) {
    val transitionState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    AnimatedVisibility(
        visibleState = transitionState,
        enter = slideInHorizontally(initialOffsetX = { 1000 }) ,
        exit = slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
    ) {
        content()
    }
}