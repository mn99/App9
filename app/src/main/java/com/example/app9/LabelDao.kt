package com.example.app9

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LabelDao {

    @Insert
    suspend fun insertLabel(label: Label)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabels(labels: List<Label>)

    @Query("SELECT value FROM Label ORDER BY value")
    suspend fun getAllLabelsAsList(): List<String>

    @Query("SELECT * FROM Label ORDER BY value")
    fun getAllLabels(): LiveData<List<Label>>
}