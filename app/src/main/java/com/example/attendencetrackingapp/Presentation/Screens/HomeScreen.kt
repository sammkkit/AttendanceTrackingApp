package com.example.attendencetrackingapp.Presentation.Screens

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.attendencetrackingapp.Models.ActivityLog
import com.example.attendencetrackingapp.Presentation.Components.BottomNavigationBar
import com.example.attendencetrackingapp.Presentation.Navigation.Routes
import com.example.attendencetrackingapp.R
import com.example.attendencetrackingapp.ViewModels.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executor


@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val username by mainViewModel.username.observeAsState("")
    val email by mainViewModel.email.observeAsState("")
    val activityLogs by mainViewModel.activityLogs.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header Section with profile and date navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.crypticbytes), contentDescription =null, modifier = Modifier.size(50.dp) )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "${username}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${email}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Date Navigation Row
        // Get today's date
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd")
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE")

        val todayDay = today.format(formatter)
        val todayWeekDay = today.format(dayOfWeekFormatter)

        val dayMonthFormatter = DateTimeFormatter.ofPattern("dd MMM")
        val formattedDate = "${today.format(dayMonthFormatter)}, ${today.format(dayOfWeekFormatter)}"

        val dates = listOf(
            today.minusDays(3),
            today.minusDays(2),
            today.minusDays(1),
            today,
            today.plusDays(1)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Date Navigation Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            @Composable
            fun DateBox(day: String, weekDay: String, isSelected: Boolean = false) {
                val backgroundColor = if (isSelected) Color(0xFF5874FC) else Color.White
                val textColor = if (isSelected) Color.White else Color.Black
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(backgroundColor)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = day, color = textColor, fontSize = 18.sp)
                    Text(text = weekDay, color = textColor, fontSize = 14.sp)
                }
            }

            // Iterate through dates and create DateBox for each
            dates.forEach { date ->
                val day = date.format(formatter)
                val weekDay = date.format(dayOfWeekFormatter)
                DateBox(day, weekDay, date == today) // Highlight today
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Today Attendance section
        Column {
            Text(text = "Today Attendance", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                @Composable
                fun AttendanceInfoBox(
                    title: String,
                    time: String,
                    description: String,
                    vectorIcon: ImageVector? = null,
                    drawableIcon: Int? = null
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .size(130.dp, 130.dp)
                            .background(Color(0xFFF3F0FF))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (vectorIcon != null) {
                            Icon(
                                imageVector = vectorIcon,
                                contentDescription = null,
                                tint = Color(0xFF5874FC),
                                modifier = Modifier.size(24.dp)
                            )
                        } else if (drawableIcon != null) {
                            Icon(
                                painter = painterResource(id = drawableIcon),
                                contentDescription = null,
                                tint = Color(0xFF5874FC),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = time, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = description, color = Color.Gray, fontSize = 14.sp)
                    }
                }

                AttendanceInfoBox("Check In", "09:00 am", "On Time", Icons.Default.Check)
                AttendanceInfoBox("Check Out", "07:00 pm", "Go Home", Icons.Default.Check)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                AttendanceInfoBox(
                    "Break Time",
                    "06:30 hours",
                    "Total Time",
                    Icons.Default.DateRange
                )
                AttendanceInfoBox("Total Days", "${formattedDate}", "Date", Icons.Default.DateRange)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Activity Section
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Your Activity", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = "View All",
                    color = Color(0xFF5874FC),
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.Report.route)
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            activityLogs.take(2).forEach { log ->
                ActivityLogItem(log)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

        }

        // Swipe to Check In Section
        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF5874FC))
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Check In", color = Color.White)
        }
    }
}

// Retrieve a BiometricPrompt instance with a predefined callback

@Composable
fun ActivityLogItem(log: ActivityLog) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = log.activity, fontWeight = FontWeight.Bold)
            Text(text = log.date, color = Color.Gray, fontSize = 12.sp)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = log.time, fontWeight = FontWeight.Bold)
            Text(text = log.status, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun AttendanceInfoBox(
    title: String,
    time: String,
    description: String,
    vectorIcon: ImageVector? = null,
    drawableIcon: Int? = null
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .size(130.dp, 130.dp)
            .background(Color(0xFFF3F0FF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (vectorIcon != null) {
            Icon(
                imageVector = vectorIcon,
                contentDescription = null,
                tint = Color(0xFF5874FC),
                modifier = Modifier.size(24.dp)
            )
        } else if (drawableIcon != null) {
            Icon(
                painter = painterResource(id = drawableIcon),
                contentDescription = null,
                tint = Color(0xFF5874FC),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = time, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = description, color = Color.Gray, fontSize = 14.sp)
    }
}



