package com.example.attendencetrackingapp.Presentation.Screens

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController) {

    val authViewModel:AuthViewModel = hiltViewModel()
    val context = LocalContext.current
    val firebaseUser by authViewModel.firebaseUser.collectAsState()

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

        var isFocusedEmail by remember { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }

        var isFocusedName by remember { mutableStateOf(false) }
        var Name by remember { mutableStateOf("") }

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
                    value = Name,
                    onValueChange = {
                        Name = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isFocusedName || Name.isNotEmpty()) Color(0xFF917AFD).copy(alpha = 1f) else Color(0xFF7D7F88).copy(alpha = 1.0f)
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
                        email = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = if (isFocusedEmail || email.isNotEmpty()) Color(0xFF917AFD).copy(alpha = 1f) else Color(0xFF7D7F88).copy(alpha = 1.0f)
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

        var isFocusedPassword by remember { mutableStateOf(false) }
        var password by remember { mutableStateOf("") }

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
                        password = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isFocusedPassword || password.isNotEmpty()) Color(0xFF917AFD).copy(alpha = 1f) else Color(0xFF7D7F88).copy(alpha = 1.0f)
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
        Button(
            onClick = {
                navController.navigate(Routes.MapScreen.route){
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop=true
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
                text = "Select Office location",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Button(
            onClick = {
                if ( email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    authViewModel.signUp(Name,email, password,context)
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

@Preview(showBackground = true)
@Composable
fun preview() {
    SignUpScreen(rememberNavController())
}
