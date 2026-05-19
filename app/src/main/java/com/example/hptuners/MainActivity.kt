package com.example.hptuners

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.hptuners.ui.theme.HPTunersTheme
import com.example.hptuners.workers.LoadBreedInformationWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private lateinit var workManager: WorkManager
    val workManager by lazy { WorkManager.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        syncBreedData()

        enableEdgeToEdge()
        setContent {
            HPTunersTheme {
                CatAdoptionBoard()
            }
        }
    }

    private fun syncBreedData() {
        val breedDataRequest = OneTimeWorkRequestBuilder<LoadBreedInformationWorker>().build()
        workManager.enqueue(breedDataRequest)
    }
}