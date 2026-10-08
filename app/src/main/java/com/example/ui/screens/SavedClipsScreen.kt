package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ClipEntity
import com.example.data.SampleClipsProvider
import com.example.ui.AppScreen
import com.example.ui.StudioUiState
import com.example.ui.TikTokStudioViewModel
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink

@Composable
fun SavedClipsScreen(
    uiState: StudioUiState,
    viewModel: TikTokStudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clips = uiState.feedClips

    val isDark = uiState.isDarkMode
    val bgColor = if (isDark) Slate950 else Color(0xFFF8FAFC)
    val cardBg = if (isDark) Slate900 else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Slate400 else Color(0xFF64748B)
    val borderColor = if (isDark) Slate800 else Color(0xFFE2E8F0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .widthIn(max = 600.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Indigo600),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = "Clips Library",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Sanaullah Clips",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = "${clips.size} Saved & Cut Clips",
                        fontSize = 11.sp,
                        color = subTextColor
                    )
                }
            }

            Button(
                onClick = { viewModel.setScreen(AppScreen.STUDIO) },
                colors = ButtonDefaults.buttonColors(containerColor = TikTokPink),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Clip", modifier = Modifier.size(16.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Clip", fontSize = 12.sp, color = Color.White)
            }
        }

        if (clips.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = Slate700, modifier = Modifier.size(54.dp))
                    Text("No clips saved yet", color = Slate400, fontSize = 14.sp)
                    Button(onClick = { viewModel.setScreen(AppScreen.STUDIO) }) {
                        Text("Create Your First Clip")
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(clips) { clip ->
                    SavedClipCard(
                        clip = clip,
                        cardBg = cardBg,
                        borderColor = borderColor,
                        textColor = textColor,
                        subTextColor = subTextColor,
                        onPlay = {
                            val index = clips.indexOf(clip)
                            if (index >= 0) {
                                viewModel.setScreen(AppScreen.FEED)
                            }
                        },
                        onShare = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "${clip.title}\n\n${clip.caption}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Clip"))
                        },
                        onDelete = { viewModel.deleteClip(clip) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedClipCard(
    clip: ClipEntity,
    cardBg: Color,
    borderColor: Color,
    textColor: Color,
    subTextColor: Color,
    onPlay: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay() }
    ) {
        Column {
            // Thumbnail container (9:16 aspect ratio preview)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 14f)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(Color.Black)
            ) {
                val drawableRes = SampleClipsProvider.getDrawableIdByName(clip.thumbnailDrawableName)
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = clip.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                )

                // Duration badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {
                    Text(
                        text = "${clip.endTimeSec - clip.startTimeSec}s",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Crop Mode Pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (clip.cropMode.contains("Smart")) TikTokCyan.copy(alpha = 0.85f) else Slate800.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                ) {
                    Text(
                        text = if (clip.cropMode.contains("Smart")) "AI Track" else "9:16 Crop",
                        color = if (clip.cropMode.contains("Smart")) Slate950 else Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                // Center Play Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Likes count in thumbnail bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Likes", tint = TikTokPink, modifier = Modifier.size(12.dp))
                    Text(
                        text = if (clip.likesCount > 999) "${clip.likesCount / 1000}K" else "${clip.likesCount}",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Info & Actions
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = clip.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = clip.creatorHandle,
                    fontSize = 10.sp,
                    color = subTextColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = TikTokCyan, modifier = Modifier.size(14.dp))
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = subTextColor, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}
