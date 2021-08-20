package com.example.goalscheck.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tableActivities")
data class ActivitiesEntity(
    @PrimaryKey(autoGenerate = true)
    var activityId: Int = 0,
    var activityName: String? = "",
    var activityDate : String? = "",
    var dateCreated : String? = "",
    var checked : Boolean

)
