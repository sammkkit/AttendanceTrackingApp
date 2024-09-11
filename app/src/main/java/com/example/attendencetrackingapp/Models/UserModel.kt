package com.example.attendencetrackingapp.Models

data class UserModel(
    val email: String="",
    val password: String="",
    val Role: String=""
)
data class OfficeLocation(
    val lat: Double=0.0,
    val long: Double=0.0
)
val Mock_office = OfficeLocation(
    lat = 23.216014,
    long = 77.408138
)