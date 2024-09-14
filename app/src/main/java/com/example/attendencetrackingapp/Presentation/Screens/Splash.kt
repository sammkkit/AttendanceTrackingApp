package com.example.attendencetrackingapp.Presentation.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.attendencetrackingapp.Presentation.Navigation.Routes
import com.example.attendencetrackingapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription =null
        )
    }
    LaunchedEffect(true) {
        delay(2000)

        if (FirebaseAuth.getInstance().currentUser != null) {
            navController.navigate(Routes.MapScreen.route) {
                Log.d("start","hi")
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        } else {
            navController.navigate(Routes.Login.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }

    }
}