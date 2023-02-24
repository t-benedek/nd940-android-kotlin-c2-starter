package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val asteroidList = listOf<Asteroid>(
        Asteroid(1, "BLING", "11",12.0,
            12.0, 12.0, 12.0, true),
        Asteroid(2, "BONG", "11",12.0,
        12.0, 12.0, 12.0, true),
        Asteroid(2, "BONG", "11",12.0,
        12.0, 12.0, 12.0, true)
    )

    val asteroids = MutableLiveData<List<Asteroid>>(asteroidList)


}