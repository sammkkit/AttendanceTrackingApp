package com.example.attendencetrackingapp.Presentation.Screens

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.attendencetrackingapp.Presentation.Navigation.Routes
import com.example.attendencetrackingapp.ViewModels.AuthViewModel
import com.example.attendencetrackingapp.ViewModels.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SignUpScreen(
    navController: NavHostController
) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val locationViewModel: LocationViewModel = hiltViewModel()
    val context = LocalContext.current
    val firebaseUser by authViewModel.firebaseUser.collectAsState()
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    var showMapSheet by remember { mutableStateOf(false) }
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var isFocusedEmail by rememberSaveable { mutableStateOf(false) }
    val email by authViewModel.email.collectAsState()

    var isFocusedName by rememberSaveable { mutableStateOf(false) }
    val Name by authViewModel.name.collectAsState()

    var isFocusedPassword by rememberSaveable { mutableStateOf(false) }
    val password by authViewModel.password.collectAsState()

    val locationState = remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNav.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }

        Log.d("SignUpScreen", "Name: $Name, Email: $email, Password: $password")

    }
    LaunchedEffect(Unit) {
        locationViewModel.initLocationService(context)
        locationViewModel.getLastLocation { location ->
            location?.let {
                locationState.value = LatLng(it.latitude, it.longitude)
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Back",
            modifier = Modifier
                .padding(top = 16.dp)
                .size(34.dp)
        )

        Column {
            Text(
                text = "Welcome !",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Sign up to your Nest account to explore your place to live!!",
                color = Color(0xFF7D7F88)
            )
        }


        Column {

            Text(
                text = "Name",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isFocusedName || Name.isNotEmpty()) Color(0xFF917AFD).copy(
                            alpha = 0.1f
                        ) else Color(
                            0xFF7D7F88
                        ).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isFocusedName) Color(0xFF917AFD).copy(alpha = 1f) else Color(
                            0xFF7D7F88
                        ).copy(
                            alpha = 0.3f
                        ),
                        shape = RoundedCornerShape(50.dp)
                    )
            ) {
                TextField(
                    value = Name ?: "",
                    onValueChange = {
                        authViewModel.updateName(it)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isFocusedName || Name.isNotEmpty()) Color(0xFF917AFD).copy(
                                alpha = 1f
                            ) else Color(0xFF7D7F88).copy(alpha = 1.0f)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = if (isFocusedName) Color(0xFF917AFD) else Color(0xFF7D7F88)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocusedName = focusState.isFocused
                        },
                    visualTransformation = VisualTransformation.None,
                    placeholder = {
                        Text("Name")
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Email",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isFocusedEmail || email.isNotEmpty()) Color(0xFF917AFD).copy(
                            alpha = 0.1f
                        ) else Color(
                            0xFF7D7F88
                        ).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isFocusedEmail) Color(0xFF917AFD).copy(alpha = 1f) else Color(
                            0xFF7D7F88
                        ).copy(
                            alpha = 0.3f
                        ),
                        shape = RoundedCornerShape(50.dp)
                    )
            ) {
                TextField(
                    value = email,
                    onValueChange = {
                        authViewModel.updateEmail(it)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isFocusedEmail || email.isNotEmpty()) Color(0xFF917AFD).copy(
                                alpha = 1f
                            ) else Color(0xFF7D7F88).copy(alpha = 1.0f)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = if (isFocusedEmail) Color(0xFF917AFD) else Color(0xFF7D7F88)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocusedEmail = focusState.isFocused
                        },
                    visualTransformation = VisualTransformation.None,
                    placeholder = {
                        Text("Email")
                    }
                )
            }
        }


        Column {
            Text(
                text = "Password",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isFocusedPassword || password.isNotEmpty()) Color(0xFF917AFD).copy(
                            alpha = 0.1f
                        ) else Color(
                            0xFF7D7F88
                        ).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isFocusedPassword) Color(0xFF917AFD).copy(alpha = 0.1f) else Color(
                            0xFF7D7F88
                        ).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(50.dp)
                    )
            ) {
                TextField(
                    placeholder = {
                        Text(text = "Password")
                    },
                    value = password,
                    onValueChange = {
                        authViewModel.updatePassword(it)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isFocusedPassword || password.isNotEmpty()) Color(0xFF917AFD).copy(
                                alpha = 1f
                            ) else Color(0xFF7D7F88).copy(alpha = 1.0f)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = if (isFocusedPassword) Color(0xFF917AFD) else Color(0xFF7D7F88)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocusedPassword = focusState.isFocused
                        },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }
        if (!permissionsState.allPermissionsGranted) {
            Button(
                onClick = { permissionsState.launchMultiplePermissionRequest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(Color(0xFF785FF3))
            ) {
                Text(
                    text = "Grant Location Permission",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        } else {
            Button(
                onClick = {
                    showMapSheet = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(Color(0xFF785FF3))
            ) {
                Text(
                    text = "Select office Location",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
        if (permissionsState.allPermissionsGranted) {
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        selectedLatLng?.let {
                            authViewModel.signUp(
                                Name,
                                email,
                                password,
                                context,
                                it.latitude,
                                it.longitude
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(Color(0xFF785FF3))
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

    }
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(23.216464, 77.389849), 15f)
    }
    if (showMapSheet) {
        AlertDialog(
            onDismissRequest = {showMapSheet=false},
            content = {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Map
                    Box(modifier = Modifier.weight(1f)) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            properties = MapProperties(
                                isMyLocationEnabled = true
                            ),
                            cameraPositionState = cameraPositionState,
                            onMapClick = { latLng ->
                                selectedLatLng = latLng
                                showMapSheet = false
                            },
                            uiSettings = MapUiSettings(mapToolbarEnabled = true)
                        )
                        selectedLatLng?.let { latLng ->
                            Marker(
                                state = rememberMarkerState(position = latLng),
                                title = "Selected Location",
                                snippet = "Lat: ${latLng.latitude}, Long: ${latLng.longitude}"
                            )
                        }
                    }
                }
            }
        )

    }

}


@Preview(showBackground = true)
@Composable
fun preview() {
    SignUpScreen(rememberNavController())
}
