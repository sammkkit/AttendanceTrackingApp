package com.example.attendencetrackingapp.Worker

//import android.app.Notification
//import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
//import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
//import androidx.biometric.BiometricPrompt
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.platform.LocalContext
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.FragmentActivity
//@Composable
//fun BiometricPrompt(show: Boolean, onDismiss: () -> Unit) {
//    if (!show) {
//        return
//    }
//
//    val context = LocalContext.current
//    val dispatch = rememberTypedDispatcher<Notification.Action>()
//
//    LaunchedEffect(key1 = context) {
//        val fragmentActivity = context as? FragmentActivity ?: return@LaunchedEffect
//        val executor = ContextCompat.getMainExecutor(fragmentActivity)
//        val biometricPrompt = BiometricPrompt(
//            fragmentActivity,
//            executor,
//            object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationError(
//                    errorCode: Int,
//                    errString: CharSequence
//                ) {
//                    super.onAuthenticationError(errorCode, errString)
//                    onDismiss()
//                    dispatch(
//                        ToastActionStr(
//                            context.getString(
//                                R.string.authentication_error,
//                                errString
//                            )
//                        )
//                    )
//                }
//
//                override fun onAuthenticationSucceeded(
//                    result: BiometricPrompt.AuthenticationResult
//                ) {
//                    super.onAuthenticationSucceeded(result)
//                    dispatch(NavigationAction(HomeState(), true))
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    onDismiss()
//                    dispatch(ToastAction(R.string.authentication_failed))
//                }
//            }
//        )
//
//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle(context.getString(R.string.app_name))
//            .setSubtitle(context.getString(R.string.login_to_enter_keypass))
//            .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
//            .setNegativeButtonText(context.getText(R.string.cancel))
//            .build()
//
//        biometricPrompt.authenticate(promptInfo)
//    }
//}
