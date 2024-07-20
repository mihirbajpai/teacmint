package com.example.teachmint.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.teachmint.data.model.LocalModel


@Dao
interface LocalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(repo: LocalModel)

    @Query("SELECT * FROM repo_table")
    suspend fun getAllData(): List<LocalModel>

    @Query("SELECT COUNT(*) FROM repo_table")
    suspend fun getCount(): Int
}