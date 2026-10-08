package com.example.data

import com.example.R

object SampleClipsProvider {
    fun getInitialClips(): List<ClipEntity> {
        return listOf(
            ClipEntity(
                id = "clip_podcast_1",
                title = "Sanaullah AI Secrets Podcast",
                videoUri = null,
                thumbnailDrawableName = "sample_podcast_frame_1791336810933",
                startTimeSec = 10,
                endTimeSec = 40,
                cropMode = "Smart AI Speaker Tracking",
                caption = "The wildest secret behind modern AI founders... Are we ready for what's next? 🤯 #sanaullah #studio #podcast #ai #tech #founder",
                hashtagsCsv = "#sanaullah,#fyp,#podcast,#aitools,#mindset,#viral,#techtrends",
                creatorName = "Sanaullah",
                creatorHandle = "@sanaullah.studio",
                soundTitle = "Deep Conversation Background - Sanaullah SoundLab",
                likesCount = 142800,
                commentsCount = 3892,
                sharesCount = 19400,
                isLiked = true,
                isSaved = false,
                createdAt = System.currentTimeMillis() - 3600000L
            ),
            ClipEntity(
                id = "clip_tech_2",
                title = "Futuristic Tech Unboxing 2035",
                videoUri = null,
                thumbnailDrawableName = "sample_tech_frame_1791336825993",
                startTimeSec = 5,
                endTimeSec = 35,
                cropMode = "Center Crop (9:16)",
                caption = "This is literally from 2035 😱 Would you use this in your daily setup? Check out the ambient glowing sensor! #sanaullah #gadgets #techreview",
                hashtagsCsv = "#sanaullah,#fyp,#gadgets,#unboxing,#tech,#futuretech,#gear",
                creatorName = "Sanaullah",
                creatorHandle = "@sanaullah.studio",
                soundTitle = "Cyber Synth Wave 2077 - AudioTrend",
                likesCount = 89400,
                commentsCount = 1520,
                sharesCount = 8210,
                isLiked = false,
                isSaved = true,
                createdAt = System.currentTimeMillis() - 7200000L
            ),
            ClipEntity(
                id = "clip_dance_3",
                title = "Downtown Freestyle Beat Drop",
                videoUri = null,
                thumbnailDrawableName = "sample_dance_frame_1791336840514",
                startTimeSec = 8,
                endTimeSec = 38,
                cropMode = "Smart AI Speaker Tracking",
                caption = "Wait for that switch-up on second 15 🔥 Tag a friend who thinks they got moves! #sanaullah #dance #energy #viralshorts #freestyle",
                hashtagsCsv = "#sanaullah,#fyp,#dancechallenge,#beatdrop,#streetstyle,#trending",
                creatorName = "Sanaullah",
                creatorHandle = "@sanaullah.studio",
                soundTitle = "Tokyo Drift Bass Boost (Sped Up) - ViralSounds",
                likesCount = 312000,
                commentsCount = 6420,
                sharesCount = 45100,
                isLiked = false,
                isSaved = false,
                createdAt = System.currentTimeMillis() - 14400000L
            ),
            ClipEntity(
                id = "clip_studio_4",
                title = "Sanaullah Studio Behind The Scenes",
                videoUri = null,
                thumbnailDrawableName = "sample_podcast_frame_1791336810933",
                startTimeSec = 0,
                endTimeSec = 30,
                cropMode = "Smart AI Speaker Tracking",
                caption = "Editing 4K vertical clips in 10 seconds with Sanaullah Studio AI engine! 🎬 #sanaullah #studio #editing #creatorlife",
                hashtagsCsv = "#sanaullah,#studio,#contentcreator,#videotools,#viral",
                creatorName = "Sanaullah",
                creatorHandle = "@sanaullah.studio",
                soundTitle = "Studio Production Beat - Sanaullah Studio",
                likesCount = 560000,
                commentsCount = 8900,
                sharesCount = 67000,
                isLiked = true,
                isSaved = true,
                createdAt = System.currentTimeMillis() - 28800000L
            ),
            ClipEntity(
                id = "clip_viral_5",
                title = "Next Gen Mobile Clip AI",
                videoUri = null,
                thumbnailDrawableName = "sample_tech_frame_1791336825993",
                startTimeSec = 4,
                endTimeSec = 34,
                cropMode = "Center Crop (9:16)",
                caption = "How Sanaullah Studio auto-crops speaker faces into 9:16 vertical shorts automatically! 🚀 #sanaullah #ai #videoedit",
                hashtagsCsv = "#sanaullah,#ai,#aivideo,#shorts,#reels,#tiktok",
                creatorName = "Sanaullah",
                creatorHandle = "@sanaullah.studio",
                soundTitle = "Future Bass Energy - Sanaullah Sounds",
                likesCount = 780000,
                commentsCount = 12400,
                sharesCount = 92000,
                isLiked = false,
                isSaved = true,
                createdAt = System.currentTimeMillis() - 43200000L
            ),
            ClipEntity(
                id = "clip_dance_6",
                title = "Neon Street Choreography",
                videoUri = null,
                thumbnailDrawableName = "sample_dance_frame_1791336840514",
                startTimeSec = 12,
                endTimeSec = 42,
                cropMode = "Smart AI Speaker Tracking",
                caption = "Night city freestyle session in 9:16 ratio! Check out the colors under neon lights 🌟 #sanaullah #dance #neoncity",
                hashtagsCsv = "#sanaullah,#dance,#nightcity,#freestyle,#trending",
                creatorName = "Sanaullah",
                creatorHandle = "@sanaullah.studio",
                soundTitle = "Club Anthem 2026 - Viral Sounds",
                likesCount = 420000,
                commentsCount = 5100,
                sharesCount = 38000,
                isLiked = true,
                isSaved = false,
                createdAt = System.currentTimeMillis() - 86400000L
            )
        )
    }

    fun getDrawableIdByName(name: String?): Int {
        return when (name) {
            "sample_podcast_frame_1791336810933" -> R.drawable.sample_podcast_frame_1791336810933
            "sample_tech_frame_1791336825993" -> R.drawable.sample_tech_frame_1791336825993
            "sample_dance_frame_1791336840514" -> R.drawable.sample_dance_frame_1791336840514
            else -> R.drawable.tiktok_studio_icon_1791336696905
        }
    }
}
