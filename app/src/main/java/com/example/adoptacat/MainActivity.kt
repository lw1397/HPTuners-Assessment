package com.example.adoptacat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.adoptacat.ui.theme.AdoptACatTheme
import com.example.adoptacat.workers.LoadBreedInformationWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val workManager by lazy { WorkManager.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        syncBreedData()

        enableEdgeToEdge()
        setContent {
            AdoptACatTheme {
                CatAdoptionBoard()
            }
        }
    }

    private fun syncBreedData() {
        val breedDataRequest = OneTimeWorkRequestBuilder<LoadBreedInformationWorker>().build()
        workManager.enqueue(breedDataRequest)
    }
}