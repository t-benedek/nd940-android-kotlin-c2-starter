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
        Asteroid(2, "BONG", "11",12.0,
        12.0, 12.0, 12.0, true),
        Asteroid(2, "BONG", "11",12.0,
        12.0, 12.0, 12.0, true)
    )

    enum class AsteroidStatus{LOADING,ERROR, DONE }
    private var _navigateToAsteroidDetail=MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail:LiveData<Asteroid?>
        get() = _navigateToAsteroidDetail

    // private val asteroidRepository = AsteroidRepository(database)
    var optionMenu = MutableLiveData(OptionMenu.SHOW_WEEK)

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


}