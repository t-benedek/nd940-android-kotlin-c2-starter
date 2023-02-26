package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
// import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.time.LocalDate

private const val BASE_URL = "https://api.nasa.gov/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .baseUrl(BASE_URL)
//    .build()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface NasaImageApiService {

    @GET("planetary/apod")
    suspend fun getImageDay(@Query("api_key") apiKey: String): ImageDay

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids (
        @Query("start_date") startDate:String,
        @Query("end_date") endDate:String,
        @Query("api_key") apiKey: String
    ): String
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object NasaImageApi {
    val retrofitService : NasaImageApiService by lazy { retrofit.create(NasaImageApiService::class.java) }
}

