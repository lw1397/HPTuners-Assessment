package com.example.adoptacat.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.adoptacat.data.breed.BreedRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LoadBreedInformationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val breedRepository: BreedRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            breedRepository.loadAllBreeds()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}