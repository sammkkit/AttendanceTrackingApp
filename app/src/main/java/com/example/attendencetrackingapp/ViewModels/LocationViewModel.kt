package com.example.attendencetrackingapp.ViewModels

import android.content.Context
import android.location.Location
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendencetrackingapp.LocationService.LocationProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor():ViewModel() {

    private var locationProvider: LocationProvider? = null

    fun initLocationService(context: Context) {
        locationProvider = LocationProvider(context)
    }

    fun getLastLocation(onLocationResult: (Location?) -> Unit) {
        viewModelScope.launch{
            locationProvider?.getLastLocation()?.addOnSuccessListener { location ->
                onLocationResult(location)
            }?.addOnFailureListener {
                onLocationResult(null)
            }
        }
    }
}