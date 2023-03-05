package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidsDao {

    @Query("select * from databaseasteroid ORDER BY closeApproachDate DESC")
    suspend fun getAllAsteroids(): List<DatabaseAsteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Suppress("RedundantSuspendModifier")
    suspend fun insert(asteroid: DatabaseAsteroid)

    @Query("DELETE FROM databaseasteroid WHERE closeApproachDate<:today")
    suspend fun deletePreviousAsteroid(today:String)

    @Query("SELECT * FROM databaseasteroid WHERE id=:id")
    suspend fun getAsteroid(id:Long): Asteroid?

    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate BETWEEN :date and :end_date ORDER BY closeApproachDate")
    fun getAsteroidsByDate(date:String, end_date:String):LiveData<List<Asteroid>?>

    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate = :date")
    fun getAsteroidOfToday(date:String):LiveData<List<Asteroid>?>
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
