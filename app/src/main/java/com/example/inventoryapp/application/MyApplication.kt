package com.example.inventoryapp.application

import android.app.Application
import com.example.inventoryapp.model.room.ProductDatabase
import com.example.inventoryapp.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { ProductDatabase.getDatabase(this) }
    val repository by lazy { MainRepository(database.wordDao()) }

}