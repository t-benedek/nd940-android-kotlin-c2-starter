package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Asteroid constructor (
    @PrimaryKey
    val id: Long,
    val codename:  String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth:Double,
    val isPotentiallyHazardous: Boolean) : Parcelable

