package com.example.goalscheck.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(
    entities = [ActivitiesEntity::class, GoalsEntity::class],
    exportSchema = false,
    version = 1
)
abstract class DatabaseRoom : RoomDatabase() {
    abstract fun dbRoomDao() : DbRoomDao
    companion object {
        @Volatile private var instance: DatabaseRoom? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
            DatabaseRoom::class.java, "GoalsCheck.db")
                .allowMainThreadQueries()
                .build()
    }
}