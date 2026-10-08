package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiGeneratedClipResult
import com.example.ai.GeminiAiService
import com.example.data.AppDatabase
import com.example.data.ClipEntity
import com.example.data.ClipRepository
import com.example.data.SampleClipsProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    STUDIO,
    FEED,
    CAMERA,
    PROFILE,
    SAVED,
    SETTINGS
}

data class StudioUiState(
    val currentScreen: AppScreen = AppScreen.STUDIO,
    val isDarkMode: Boolean = true, // White and Dark theme support
    val showLoginModal: Boolean = false,
    val selectedVideoUri: Uri? = null,
    val selectedDemoSampleIndex: Int = 0,
    val videoFileName: String = "sanaullah_podcast_ep1.mp4",
    val videoDurationSec: Int = 180,
    val startTimeSec: Int = 10,
    val endTimeSec: Int = 40,
    val cropMode: String = "Center Crop (9:16)",
    val topicText: String = "Sanaullah Studio AI Insights",
    val tone: String = "Engaging & Viral",
    val isProcessing: Boolean = false,
    val isPlayingPreview: Boolean = true,
    val previewCurrentPosSec: Int = 10,
    val generatedResult: AiGeneratedClipResult? = null,
    val showClipGeneratedDialog: Boolean = false,
    val toastMessage: String? = null,
    val isSpeakerGridOverlayVisible: Boolean = true,

    // Feed state
    val feedClips: List<ClipEntity> = emptyList(),
    val currentFeedIndex: Int = 0,
    val isFeedPlaying: Boolean = true,
    val feedTab: String = "For You",
    val showTranslation: Boolean = false,
    val activeCommentsClipId: String? = null,
    val activeShareClipId: String? = null,
    val commentsList: List<Pair<String, String>> = listOf(
        Pair("@sarah_creates", "Sanaullah Studio AI tracking is crazy good! 🔥"),
        Pair("@tech_pulse", "Need more 9:16 clips like this on my FYP"),
        Pair("@growth_hacker", "Best video trimmer for shorts 🚀"),
        Pair("@vlog_queen", "What audio track is this??")
    ),

    // Camera state
    val isRecording: Boolean = false,
    val recordedDurationSec: Int = 0,
    val selectedCameraSound: String = "Trending Sound - Sanaullah SoundLab",
    val isFrontCamera: Boolean = true,
    val cameraSpeed: Float = 1.0f,
    val isBeautyOn: Boolean = true,
    val cameraTimerSec: Int = 0,
    val isFlashOn: Boolean = false,
    val activeCameraFilter: String = "Cyber Glow",

    // User / Profile state matching screenshot
    val isSignedIn: Boolean = true,
    val userName: String = "Sanaullah",
    val userHandle: String = "@sanaullah.studio",
    val userBio: String = "Creator & Founder of Sanaullah Studio 🚀 | 9:16 AI Shorts & Reels Editor",
    val followersCount: String = "3M",
    val followingCount: String = "1.2M",
    val likesTotalCount: String = "12.8M",
    val userEmail: String = "sanaullah@studio.ai",
    val authProvider: String = "Google Account",
    val profileTab: Int = 0 // 0: Video Grid, 1: Liked Videos, 2: Saved/Private
)

class TikTokStudioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ClipRepository
    private val _uiState = MutableStateFlow(StudioUiState())
    val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()

    private var previewPlaybackJob: Job? = null
    private var recordingTimerJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ClipRepository(db.clipDao())

        viewModelScope.launch {
            repository.ensureInitialData()
            repository.allClips.collect { clips ->
                _uiState.update { it.copy(feedClips = clips) }
            }
        }

        startPreviewPlaybackLoop()
    }

    fun setScreen(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
        if (screen == AppScreen.STUDIO) {
            startPreviewPlaybackLoop()
        } else {
            previewPlaybackJob?.cancel()
        }
    }

    // Theme Management (White & Dark)
    fun toggleTheme() {
        _uiState.update {
            val newDark = !it.isDarkMode
            it.copy(
                isDarkMode = newDark,
                toastMessage = if (newDark) "Switched to Dark Theme 🌙" else "Switched to White / Light Theme ☀️"
            )
        }
    }

    fun setTheme(isDark: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDark) }
    }

    // Login & Auth Options (White & Dark supported)
    fun openLoginModal() {
        _uiState.update { it.copy(showLoginModal = true) }
    }

    fun closeLoginModal() {
        _uiState.update { it.copy(showLoginModal = false) }
    }

    fun signInWithGoogle(email: String = "sanaullah@gmail.com", name: String = "Sanaullah") {
        _uiState.update {
            it.copy(
                isSignedIn = true,
                userName = name,
                userHandle = "@${name.lowercase().replace(" ", "")}.studio",
                userEmail = email,
                authProvider = "Google Account",
                showLoginModal = false,
                toastMessage = "Signed in with Google as $name ✅"
            )
        }
    }

    fun signInWithTikTok(handle: String = "@sanaullah.tiktok") {
        _uiState.update {
            it.copy(
                isSignedIn = true,
                userName = "Sanaullah",
                userHandle = handle,
                authProvider = "TikTok Account",
                showLoginModal = false,
                toastMessage = "Connected with TikTok account $handle 🎵"
            )
        }
    }

    fun signInWithEmail(email: String) {
        val derivedName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        _uiState.update {
            it.copy(
                isSignedIn = true,
                userName = if (derivedName.isNotBlank()) derivedName else "Sanaullah",
                userHandle = "@${derivedName.lowercase()}",
                userEmail = email,
                authProvider = "Email / Password",
                showLoginModal = false,
                toastMessage = "Logged in successfully! 🌟"
            )
        }
    }

    fun signOut() {
        _uiState.update {
            it.copy(
                isSignedIn = false,
                userName = "Guest User",
                userHandle = "@guest",
                userEmail = "guest@studio.local",
                authProvider = "None",
                showLoginModal = false,
                toastMessage = "Signed out"
            )
        }
    }

    fun setProfileTab(tab: Int) {
        _uiState.update { it.copy(profileTab = tab) }
    }

    fun selectDemoSample(index: Int) {
        val sampleNames = listOf("sanaullah_podcast_ep1.mp4", "sanaullah_tech_review.mp4", "sanaullah_dance_session.mp4")
        val sampleTopics = listOf("Sanaullah Studio AI Insights", "Next-Gen 2035 Tech Reveal", "Street Beat Freestyle")
        val fileName = sampleNames.getOrElse(index) { "sample_video.mp4" }
        val topic = sampleTopics.getOrElse(index) { "Viral Clip" }

        _uiState.update {
            it.copy(
                selectedVideoUri = null,
                selectedDemoSampleIndex = index,
                videoFileName = fileName,
                topicText = topic,
                startTimeSec = 10,
                endTimeSec = 40,
                previewCurrentPosSec = 10
            )
        }
    }

    fun setVideoUri(uri: Uri, fileName: String? = null) {
        val name = fileName ?: (uri.lastPathSegment ?: "user_video.mp4")
        _uiState.update {
            it.copy(
                selectedVideoUri = uri,
                videoFileName = name,
                videoDurationSec = 60,
                startTimeSec = 0,
                endTimeSec = 30,
                previewCurrentPosSec = 0
            )
        }
    }

    fun updateTrimBounds(start: Int, end: Int) {
        val s = start.coerceAtLeast(0)
        val e = end.coerceAtLeast(s + 1)
        _uiState.update {
            it.copy(
                startTimeSec = s,
                endTimeSec = e,
                previewCurrentPosSec = s
            )
        }
    }

    fun setQuickPresetDuration(seconds: Int) {
        val start = _uiState.value.startTimeSec
        val end = (start + seconds).coerceAtMost(_uiState.value.videoDurationSec)
        _uiState.update { it.copy(endTimeSec = end, previewCurrentPosSec = start) }
    }

    fun setCropMode(mode: String) {
        _uiState.update { it.copy(cropMode = mode) }
    }

    fun setTopic(topic: String) {
        _uiState.update { it.copy(topicText = topic) }
    }

    fun setTone(tone: String) {
        _uiState.update { it.copy(tone = tone) }
    }

    fun togglePreviewPlayback() {
        _uiState.update { it.copy(isPlayingPreview = !it.isPlayingPreview) }
    }

    fun toggleGridOverlay() {
        _uiState.update { it.copy(isSpeakerGridOverlayVisible = !it.isSpeakerGridOverlayVisible) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun startPreviewPlaybackLoop() {
        previewPlaybackJob?.cancel()
        previewPlaybackJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_uiState.value.isPlayingPreview && _uiState.value.currentScreen == AppScreen.STUDIO) {
                    val current = _uiState.value.previewCurrentPosSec
                    val start = _uiState.value.startTimeSec
                    val end = _uiState.value.endTimeSec
                    val next = if (current >= end) start else current + 1
                    _uiState.update { it.copy(previewCurrentPosSec = next) }
                }
            }
        }
    }

    fun processVideoAndGenerateClip() {
        val state = _uiState.value
        val topic = state.topicText.ifBlank { "Viral Clip" }
        val duration = (state.endTimeSec - state.startTimeSec).coerceAtLeast(5)

        _uiState.update { it.copy(isProcessing = true) }

        viewModelScope.launch {
            val aiResult = GeminiAiService.generateViralClipMetadata(
                topic = topic,
                durationSeconds = duration,
                cropMode = state.cropMode,
                tone = state.tone
            )

            val sampleThumbnails = listOf(
                "sample_podcast_frame_1791336810933",
                "sample_tech_frame_1791336825993",
                "sample_dance_frame_1791336840514"
            )
            val thumb = if (state.selectedVideoUri == null) {
                sampleThumbnails.getOrElse(state.selectedDemoSampleIndex) { sampleThumbnails[0] }
            } else {
                null
            }

            val newClip = ClipEntity(
                id = UUID.randomUUID().toString(),
                title = topic,
                videoUri = state.selectedVideoUri?.toString(),
                thumbnailDrawableName = thumb,
                startTimeSec = state.startTimeSec,
                endTimeSec = state.endTimeSec,
                cropMode = state.cropMode,
                caption = aiResult.caption,
                hashtagsCsv = aiResult.hashtags.joinToString(","),
                creatorName = state.userName,
                creatorHandle = state.userHandle,
                soundTitle = aiResult.soundSuggestion,
                likesCount = 1,
                commentsCount = 0,
                sharesCount = 0,
                isLiked = true,
                isSaved = true
            )

            repository.saveClip(newClip)

            _uiState.update {
                it.copy(
                    isProcessing = false,
                    generatedResult = aiResult,
                    showClipGeneratedDialog = true,
                    toastMessage = "Clip created & saved to Sanaullah Studio! 🎉"
                )
            }
        }
    }

    fun dismissGeneratedDialog() {
        _uiState.update { it.copy(showClipGeneratedDialog = false) }
    }

    // Feed Interactions
    fun nextFeedClip() {
        val total = _uiState.value.feedClips.size
        if (total > 0) {
            val next = (_uiState.value.currentFeedIndex + 1) % total
            _uiState.update { it.copy(currentFeedIndex = next) }
        }
    }

    fun previousFeedClip() {
        val total = _uiState.value.feedClips.size
        if (total > 0) {
            val prev = if (_uiState.value.currentFeedIndex == 0) total - 1 else _uiState.value.currentFeedIndex - 1
            _uiState.update { it.copy(currentFeedIndex = prev) }
        }
    }

    fun toggleFeedPlayback() {
        _uiState.update { it.copy(isFeedPlaying = !it.isFeedPlaying) }
    }

    fun toggleFeedLike(clip: ClipEntity) {
        viewModelScope.launch {
            repository.toggleLike(clip.id, clip.isLiked, clip.likesCount)
        }
    }

    fun toggleFeedSave(clip: ClipEntity) {
        viewModelScope.launch {
            repository.toggleSave(clip.id, clip.isSaved)
            _uiState.update {
                it.copy(toastMessage = if (!clip.isSaved) "Saved to your bookmarks! 🔖" else "Removed from bookmarks")
            }
        }
    }

    fun setFeedTab(tab: String) {
        _uiState.update { it.copy(feedTab = tab) }
    }

    fun toggleTranslation() {
        _uiState.update { it.copy(showTranslation = !it.showTranslation) }
    }

    fun openCommentsSheet(clipId: String) {
        _uiState.update { it.copy(activeCommentsClipId = clipId) }
    }

    fun closeCommentsSheet() {
        _uiState.update { it.copy(activeCommentsClipId = null) }
    }

    fun addComment(text: String) {
        if (text.isNotBlank()) {
            _uiState.update {
                it.copy(
                    commentsList = listOf(Pair(it.userHandle, text)) + it.commentsList,
                    toastMessage = "Comment posted! 💬"
                )
            }
        }
    }

    fun openShareSheet(clipId: String) {
        _uiState.update { it.copy(activeShareClipId = clipId) }
    }

    fun closeShareSheet() {
        _uiState.update { it.copy(activeShareClipId = null) }
    }

    // Camera Interactions
    fun toggleRecording() {
        val isCurrentlyRecording = _uiState.value.isRecording
        if (!isCurrentlyRecording) {
            _uiState.update { it.copy(isRecording = true, recordedDurationSec = 0) }
            recordingTimerJob?.cancel()
            recordingTimerJob = viewModelScope.launch {
                while (_uiState.value.isRecording) {
                    delay(1000)
                    val nextSec = _uiState.value.recordedDurationSec + 1
                    if (nextSec >= 60) {
                        finishRecording()
                        break
                    } else {
                        _uiState.update { it.copy(recordedDurationSec = nextSec) }
                    }
                }
            }
        } else {
            finishRecording()
        }
    }

    private fun finishRecording() {
        recordingTimerJob?.cancel()
        val duration = _uiState.value.recordedDurationSec.coerceAtLeast(3)
        _uiState.update {
            it.copy(
                isRecording = false,
                toastMessage = "Recorded ${duration}s clip! Loaded into Sanaullah Studio.",
                currentScreen = AppScreen.STUDIO,
                videoFileName = "camera_recording_${System.currentTimeMillis()}.mp4",
                startTimeSec = 0,
                endTimeSec = duration,
                topicText = "Live Camera Recording"
            )
        }
    }

    fun flipCamera() {
        _uiState.update { it.copy(isFrontCamera = !it.isFrontCamera) }
    }

    fun cycleSpeed() {
        val speeds = listOf(0.5f, 1.0f, 2.0f, 3.0f)
        val currentIndex = speeds.indexOf(_uiState.value.cameraSpeed)
        val next = speeds[(currentIndex + 1) % speeds.size]
        _uiState.update { it.copy(cameraSpeed = next) }
    }

    fun toggleBeauty() {
        _uiState.update { it.copy(isBeautyOn = !it.isBeautyOn) }
    }

    fun cycleTimer() {
        val timers = listOf(0, 3, 10)
        val currentIndex = timers.indexOf(_uiState.value.cameraTimerSec)
        val next = timers[(currentIndex + 1) % timers.size]
        _uiState.update { it.copy(cameraTimerSec = next) }
    }

    fun toggleFlash() {
        _uiState.update { it.copy(isFlashOn = !it.isFlashOn) }
    }

    fun cycleFilter() {
        val filters = listOf("Normal", "Cyber Glow", "Vintage Film", "Golden Hour", "Neon Noir")
        val currentIndex = filters.indexOf(_uiState.value.activeCameraFilter)
        val next = filters[(currentIndex + 1) % filters.size]
        _uiState.update { it.copy(activeCameraFilter = next) }
    }

    fun deleteClip(clip: ClipEntity) {
        viewModelScope.launch {
            repository.deleteClip(clip)
            _uiState.update { it.copy(toastMessage = "Clip deleted") }
        }
    }
}
