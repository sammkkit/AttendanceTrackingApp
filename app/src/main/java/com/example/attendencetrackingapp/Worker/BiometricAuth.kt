package com.example.attendencetrackingapp.Worker

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.concurrent.Executor

class BiometricHelper(
    private val fragment: Fragment,
    private val onAuthenticationSuccess: () -> Unit
) {

    private val executor: Executor = ContextCompat.getMainExecutor(fragment.requireContext())
    private val biometricPrompt: BiometricPrompt = BiometricPrompt(
        fragment,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Handle the error, for example, show a Toast
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthenticationSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // Handle authentication failure, for example, show a Toast
            }
        }
    )

    fun authenticate() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
