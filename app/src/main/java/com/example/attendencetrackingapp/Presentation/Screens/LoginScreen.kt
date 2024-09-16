package com.example.attendencetrackingapp.Presentation.Screens


import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LoginScreen(
    navController: NavHostController
) {
    val authViewModel :AuthViewModel = hiltViewModel()
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val firebaseUser by authViewModel.firebaseUser.collectAsState(initial = null)
    LaunchedEffect(firebaseUser){
        if(firebaseUser!=null){
            navController.navigate(Routes.BottomNav.route){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop=true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Back",
            modifier = Modifier
                .padding(top = 16.dp)
                .size(34.dp)
        )

        // Welcome Text
        Column {
            Text(
                text = "Welcome !",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Sign up to your GeoLog account to ease your attendance tracking!!",
                color = Color(0xFF7D7F88)
            )
        }

        // Email Field
        Column {
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
                        color = if (isEmailFocused || email.isNotEmpty()) Color(0xFF917AFD).copy(
                            alpha = 0.1f
                        ) else Color(0xFF7D7F88).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isEmailFocused) Color(0xFF917AFD) else Color(0xFF7D7F88).copy(
                            alpha = 0.3f
                        ),
                        shape = RoundedCornerShape(50.dp)
                    )
            ) {
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isEmailFocused || email.isNotEmpty()) Color(0xFF917AFD) else Color(0xFF7D7F88)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = if (isEmailFocused) Color(0xFF917AFD) else Color(0xFF7D7F88)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState -> isEmailFocused = focusState.isFocused },
                    placeholder = {
                        Text("Email")
                    }
                )
            }
        }

        // Password Field
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
                        color = if (isPasswordFocused || password.isNotEmpty()) Color(0xFF917AFD).copy(
                            alpha = 0.1f
                        ) else Color(0xFF7D7F88).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isPasswordFocused) Color(0xFF917AFD) else Color(0xFF7D7F88).copy(
                            alpha = 0.3f
                        ),
                        shape = RoundedCornerShape(50.dp)
                    )
            ) {
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isPasswordFocused || password.isNotEmpty()) Color(0xFF917AFD) else Color(0xFF7D7F88)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = if (isPasswordFocused) Color(0xFF917AFD) else Color(0xFF7D7F88)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState -> isPasswordFocused = focusState.isFocused },
                    placeholder = {
                        Text("Password")
                    }
                )
            }
        }

        // Login Button
//        Button(
//            onClick = {
//                if(email.isEmpty() || password.isEmpty()){
//                    Toast.makeText(context,"Enter All fields", Toast.LENGTH_SHORT).show()
//                }else{
//                    authViewModel.login(email, password,context)
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .border(
//                    width = 1.dp,
//                    color = Color.White,
//                    shape = RoundedCornerShape(25.dp)
//                ),
//            colors = ButtonDefaults.buttonColors(Color(0xFF785FF3))
//        ) {
//            Text(
//                text = "Log in",
//                color = Color.White,
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp
//            )
//        }



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
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Enter All fields", Toast.LENGTH_SHORT).show()
                    } else {
                        authViewModel.login(email, password, context)
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
                    text = "Log in",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }


        // Forgot Password Link
//        Text(
//            text = "Forgot password?",
//            color = Color(0xFF917AFD),
//            modifier = Modifier
//                .clickable { /* Handle forgot password */ }
//        )

        // OR Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Divider(
                color = Color.Gray.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "OR",
                color = Color(0xFF9E91DA),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFF3F0FF), shape = RoundedCornerShape(44.dp))
                    .padding(vertical = 4.dp, horizontal = 12.dp)
            )
            Divider(
                color = Color.Gray.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
        }

        // Signup Link
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Routes.Signup.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
                    ,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "New? ",
                color = Color.Gray
            )
            Text(
                text = "Sign up",
                color = Color(0xFF917AFD),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Signup.route) { }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun prev(){
    LoginScreen(navController = rememberNavController())
}
