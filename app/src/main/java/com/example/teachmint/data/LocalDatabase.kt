package com.example.teachmint.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.teachmint.data.LocalDao
import com.example.teachmint.data.model.LocalModel


@Database(entities = [LocalModel::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localDao(): LocalDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "local_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
