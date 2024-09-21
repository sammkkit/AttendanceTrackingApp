package com.example.attendencetrackingapp.Presentation.Navigation

sealed class Routes(val route:String) {
    object Home: Routes("home")
    object Profile: Routes("profile")
    object Report: Routes("report")
    object Splash: Routes("splash")
    object BottomNav: Routes("bottom_nav")
    object Login: Routes("login")
    object Signup: Routes("signup")
    object Privacy : Routes("privacy_screen")
}