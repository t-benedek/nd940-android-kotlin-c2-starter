package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepo
import retrofit2.HttpException

class RefreshDataWork(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepo(database.asteroidsDao)
        return try {
            // get only asteroids of today
            repository.refreshTodayAsteroid()
            Result.success()
        }catch (e:HttpException){
            Result.retry()
        }
    }
}