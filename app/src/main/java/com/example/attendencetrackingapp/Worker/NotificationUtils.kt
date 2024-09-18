package com.example.attendencetrackingapp.Worker
//
//// NotificationUtils.kt
//import android.annotation.SuppressLint
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import androidx.compose.ui.platform.LocalContext
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.example.attendencetrackingapp.R
//
//const val CHANNEL_ID = "attendance_channel"
//const val NOTIFICATION_ID = 1
//
//@SuppressLint("MissingPermission")
//fun sendNotification(context: Context) {
//    val notificationManager = NotificationManagerCompat.from(context)
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        notificationManager.createNotificationChannel(
//            NotificationChannel(CHANNEL_ID, "Attendance", NotificationManager.IMPORTANCE_HIGH)
//        )
//    }
//
//    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
//        .setSmallIcon(R.drawable.ic_launcher_foreground)
//        .setContentTitle("Mark Attendance")
//        .setContentText("You are near the office. Tap to mark attendance.")
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .setContentIntent(
//            PendingIntent.getActivity(
//                context,
//                0,
//                Intent(context, AttendanceActivity::class.java),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        )
//
//    notificationManager.notify(NOTIFICATION_ID, builder.build())
//}