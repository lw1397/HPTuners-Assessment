package com.example.hptuners

import android.content.Context
import androidx.room.Room
import com.example.hptuners.data.adoptedCat.AdoptedCatDao
import com.example.hptuners.data.breed.BreedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "hptuners_db"
        )
            // Development only, I would NEVER have this in a production app
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideKtor() =
        HttpClient(CIO) {
            install(Resources)
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // Prevents crashes if API adds new fields
                })
            }
            install(DefaultRequest) {
                url(BuildConfig.BASE_URL)
                header("x-api-key", BuildConfig.CATS_API_KEY)
            }
        }


    @Provides
    @Singleton
    fun provideBreedDao(db: AppDatabase): BreedDao = db.breedDao()

    @Provides
    @Singleton
    fun provideAdoptedCatDao(db: AppDatabase): AdoptedCatDao = db.adoptedCatDao()
}