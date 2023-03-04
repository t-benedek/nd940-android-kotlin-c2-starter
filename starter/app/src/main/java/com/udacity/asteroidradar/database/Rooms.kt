package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidsDao {

    @Query("select * from databaseasteroid")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(DatabaseAsteroid::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder (
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}

class DatabaseHelper {
    companion object o {
        fun toDatabaseAsteroid(a: Asteroid): DatabaseAsteroid {
            return DatabaseAsteroid(
                id = a.id,
                codename = a.codename,
                closeApproachDate = a.closeApproachDate,
                absoluteMagnitude = a.absoluteMagnitude,
                estimatedDiameter = a.estimatedDiameter,
                relativeVelocity = a.relativeVelocity,
                distanceFromEarth = a.distanceFromEarth,
                isPotentiallyHazardous = a.isPotentiallyHazardous
            )
        }
    }
}
