package com.example.goalscheck.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "tableGoals")
data class GoalsEntity(
    @PrimaryKey(autoGenerate = true)
    var goalsId : Int = 0,
    var goalsName : String? = "",
    var goalsTarget : String? = "",
    var goalsDatetime : String? = "",
    var goalsDescription : String? = "",
    var goalsCheck : Boolean
)