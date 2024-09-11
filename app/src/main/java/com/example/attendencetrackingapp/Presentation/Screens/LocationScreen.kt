package com.example.attendencetrackingapp.Presentation.Screens

import android.Manifest
import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.attendencetrackingapp.ViewModels.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(
     // Use hiltViewModel() if using Hilt, otherwise viewModel()
) {
    val viewModel: LocationViewModel = hiltViewModel()
    val context = LocalContext.current

    // Initialize the LocationProvider inside the ViewModel
    LaunchedEffect(Unit) {
        viewModel.initLocationService(context)
    }

    // Request both fine and coarse location permissions
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    var locationText by remember { mutableStateOf("Fetching location...") }

    Column(Modifier.fillMaxSize().padding(50.dp)) {
        // If permissions are not granted, request them
        if (!permissionsState.allPermissionsGranted) {
            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                Text("Grant Location Permission")
            }
        } else {
            // Permissions are granted, fetch location
            Button(onClick = {
                viewModel.getLastLocation { location ->
                    location?.let {
                        locationText = "Latitude: ${it.latitude}, Longitude: ${it.longitude}"
                    } ?: run {
                        locationText = "Unable to get location"
                    }
                }
            }) {
                Text("Get Location")
            }

            // Display the fetched location
            BasicText(text = locationText)
        }
    }
}
