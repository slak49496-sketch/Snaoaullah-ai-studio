package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clips")
data class ClipEntity(
    @PrimaryKey val id: String,
    val title: String,
    val videoUri: String?,
    val thumbnailDrawableName: String?,
    val startTimeSec: Int,
    val endTimeSec: Int,
    val cropMode: String,
    val caption: String,
    val hashtagsCsv: String,
    val creatorName: String,
    val creatorHandle: String,
    val soundTitle: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
