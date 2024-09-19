package com.example.attendencetrackingapp.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendencetrackingapp.Models.ActivityLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val user = firebaseAuth.currentUser
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("users").child(user!!.uid)
    private val attendanceRef =
        database.getReference("AttendanceRecord").child(user!!.uid).child("Attendance")
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _officeLocation = MutableLiveData<Pair<Double, Double>>()
    val officeLocation: LiveData<Pair<Double, Double>> get() = _officeLocation

    private val _activityLogs = MutableLiveData<List<ActivityLog>>()
    val activityLogs: LiveData<List<ActivityLog>> get() = _activityLogs

    init {
        if (user!= null){
            fetchUserData()
            fetchActivityLogs()
        }
    }

    private fun fetchUserData() {
        userRef.child("name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _username.value = snapshot.getValue(String::class.java) ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })

        userRef.child("email").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _email.value = snapshot.getValue(String::class.java) ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })

        userRef.child("officeCoordinates").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val coordinatesString = snapshot.getValue(String::class.java) ?: ""
                val (latitude, longitude) = parseCoordinates(coordinatesString)
                _officeLocation.value = Pair(latitude, longitude)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    private fun fetchActivityLogs() {
        attendanceRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {
                        val logs = mutableListOf<ActivityLog>()
                        for (dateSnapshot in snapshot.children) {
                            val date = dateSnapshot.key ?: continue
                            val punchesSnapshot = dateSnapshot.child("Punches")
                            val dayPunches = mutableListOf<Pair<String, String?>>()
                            for (punchSnapshot in punchesSnapshot.children) {
                                val punchInTime =
                                    punchSnapshot.child("punchInTime").getValue(String::class.java)
                                        ?: continue
                                val punchOutTime =
                                    punchSnapshot.child("punchOutTime").getValue(String::class.java)
                                dayPunches.add(Pair(punchInTime, punchOutTime))
                            }
                            dayPunches.sortBy { it.first }

                            for (punch in dayPunches) {
                                logs.add(ActivityLog(
                                    activity = "Check In",
                                    date = formatDate(date),
                                    time = formatTime(punch.first),
                                    status = determineStatus(punch.first)
                                ))

                                if (punch.second != null) {
                                    logs.add(ActivityLog(
                                        activity = "Check Out",
                                        date = formatDate(date),
                                        time = formatTime(punch.second!!),
                                        status = determineStatus(punch.second!!)
                                    ))
                                }
                            }

                        }
                        _activityLogs.value = logs.sortedByDescending { it.date  }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    private fun parseCoordinates(coordinatesString: String): Pair<Double, Double> {
        val latitude: Double
        val longitude: Double
        return try {
            val regex = Regex("""lat/lng: \(([^,]+),([^)]*)\)""")
            val matchResult = regex.find(coordinatesString)
            if (matchResult != null) {
                latitude = matchResult.groupValues[1].toDouble()
                longitude = matchResult.groupValues[2].toDouble()
                Pair(latitude, longitude)
            } else {
                Pair(0.0, 0.0)
            }
        } catch (e: Exception) {
            Pair(0.0, 0.0)
        }
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

    private fun formatTime(timeString: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val time = inputFormat.parse(timeString)
        return outputFormat.format(time!!)
    }

    private fun determineStatus(time: String): String {
        // Implement your logic to determine status based on time
        // For example, you could compare it with a predefined schedule
        return "On Time" // Placeholder
    }
}
