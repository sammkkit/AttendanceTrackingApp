package com.example.attendencetrackingapp.Presentation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(
    navController: NavHostController,
){
    Column {
        Text(text = "Home Screen")
    }
}