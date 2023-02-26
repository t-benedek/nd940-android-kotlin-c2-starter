package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaImageApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import okhttp3.Response
import org.json.JSONObject

private const val API_KEY = "0SFEMaVSIupLNBuIH8qDZ5hhNKAkJflFeCro1Mmr"

class MainViewModel(application: Application) : AndroidViewModel(application){
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
            try{
                _dayImageUrl.value=NasaImageApi.retrofitService.getImageDay(API_KEY).url
                val response = NasaImageApi.retrofitService.getAsteroids (
                    "2023-01-08",
                    "2023-01-09",
                    API_KEY)
                val jsonObject = JSONObject(response)
                val list = parseAsteroidsJsonResult(jsonObject)

                _asteroidList.value = list
                val size = _asteroidList?.value?.size
            } catch(exc:Exception){
                Log.e("MainViewModel",exc.message,exc)
            }
        }
        Log.i("MainViewModel", "LOG CALLING getDayImageURL\"")
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