package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
    @Query("SELECT * FROM clips ORDER BY createdAt DESC")
    fun getAllClips(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE id = :id LIMIT 1")
    suspend fun getClipById(id: String): ClipEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: ClipEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clips: List<ClipEntity>)

    @Update
    suspend fun updateClip(clip: ClipEntity)

    @Delete
    suspend fun deleteClip(clip: ClipEntity)

    @Query("UPDATE clips SET isLiked = :isLiked, likesCount = :likesCount WHERE id = :id")
    suspend fun updateLiked(id: String, isLiked: Boolean, likesCount: Int)

    @Query("UPDATE clips SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSaved(id: String, isSaved: Boolean)
}
