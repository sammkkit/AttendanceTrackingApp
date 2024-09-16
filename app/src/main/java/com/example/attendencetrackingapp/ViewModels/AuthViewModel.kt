package com.example.attendencetrackingapp.ViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    //TODO("try to inject work manager (error is we cant inject viewmodel of workmanager into aauth viewmodel)")
): ViewModel() {
    val auth =FirebaseAuth.getInstance()

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(null)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

    private val _Error = MutableStateFlow<String?>(null)
    val Error: StateFlow<String?> = _Error.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login( email:String , password:String,context: Context){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.value = auth.currentUser

                } else {
                    _Error.value = it.exception!!.message
                    Toast.makeText(context, _Error.value, Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun SaveUserData(user: FirebaseUser,name: String,officeLat:Double,officeLong:Double){
        user?.let {
            val userId = user.uid
            val userEmail = user.email
            val userMap = mapOf(
                "email" to userEmail,
                "name" to name,
                "officeLatitude" to officeLat,
                "officeLongitude" to officeLong
            )

            // Save user details under their UID
            userRef.child(userId).setValue(userMap)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        _Error.value = it.exception?.message // Handle errors while saving user data
                    }
                }
        }
    }
    fun signUp(
        name:String,
        email:String,
        password: String,
        context: Context,
        officeLat:Double,
        officeLong:Double
    ){
        if (password.length < 6) {
            _Error.value = "Password should be at least 6 characters long."
            Toast.makeText(context, _Error.value, Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    user?.let{
                        _firebaseUser.value = it
                        SaveUserData(it,name,officeLat,officeLong)
                    }
                } else {
                    _Error.value = it.exception?.message
                    Toast.makeText(context, _Error.value, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun logout() {
        auth.signOut()
        _firebaseUser.value = null
    }
}