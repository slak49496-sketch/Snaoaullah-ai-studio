package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ClipEntity
import com.example.data.SampleClipsProvider
import com.example.ui.StudioUiState
import com.example.ui.TikTokStudioViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    uiState: StudioUiState,
    viewModel: TikTokStudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val clips = uiState.feedClips
    val currentClip = clips.getOrNull(uiState.currentFeedIndex)

    var showHeartAnimation by remember { mutableStateOf(false) }

    // Vinyl Record infinite rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "Vinyl")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (currentClip != null) {
                            if (!currentClip.isLiked) {
                                viewModel.toggleFeedLike(currentClip)
                            }
                            showHeartAnimation = true
                            coroutineScope.launch {
                                delay(800)
                                showHeartAnimation = false
                            }
                        }
                    },
                    onTap = {
                        viewModel.toggleFeedPlayback()
                    }
                )
            }
    ) {
        if (currentClip != null) {
            // Full-screen Video / Visual Content
            val drawableRes = SampleClipsProvider.getDrawableIdByName(currentClip.thumbnailDrawableName)
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = currentClip.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Top gradient overlay for header readability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                        )
                    )
            )

            // Bottom gradient overlay for caption readability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
            )

            // Top Tabs: "Following | For You" (matching screenshot: Mockup | for you)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Following",
                    fontSize = 15.sp,
                    fontWeight = if (uiState.feedTab == "Following") FontWeight.Bold else FontWeight.Normal,
                    color = if (uiState.feedTab == "Following") Color.White else Slate400,
                    modifier = Modifier
                        .clickable { viewModel.setFeedTab("Following") }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )

                Text(
                    text = "|",
                    fontSize = 13.sp,
                    color = Slate700,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { viewModel.setFeedTab("For You") }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "For You",
                        fontSize = 16.sp,
                        fontWeight = if (uiState.feedTab == "For You") FontWeight.Bold else FontWeight.Normal,
                        color = if (uiState.feedTab == "For You") Color.White else Slate400
                    )
                    if (uiState.feedTab == "For You") {
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(28.dp)
                                .height(2.5.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White)
                        )
                    }
                }
            }

            // Up/Down navigation floating buttons for quick switching in emulator
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { viewModel.previousFeedClip() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Previous Clip",
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = { viewModel.nextFeedClip() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Next Clip",
                        tint = Color.White
                    )
                }
            }

            // Right Vertical Action Bar (matching user's screenshot exactly!)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Creator Avatar with pink (+) follow badge
                Box(
                    modifier = Modifier.size(50.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                            .background(Indigo600),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentClip.creatorName.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    // Pink Follow (+) badge
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.BottomCenter)
                            .clip(CircleShape)
                            .background(TikTokPink)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Follow",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Like Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { viewModel.toggleFeedLike(currentClip) }
                ) {
                    Icon(
                        imageVector = if (currentClip.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (currentClip.isLiked) TikTokPink else Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("feed_like_button")
                    )
                    Text(
                        text = formatMetric(currentClip.likesCount),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Comment Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { viewModel.openCommentsSheet(currentClip.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Sms,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("feed_comment_button")
                    )
                    Text(
                        text = formatMetric(currentClip.commentsCount),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Bookmark / Save Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { viewModel.toggleFeedSave(currentClip) }
                ) {
                    Icon(
                        imageVector = if (currentClip.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Bookmark",
                        tint = if (currentClip.isSaved) TikTokCyan else Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                    Text(
                        text = if (currentClip.isSaved) "Saved" else "Save",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Share Button (curved reply arrow)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { viewModel.openShareSheet(currentClip.id) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Reply,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier
                            .size(34.dp)
                            .scale(scaleX = -1f, scaleY = 1f) // Mirror to look like TikTok curved share arrow
                    )
                    Text(
                        text = formatMetric(currentClip.sharesCount),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Spinning Vinyl Disc with music note
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Slate900)
                        .border(4.dp, Slate800, CircleShape)
                        .rotate(if (uiState.isFeedPlaying) rotationAngle else 0f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(TikTokPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Music",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Bottom-Left Overlay: Creator handle, Caption, Translation, Sound ticker
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(0.78f)
                    .padding(start = 16.dp, bottom = 64.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Creator Handle & Timestamp
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = currentClip.creatorHandle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "• 2h ago",
                        fontSize = 12.sp,
                        color = Slate400
                    )
                }

                // Caption with hashtags
                Text(
                    text = currentClip.caption,
                    fontSize = 13.sp,
                    color = Slate100,
                    lineHeight = 18.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                // "See translation"
                Row(
                    modifier = Modifier.clickable { viewModel.toggleTranslation() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.showTranslation) "See original" else "See translation",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate300
                    )
                }

                // Music Sound Ticker
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Sound",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = currentClip.soundTitle,
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Center Play Icon when paused
            if (!uiState.isFeedPlaying) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Paused",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            // Double Tap Heart Explosion Animation
            AnimatedVisibility(
                visible = showHeartAnimation,
                enter = scaleIn(initialScale = 0.3f, animationSpec = tween(200, easing = FastOutSlowInEasing)) + fadeIn(),
                exit = scaleOut(targetScale = 1.3f, animationSpec = tween(300)) + fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Heart Animation",
                    tint = TikTokPink,
                    modifier = Modifier.size(90.dp)
                )
            }
        } else {
            // Empty State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("No clips in feed yet", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Create a clip in the Studio to watch it here!", color = Slate400, fontSize = 13.sp)
                }
            }
        }
    }

    // Comments Modal Bottom Sheet
    if (uiState.activeCommentsClipId != null) {
        val isDark = uiState.isDarkMode
        val sheetColor = if (isDark) Slate900 else Color.White
        val sheetTextColor = if (isDark) Color.White else Color(0xFF0F172A)
        val sheetSubColor = if (isDark) Slate400 else Color(0xFF64748B)
        val inputBg = if (isDark) Slate950 else Color(0xFFF1F5F9)
        val inputBorder = if (isDark) Slate800 else Color(0xFFCBD5E1)

        var newCommentText by remember { mutableStateOf("") }
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeCommentsSheet() },
            containerColor = sheetColor,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(380.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${uiState.commentsList.size} comments",
                        color = sheetTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    IconButton(onClick = { viewModel.closeCommentsSheet() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = sheetSubColor)
                    }
                }

                // Comments list
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.commentsList.forEach { (user, text) ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Indigo600),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(user.take(2).uppercase().replace("@", ""), fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(user, color = sheetSubColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                Text(text, color = sheetTextColor, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // Add comment row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        placeholder = { Text("Add a comment...", color = sheetSubColor, fontSize = 13.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = inputBg,
                            unfocusedContainerColor = inputBg,
                            focusedBorderColor = TikTokPink,
                            unfocusedBorderColor = inputBorder,
                            focusedTextColor = sheetTextColor,
                            unfocusedTextColor = sheetTextColor
                        ),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (newCommentText.isNotBlank()) {
                                viewModel.addComment(newCommentText)
                                newCommentText = ""
                            }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(TikTokPink)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Post", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }

    // Share Modal Bottom Sheet
    if (uiState.activeShareClipId != null) {
        val isDark = uiState.isDarkMode
        val sheetColor = if (isDark) Slate900 else Color.White
        val sheetTextColor = if (isDark) Color.White else Color(0xFF0F172A)
        val sheetSubColor = if (isDark) Slate400 else Color(0xFF64748B)

        ModalBottomSheet(
            onDismissRequest = { viewModel.closeShareSheet() },
            containerColor = sheetColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Share Sanaullah Studio Clip",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = sheetTextColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val shareTargets = listOf(
                        Triple("Copy Link", Icons.AutoMirrored.Filled.Reply, TikTokCyan),
                        Triple("WhatsApp", Icons.Default.Sms, EmeraldGreen),
                        Triple("Instagram", Icons.Default.Favorite, TikTokPink),
                        Triple("More", Icons.Default.Share, Indigo500)
                    )

                    shareTargets.forEach { (label, icon, color) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.clickable {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Check out this clip by Sanaullah: https://tiktok.com/@sanaullah.studio/video/${uiState.activeShareClipId}")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Clip"))
                                viewModel.closeShareSheet()
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(color.copy(alpha = 0.15f))
                                    .border(1.dp, color.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(22.dp))
                            }
                            Text(label, color = sheetSubColor, fontSize = 11.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

private fun formatMetric(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}
