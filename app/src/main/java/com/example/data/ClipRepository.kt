package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ClipRepository(private val clipDao: ClipDao) {
    val allClips: Flow<List<ClipEntity>> = clipDao.getAllClips()

    suspend fun ensureInitialData() {
        val existing = clipDao.getAllClips().first()
        if (existing.isEmpty()) {
            clipDao.insertAll(SampleClipsProvider.getInitialClips())
        }
    }

    suspend fun saveClip(clip: ClipEntity) {
        clipDao.insertClip(clip)
    }

    suspend fun toggleLike(clipId: String, currentLiked: Boolean, currentCount: Int) {
        val newLiked = !currentLiked
        val newCount = if (newLiked) currentCount + 1 else (currentCount - 1).coerceAtLeast(0)
        clipDao.updateLiked(clipId, newLiked, newCount)
    }

    suspend fun toggleSave(clipId: String, currentSaved: Boolean) {
        clipDao.updateSaved(clipId, !currentSaved)
    }

    suspend fun deleteClip(clip: ClipEntity) {
        clipDao.deleteClip(clip)
    }
}
