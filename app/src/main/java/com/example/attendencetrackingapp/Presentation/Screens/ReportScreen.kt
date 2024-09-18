package com.example.attendencetrackingapp.Presentation.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.attendencetrackingapp.ViewModels.MainViewModel

@Composable
fun ReportScreen(
    navController: NavHostController,
){

    val mainViewModel: MainViewModel = hiltViewModel()
    val activityLogs by mainViewModel.activityLogs.observeAsState(initial = emptyList())
    val groupedLogs = activityLogs.groupBy { it.date }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        groupedLogs.forEach { (date, logs) ->
            // Date Header
            item {
                Text(
                    text = date, // Assuming date is a formatted string
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Check-in and Check-out logs for this date
            items(logs) { log ->
                ActivityLogItem(log) // Display each log (check-in/check-out)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Add space after each date section
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun ActivityLogItem(log: ActivityLog) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Check-in: ${log.checkInTime}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Check-out: ${log.checkOutTime}", style = MaterialTheme.typography.bodyMedium)
    }
}

// Sample data model
data class ActivityLog(
    val date: String, // Formatted date like "19 Sept, Thu"
    val checkInTime: String,
    val checkOutTime: String
)