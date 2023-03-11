package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.api.NasaImageApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDao
import com.udacity.asteroidradar.database.DatabaseHelper
import org.json.JSONObject
import java.time.LocalDate

class AsteroidRepo(private val database: AsteroidsDao) {
    private var today = LocalDate.now().toString()
    var endDate = LocalDate.now().plusDays(7).toString()
    val API_KEY = "0SFEMaVSIupLNBuIH8qDZ5hhNKAkJflFeCro1Mmr"

    val allAsteroids = database.getAllAsteroids()
    val asteroidsOfWeek = database.getAsteroidsByDate(today, endDate)
    val asteroidOfToday = database.getAsteroidOfToday(today)

    suspend fun refreshAsteroid(){
            try{
                val response = NasaImageApi.retrofitService.getAsteroids (
                    endDate,
                    today,
                    API_KEY)
                val jsonObject = JSONObject(response)
                val list = parseAsteroidsJsonResult(jsonObject)

                for (a in list) {
                    database.insert(a)
                }
            } catch(exc:Exception){
                Log.e("MainViewModel",exc.message,exc)
            }
    }

    suspend fun refreshTodayAsteroid(){
        try{
            val response = NasaImageApi.retrofitService.getAsteroids (
                today,
                today,
                API_KEY)
            val jsonObject = JSONObject(response)
            val list = parseAsteroidsJsonResult(jsonObject)

            for (a in list) {
                database.insert(a)
            }
        } catch(exc:Exception){
            Log.e("MainViewModel",exc.message,exc)
        }
    }


    suspend fun deletePreviousAsteroids(){
        database.deletePreviousAsteroid(today)
    }
}