package com.example.goalscheck.repo

import com.example.goalscheck.database.ActivitiesEntity
import com.example.goalscheck.database.DatabaseRoom
//import com.example.goalscheck.database.DatabaseRoom
import com.example.goalscheck.database.GoalsEntity

class Repository (
    private val db : DatabaseRoom)
{
    suspend fun saveActivity(activitiesEntity: ActivitiesEntity):Long {
        return db.dbRoomDao().insertActivity(activitiesEntity)
    }

    suspend fun saveGoals(goalsEntity: GoalsEntity) : Long {
        return db.dbRoomDao().insertGoals(goalsEntity)
    }

    suspend fun updateActivities(activitiesEntity: ActivitiesEntity): Int {
        return db.dbRoomDao().updateActivity(activitiesEntity)
    }

    suspend fun updateGoals(goalsEntity: GoalsEntity) : Int {
        return db.dbRoomDao().updateGoals(goalsEntity)
    }

    suspend fun deleteActivities(activitiesEntity: ActivitiesEntity) : Int{
        return db.dbRoomDao().deleteActivity(activitiesEntity)
    }

    suspend fun deleteGoals(goalsEntity: GoalsEntity) : Int {
        return db.dbRoomDao().deleteGoals(goalsEntity)
    }

    val getActivity = db.dbRoomDao().listActivities()

    val getGoals = db.dbRoomDao().listGoals()


}