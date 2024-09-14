package com.example.attendencetrackingapp.DI

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        // Initialize FirebaseApp if not already initialized
        return FirebaseDatabase.getInstance()
    }
}