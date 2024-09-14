package com.example.attendencetrackingapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.attendencetrackingapp.Presentation.Navigation.NavGraph
import com.example.attendencetrackingapp.ViewModels.AuthViewModel
import com.example.attendencetrackingapp.ViewModels.WorkManagerViewModel
import com.example.attendencetrackingapp.ui.theme.AttendenceTrackingAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val workManagerViewModel: WorkManagerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AttendenceTrackingAppTheme {
                val firebaseUser = authViewModel.firebaseUser.collectAsState().value
                val context = LocalContext.current
                var hasLocationPermission by remember { mutableStateOf(false) }

                // Permission launcher to request location permission
                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    hasLocationPermission = isGranted
                }

                LaunchedEffect(firebaseUser) {
                    if (firebaseUser != null) {
                        // Check if location permission is granted
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            hasLocationPermission = true
                        } else {
                            // Request location permission
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    }
                }
                LaunchedEffect(hasLocationPermission) {
                    if (firebaseUser != null && hasLocationPermission) {
                        workManagerViewModel.startWork()
                    }
                }
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
