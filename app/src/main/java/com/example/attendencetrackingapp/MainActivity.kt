package com.example.attendencetrackingapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
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
                var hasBackgroundLocationPermission by remember { mutableStateOf(false) }

                // Permission launcher to request location and background location permissions
                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    hasBackgroundLocationPermission = permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true
                }

                // Function to request location permissions
                fun requestLocationPermissions() {
                    val permissionsToRequest = mutableListOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                    // Background location permission is only needed for Android 11+
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        permissionsToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    }

                    locationPermissionLauncher.launch(permissionsToRequest.toTypedArray())
                }

                LaunchedEffect(firebaseUser) {
                    if (firebaseUser != null) {
                        // Check if location permissions are granted
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            hasLocationPermission = true
                        } else {
                            // Request location permission
                            requestLocationPermissions()
                        }
                    }
                }

                LaunchedEffect(hasLocationPermission, hasBackgroundLocationPermission) {
                    if (firebaseUser != null && hasLocationPermission) {
                        // Start the WorkManager if permissions are granted
                        workManagerViewModel.startWork()
                        // Request battery optimization exemption
                        requestBatteryOptimizationExemption()
                    } else if (!hasLocationPermission) {
                        requestLocationPermissions()
                    }
                }

                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }

    // Request battery optimization exemption
    private fun requestBatteryOptimizationExemption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = packageName
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }
}
