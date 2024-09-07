package com.example.attendencetrackingapp.ViewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    val auth =FirebaseAuth.getInstance()

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(null)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

    private val _Error = MutableStateFlow<String?>(null)
    val Error: StateFlow<String?> = _Error.asStateFlow()

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
    private fun SaveUserData(user: FirebaseUser,name: String){
        user?.let {
            val userId = user.uid
            val userEmail = user.email
            val userMap = mapOf(
                "email" to userEmail,
                "name" to name
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
    fun signUp(name:String,email:String,password: String,context: Context){
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
                        SaveUserData(it,name)
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