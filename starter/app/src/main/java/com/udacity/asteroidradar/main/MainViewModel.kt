package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.ImageDay
import com.udacity.asteroidradar.api.NasaImageApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepo
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val asteroidsDao = getDatabase(application).asteroidsDao
    private val repo = AsteroidRepo(asteroidsDao)

    // Navigation to Details status
    private var _navigateToAsteroidDetail=MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail:LiveData<Asteroid?>
        get() = _navigateToAsteroidDetail

    enum class OptionMenu { SHOW_ALL, SHOW_TODAY, SHOW_WEEK }
    var optionMenu = MutableLiveData(OptionMenu.SHOW_WEEK)

    var asteroids:LiveData<List<Asteroid>?> = Transformations.switchMap(optionMenu){
        when(it){
            OptionMenu.SHOW_ALL -> repo.asteroidsOfWeek
            OptionMenu.SHOW_TODAY->repo.asteroidOfToday
            else->repo.allAsteroids
        }
    }

    // Image of the Day VAriables
    private var _dayImage = MutableLiveData<ImageDay>()
    val dayImage:LiveData<ImageDay>
        get() = _dayImage

    init {
        _navigateToAsteroidDetail.value = null
        viewModelScope.launch {
            _dayImage.value=NasaImageApi.retrofitService.getImageDay(repo.API_KEY)
            repo.refreshAsteroid()
            // asteroids = asteroidsDao.getAllAsteroids()
        }
    }

    fun onAsteroidClicked(asteroidId:Long) {
        viewModelScope.launch {
            var asteroid = asteroids?.value?.get(0)
            for (a in asteroids.value!!) {
                if (a.id == asteroidId) asteroid = a
            }
            _navigateToAsteroidDetail.value = asteroid
        }
    }

    fun onAsteroidDetailNavigated(){
        _navigateToAsteroidDetail.value = null
    }
}