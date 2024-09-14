package com.example.attendencetrackingapp.LocationService

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import javax.inject.Inject


class LocationProvider @Inject constructor(
    context: Context
) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    // SuppressLint annotation is used to bypass the missing permission warning
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Task<Location> {
        return fusedLocationClient.lastLocation
    }
}