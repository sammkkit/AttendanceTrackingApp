package com.example.attendencetrackingapp.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val user = firebaseAuth.currentUser
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("users").child(user!!.uid)

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _officeLocation = MutableLiveData<Pair<Double, Double>>()
    val officeLocation: LiveData<Pair<Double, Double>> get() = _officeLocation

    init {
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
}
