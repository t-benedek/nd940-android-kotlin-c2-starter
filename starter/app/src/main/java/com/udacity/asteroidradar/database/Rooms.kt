package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidsDao {


    @Query("select * from databaseasteroid ORDER BY closeApproachDate DESC")
    suspend fun getAllAsteroids(): List<DatabaseAsteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Suppress("RedundantSuspendModifier")
    suspend fun insert(asteroid: DatabaseAsteroid)
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

        fun toAsteroidFromDatabase(inList: List<DatabaseAsteroid>): List<Asteroid> {
            var retList = ArrayList<Asteroid>()
            for (a in inList) {
                retList.add(
                    Asteroid(
                        id = a.id,
                        codename = a.codename,
                        closeApproachDate = a.closeApproachDate,
                        absoluteMagnitude = a.absoluteMagnitude,
                        estimatedDiameter = a.estimatedDiameter,
                        relativeVelocity = a.relativeVelocity,
                        distanceFromEarth = a.distanceFromEarth,
                        isPotentiallyHazardous = a.isPotentiallyHazardous
                    )
                )
            }
            return retList
        }
    }
}
