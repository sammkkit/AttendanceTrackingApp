package com.example.attendencetrackingapp.Presentation.Components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.attendencetrackingapp.Presentation.Navigation.Routes

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(Routes.Home.route, Icons.Default.Home, "Home")
    object Report : BottomNavItem(Routes.Report.route, Icons.Default.ShoppingCart, "Report")
    object Profile : BottomNavItem(Routes.Profile.route, Icons.Default.Person, "Profile")
}

@Composable
fun BottomNavigationBar(
    navController : NavHostController,
){
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Report,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar (
        containerColor = Color.White,
//         contentColor = Color.Red
    ){
        val mycolor = Color(0xFF5874FC)
        items.forEach{item->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(
                                if(currentRoute==item.route){
                                    34.dp
                                }else{
                                    30.dp
                                }
                            )
                        , tint =  if(currentRoute==item.route){
                            mycolor
                        }else{
                            Color.DarkGray
                        }
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute!=item.route){
                        navController.navigate(item.route){
                            popUpTo = navController.graph.findStartDestination().id
                            launchSingleTop = true
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = if(currentRoute==item.route){
                            13.sp
                        }else{
                            12.sp
                        },
                        color = if(currentRoute==item.route){
                            mycolor
                        }else{
                            Color.DarkGray
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // This removes the background for selected item
                )
            )
        }

    }
}