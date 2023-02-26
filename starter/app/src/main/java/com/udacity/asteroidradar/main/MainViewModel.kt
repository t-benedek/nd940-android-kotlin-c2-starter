package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaImageApi
import com.udacity.asteroidradar.api.NasaImageApiService
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

private const val API_KEY = "0SFEMaVSIupLNBuIH8qDZ5hhNKAkJflFeCro1Mmr"

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val asteroidList = listOf<Asteroid>(
        Asteroid(1, "BLING", "11",12.0,
            12.0, 12.0, 12.0, true),
        Asteroid(2, "BONG", "21",12.0,
        12.0, 12.0, 12.0, false),
        Asteroid(3, "BONG", "230",12.0,
        12.0, 12.0, 12.0, true),
        Asteroid(4, "BONG", "540",12.0,
            12.0, 12.0, 12.0, false),
        Asteroid(5, "BONG", "54",12.0,
            12.0, 12.0, 12.0, true),
        Asteroid(6, "BONG", "56",12.0,
            12.0, 12.0, 12.0, false),
        Asteroid(7, "BONG", "67",12.0,
            12.0, 12.0, 12.0, true),
        Asteroid(8, "BONG", "78",12.0,
            12.0, 12.0, 12.0, false),
        Asteroid(9, "BONG", "999",12.0,
            12.0, 12.0, 12.0, true)
    )

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
//        viewModelScope.launch {
//            asteroidRepository.refreshAsteroid()
//        }
        _navigateToAsteroidDetail.value = null
        viewModelScope.launch {
            try{
                _dayImageUrl.value=NasaImageApi.retrofitService.getImageDay(API_KEY).url
            } catch(exc:Exception){
                Log.e("MainViewModel",exc.message,exc)
            }
        }
        Log.i("MainViewModel", "LOG CALLING getDayImageURL\"")
    }

    val asteroids = MutableLiveData<List<Asteroid>>(asteroidList)

    fun onAsteroidClicked(asteroidId:Long) {
        viewModelScope.launch {
            _navigateToAsteroidDetail.value = asteroidList.get(1)
        }
    }

    fun onAsteroidDetailNavigated(){
        _navigateToAsteroidDetail.value = null
    }


}