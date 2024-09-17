package com.example.attendencetrackingapp.Worker

import android.content.Context
import android.content.pm.ServiceInfo
import android.location.Location
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.attendencetrackingapp.LocationService.LocationProvider
import com.example.attendencetrackingapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationWork(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val locationProvider = LocationProvider(applicationContext)
    private val database = FirebaseDatabase.getInstance()
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val userId = firebaseUser?.uid
    private val userRef = database.getReference("users")



    override suspend fun doWork(): Result  {
//        setForeground(createForegroundInfo())
        Log.d("LocationWork", "Worker started")
        return try {
//            ensureDateNodeExists(userId)
            //office location
            val (officeLat, officeLong) = userId?.let { fetchOfficeCoordinates(it) } ?: Pair(37.4219983, -122.084)
            // Fetch last location
            val location = withContext(Dispatchers.IO) {
                locationProvider.getLastLocation().await()
            }
            location?.let {
                val distance = calculateDistance(it.latitude, it.longitude, officeLat, officeLong)
                Log.d(
                    "LocationWork",
                    "Location of user fetched: Lat: ${it.latitude}, Long: ${it.longitude}"
                )
                Log.d("LocationWork", "Calculated distance: $distance meters")
                if (distance < 200) {
                    Log.d("LocationWork", "User is within 200 meters of the office. Proceeding with punch-in.")
                    if (userId != null) {
                        punchInUser(userId, it.latitude, it.longitude)
                    }
                    Log.d("LocationWork", "Punch-in successful.")
                } else {
                    Log.d("LocationWork", "User is more than 200 meters from the office. Proceeding with punch-out.")
                    if (userId != null) {
                        punchOutUser(userId, it.latitude, it.longitude)
                    }
                    Log.d("LocationWork", "Punch-out successful.")
                }

//                saveLocationToFirebase(it)
            } ?: run {
                Log.d("LocationWork", "Location is null, returning failure")
                return Result.failure()
            }

            Log.d("LocationWork", "Worker finished successfully")
            Result.success()
        } catch (e: Exception) {
            // Log error or handle exception
            Log.d("LocationWork", "${e.message}")
            Result.failure()
        }
    }
    private fun createForegroundInfo(): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, "location_channel_id")
            .setContentTitle("Attendance Tracking")
            .setContentText("Tracking your location for attendance purposes")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Your app icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .build()

        return ForegroundInfo(
            1,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION // This is the important part
        )
    }
    private suspend fun fetchOfficeCoordinates(userId: String): Pair<Double, Double>? {
        return try {
            Log.d("LocationWork", "Fetching office coordinates for userId: $userId")
            val userSnapshot = userRef.child(userId).get().await()
            if (userSnapshot.exists()) {
                val officeLat = userSnapshot.child("officeLatitude").getValue(Double::class.java) ?: 0.0
                val officeLong = userSnapshot.child("officeLongitude").getValue(Double::class.java) ?: 0.0
                Log.d("LocationWork", "Office coordinates fetched: Lat: $officeLat, Long: $officeLong")
                Pair(officeLat, officeLong)
            } else {
                Log.e("LocationWork", "User data does not exist in firebase")
                null
            }
        } catch (e: Exception) {
            Log.e("LocationWork", "Error fetching user data: ${e.message}")
            null
        }
    }
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371e3 // Earth radius in meters
        val φ1 = lat1 * Math.PI / 180
        val φ2 = lat2 * Math.PI / 180
        val Δφ = (lat2 - lat1) * Math.PI / 180
        val Δλ = (lon2 - lon1) * Math.PI / 180

        val a = sin(Δφ / 2) * sin(Δφ / 2) + cos(φ1) * cos(φ2) * sin(Δλ / 2) * sin(Δλ / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c // Distance in meters
    }

    private suspend fun punchInUser(userId: String, currentLat: Double, currentLong: Double) {
        val attendanceRef =
            database.getReference("AttendanceRecord/$userId/Attendance/${getCurrentDate()}/Punches")

        // Get the last punch entry
        val lastPunchSnapshot =
            attendanceRef.orderByKey().limitToLast(1).get().await().children.lastOrNull()

        // Check if there's already a punch-in without a corresponding punch-out
        if (lastPunchSnapshot != null) {
            val lastPunchData = lastPunchSnapshot.value as Map<*, *>
            if (lastPunchData.containsKey("punchInTime") && !lastPunchData.containsKey("punchOutTime")) {
                // User has already punched in without punching out, prevent new punch-in
                Log.d("LocationWork", "Cannot punch in again without punching out first.")
                return
            }
        }

        // If there’s no unclosed punch-in, create a new punch-in record
        val newPunchKey = attendanceRef.push().key
        newPunchKey?.let {
            val punchInTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val punchData = mapOf("punchInTime" to punchInTime)

            try {
                attendanceRef.child(it).setValue(punchData).await()
                Log.d("LocationWork", "Punch-in data saved to Firebase")
            } catch (e: Exception) {
                Log.e("LocationWork", "Error saving punch-in data to Firebase: ${e.message}")
            }
        }
    }

    private suspend fun punchOutUser(userId: String, currentLat: Double, currentLong: Double) {
        val attendanceRef =
            database.getReference("AttendanceRecord/$userId/Attendance/${getCurrentDate()}/Punches")
        val lastPunchKey =
            attendanceRef.orderByKey().limitToLast(1).get().await().children.lastOrNull()?.key

        lastPunchKey?.let {
            val punchOutTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val punchData = mapOf("punchOutTime" to punchOutTime)

            try {
                attendanceRef.child(it).updateChildren(punchData).await()
                Log.d("LocationWork", "Punch-out data updated in Firebase")
            } catch (e: Exception) {
                Log.e("LocationWork", "Error updating punch-out data in Firebase: ${e.message}")
            }
        }
    }


    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}