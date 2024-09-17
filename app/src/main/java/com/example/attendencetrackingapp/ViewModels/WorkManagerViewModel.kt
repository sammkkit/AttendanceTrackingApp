package com.example.attendencetrackingapp.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.attendencetrackingapp.Worker.LocationWork
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WorkManagerViewModel @Inject constructor(
    private val workManager: WorkManager
):ViewModel(){
    fun startWork(){
        Log.d("WorkManagerViewModel", "Starting WorkManager")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<LocationWork>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "LocationWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
        observeWorkStatus(workRequest.id)
    }
    private fun observeWorkStatus(workRequestId: UUID) {
        workManager.getWorkInfoByIdLiveData(workRequestId)
            .observeForever { workInfo ->
                workInfo?.let {
                    when (it.state) {
                        WorkInfo.State.ENQUEUED -> Log.d("WorkManagerViewModel", "Work is enqueued")
                        WorkInfo.State.RUNNING -> Log.d("WorkManagerViewModel", "Work is running")
                        WorkInfo.State.SUCCEEDED -> Log.d("WorkManagerViewModel", "Work completed successfully")
                        WorkInfo.State.FAILED -> Log.d("WorkManagerViewModel", "Work failed")
                        else -> Log.d("WorkManagerViewModel", "Work in unexpected state: ${it.state}")
                    }
                }
            }

    }

}



//val locationWorkRequest = OneTimeWorkRequestBuilder<LocationWork>()
//            .build()
//        WorkManager.getInstance()
//            .enqueue(locationWorkRequest)