package com.example.attendencetrackingapp.Worker
//
//// BiometricAuth.kt
//import android.content.Context
//import android.hardware.biometrics.BiometricManager
//import android.os.Build
//import androidx.biometric.BiometricPrompt
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//
//class BiometricAuth(private val context: Context) {
//    private val biometricManager = BiometricManager.from(context)
//
//    fun authenticate(onSuccess: () -> Unit, onError: () -> Unit) {
//        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
//            val biometricPrompt = BiometricPrompt(context as AppCompatActivity, executor, callback)
//            val promptInfo = BiometricPrompt.PromptInfo.Builder()
//                .setTitle("Authenticate")
//                .setDescription("Verify your identity")
//                .setNegativeButtonText("Cancel")
//                .build()
//            biometricPrompt.authenticate(promptInfo)
//        } else {
//            onError()
//        }
//
//        val executor = Executor { command -> command.run() }
//        val callback = object : BiometricPrompt.AuthenticationCallback() {
//            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
//                super.onAuthenticationError(errorCode, errString)
//                onError()
//            }
//
//            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
//                super.onAuthenticationSucceeded(result)
//                onSuccess()
//            }
//        }
//    }
//}