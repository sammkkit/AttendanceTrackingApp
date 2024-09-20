package com.example.attendencetrackingapp.Presentation.Screens

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.attendencetrackingapp.Presentation.Navigation.Routes
import com.example.attendencetrackingapp.R
import com.example.attendencetrackingapp.ViewModels.AuthViewModel
import com.example.attendencetrackingapp.ViewModels.MainViewModel
import java.util.Locale


fun parseCoordinates(coordinates: String): Pair<Double, Double> {
    return if (coordinates.isNotBlank()) {
        val parts = coordinates.split(",")
        if (parts.size == 2) {
            val latitude = parts[0].toDoubleOrNull() ?: 0.0
            val longitude = parts[1].toDoubleOrNull() ?: 0.0
            Pair(latitude, longitude)
        } else {
            Pair(0.0, 0.0) // Default values or error handling
        }
    } else {
        Pair(0.0, 0.0) // Default values or error handling
    }
}

fun getAddressFromCoordinates(context: Context, latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address>?

    return try {
        addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses!!.isNotEmpty()) {
            val address: Address = addresses[0]
            // Construct the address string from the Address object
            "${address.getAddressLine(0)}, ${address.locality}, ${address.countryName}"
        } else {
            "Address not found"
        }
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {


    val username by mainViewModel.username.observeAsState("")
    val email by mainViewModel.email.observeAsState("")
    val officeLocation by mainViewModel.officeLocation.observeAsState(initial = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Profile section with a person icon instead of an image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.crypticbytes),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${username}")
            Text(text = "${email}", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Edit Profile Button
//        Button(
//            onClick = { /* Handle edit profile click */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 32.dp),
//            colors = ButtonDefaults.buttonColors(Color.Blue)
//        ) {
//            Text(text = "Edit Profile", color = Color.White)
//        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu items list
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "My Profile", modifier = Modifier.weight(1f))
            }
            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Settings", modifier = Modifier.weight(1f))
            }
            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Privacy Policy", modifier = Modifier.weight(1f))
            }
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = " Office - ${
                        officeLocation
                    }", modifier = Modifier.weight(1f)
                )
            }
            Divider()
        }

        Spacer(modifier = Modifier.weight(1f))

        // Log out Button
        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate(Routes.Login.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                } // Navigate to login on log out
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF5874FC))
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Log out", color = Color.White)
        }
    }
}

@Preview
@Composable
fun pr() {
    ProfileScreen(navController = rememberNavController())
}
//        }