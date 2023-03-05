package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaImageApi
import com.udacity.asteroidradar.database.DatabaseHelper
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepo
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val asteroidsDao = getDatabase(application).asteroidsDao
    private val repo = AsteroidRepo(asteroidsDao)

    private var _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroids:LiveData<List<Asteroid>>
        get() = _asteroidList

    enum class AsteroidStatus{LOADING,ERROR, DONE }

    // Navigation to Details status
    private var _navigateToAsteroidDetail=MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail:LiveData<Asteroid?>
        get() = _navigateToAsteroidDetail

    // private val asteroidRepository = AsteroidRepository(database)
    var optionMenu = MutableLiveData(OptionMenu.SHOW_WEEK)

    // Image of the Day VAriables
    private var _dayImageUrl = MutableLiveData<String>()
    val dayImageUrl:LiveData<String>
        get() = _dayImageUrl

    enum class OptionMenu { SHOW_ALL, SHOW_TODAY, SHOW_WEEK }
    private val _status = MutableLiveData<AsteroidStatus>()
    val status: LiveData<AsteroidStatus>
        get()=_status

    init {

        _navigateToAsteroidDetail.value = null
        viewModelScope.launch {
            _dayImageUrl.value=NasaImageApi.retrofitService.getImageDay(repo.API_KEY).url
            repo.refreshAsteroid()
            _asteroidList.value = DatabaseHelper.toAsteroidFromDatabase(asteroidsDao.getAllAsteroids())
        }
    }

    fun onAsteroidClicked(asteroidId:Long) {
        viewModelScope.launch {
            var asteroid = _asteroidList?.value?.get(0)
            for (a in _asteroidList.value!!) {
                if (a.id == asteroidId) asteroid = a
            }
            _navigateToAsteroidDetail.value = asteroid
        }
    }

    fun onAsteroidDetailNavigated(){
        _navigateToAsteroidDetail.value = null
    }
}