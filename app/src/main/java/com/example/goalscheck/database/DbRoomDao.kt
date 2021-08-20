package com.example.goalscheck.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DbRoomDao {
    @Insert
    suspend fun insertActivity(activitiesEntity: ActivitiesEntity) : Long

    @Insert
    suspend fun insertGoals(goalsEntity: GoalsEntity) : Long

    @Update
    suspend fun updateActivity(activitiesEntity: ActivitiesEntity) : Int

    @Update
    suspend fun updateGoals(goalsEntity: GoalsEntity) :Int

    @Delete
    suspend fun deleteActivity(activitiesEntity: ActivitiesEntity) : Int

    @Delete
    suspend fun deleteGoals(goalsEntity: GoalsEntity) : Int

    @Query("SELECT * FROM tableActivities")
    fun listActivities() : LiveData<List<ActivitiesEntity>>

    @Query("SELECT * FROM tableGoals")
    fun listGoals() : LiveData<List<GoalsEntity>>
}