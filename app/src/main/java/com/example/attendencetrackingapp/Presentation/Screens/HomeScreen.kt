package com.example.attendencetrackingapp.Presentation.Screens

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.attendencetrackingapp.Presentation.Components.BottomNavigationBar
import com.example.attendencetrackingapp.R

//@Composable
//fun HomeScreen(
//    navController: NavHostController,
//){
//    Column {
//        Text(text = "Home Screen")
//    }
//}

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Section with profile and date navigation
        HeaderSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Today Attendance section
        TodayAttendanceSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Activity Section
        ActivitySection()

        Spacer(modifier = Modifier.height(16.dp))

        // Swipe to Check In Section
        SwipeToCheckIn()

    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // replace with actual image
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = "Michael Mitc", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Lead UI/UX Designer", color = Color.Gray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bell Icon
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_background), // replace with actual bell icon
            contentDescription = "Notifications",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }

    // Date Navigation Row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DateBox("06", "Thu")
        DateBox("07", "Fri")
        DateBox("08", "Sat")
        DateBox("09", "Sun", isSelected = true) // Current date is selected
        DateBox("10", "Mon")
    }
}

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

@Composable
fun TodayAttendanceSection() {
    Column {
        Text(text = "Today Attendance", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            AttendanceInfoBox("Check In", "10:20 am", "On Time", Icons.Default.Check)
            AttendanceInfoBox("Check Out", "07:00 pm", "Go Home", Icons.Default.Check)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            AttendanceInfoBox("Break Time", "00:30 min", "Avg Time 30 min", Icons.Default.DateRange)
            AttendanceInfoBox("Total Days", "28", "Working Days", Icons.Default.DateRange)
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


@Composable
fun ActivitySection() {
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

                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ActivityLogItem("Check In", "April 17, 2023", "10:00 am", "On Time")
        ActivityLogItem("Break In", "April 17, 2023", "12:30 am", "On Time")
    }
}

@Composable
fun ActivityLogItem(activity: String, date: String, time: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = activity, fontWeight = FontWeight.Bold)
            Text(text = date, color = Color.Gray, fontSize = 12.sp)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = time, fontWeight = FontWeight.Bold)
            Text(text = status, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun SwipeToCheckIn() {
    Button(
        onClick = { /* Handle Swipe Action */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5874FC))
    ) {
        Text(text = "Swipe to Check In", color = Color.White)
    }
}
@Preview(showBackground = true)
@Composable
fun prererfwefwsf(){
    HomeScreen(rememberNavController())
}

