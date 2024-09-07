package com.example.attendencetrackingapp.Presentation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.attendencetrackingapp.Presentation.Navigation.Routes
import com.example.attendencetrackingapp.ViewModels.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
){
    val authViewModel:AuthViewModel = hiltViewModel()
    val firebaseUser by authViewModel.firebaseUser.collectAsState(null)
    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
    Column {
        Text(text = "Profile Screen")
        Button(onClick = {
            authViewModel.logout()
        }) {
            Text(text = "LOG OUT")
        }
    }
}