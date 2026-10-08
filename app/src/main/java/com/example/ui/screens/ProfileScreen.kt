package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ClipEntity
import com.example.data.SampleClipsProvider
import com.example.ui.AppScreen
import com.example.ui.StudioUiState
import com.example.ui.TikTokStudioViewModel
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate200
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
fun ProfileScreen(
    uiState: StudioUiState,
    viewModel: TikTokStudioViewModel,
    modifier: Modifier = Modifier
) {
    val isDark = uiState.isDarkMode
    val bgColor = if (isDark) Slate950 else Color(0xFFF8FAFC)
    val cardColor = if (isDark) Slate900 else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Slate400 else Color(0xFF64748B)
    val borderColor = if (isDark) Slate800 else Color(0xFFE2E8F0)

    val clips = uiState.feedClips

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .widthIn(max = 600.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.setScreen(AppScreen.STUDIO) },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Slate900 else Color.White)
                    .border(1.dp, borderColor, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = textColor, modifier = Modifier.size(18.dp))
            }

            Text(
                text = "Sanaullah Studio",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            // Theme Switcher & Login Shortcut
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { viewModel.toggleTheme() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Slate900 else Color.White)
                        .border(1.dp, borderColor, CircleShape)
                        .testTag("theme_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Toggle Theme",
                        tint = if (isDark) TikTokCyan else Color(0xFFF59E0B),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { viewModel.openLoginModal() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Slate900 else Color.White)
                        .border(1.dp, borderColor, CircleShape)
                        .testTag("profile_login_options_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Login Options",
                        tint = TikTokPink,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Avatar matching screenshot
        Box(
            modifier = Modifier.size(92.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            listOf(TikTokPink, TikTokCyan, Indigo600, TikTokPink)
                        )
                    )
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(if (isDark) Slate900 else Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "TikTok Music Icon",
                        tint = if (isDark) TikTokCyan else Indigo600,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // User Name & Handle
        Text(
            text = uiState.userName,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = uiState.userHandle,
            fontSize = 13.sp,
            color = subTextColor
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Numbers Row matching screenshot (3M Followers, 1.2M Following, Likes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(count = uiState.followersCount, label = "Followers", textColor = textColor, subTextColor = subTextColor)
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(borderColor))
            StatItem(count = uiState.followingCount, label = "Following", textColor = textColor, subTextColor = subTextColor)
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(borderColor))
            StatItem(count = uiState.likesTotalCount, label = "Likes", textColor = textColor, subTextColor = subTextColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons Row matching screenshot ("User profile", "TikTok Profile")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.openLoginModal() },
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Slate900 else Color.White,
                    contentColor = textColor
                ),
                border = BorderStroke(1.dp, borderColor)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "User Profile",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }

            Button(
                onClick = { viewModel.openLoginModal() },
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TikTokPink,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "TikTok Profile",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs matching screenshot (Videos Grid, Liked, Private/Saved)
        TabRow(
            selectedTabIndex = uiState.profileTab,
            containerColor = Color.Transparent,
            contentColor = textColor,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.profileTab]),
                    color = TikTokPink,
                    height = 2.5.dp
                )
            },
            divider = { HorizontalDivider(color = borderColor) }
        ) {
            Tab(
                selected = uiState.profileTab == 0,
                onClick = { viewModel.setProfileTab(0) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Videos",
                        tint = if (uiState.profileTab == 0) textColor else subTextColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            )
            Tab(
                selected = uiState.profileTab == 1,
                onClick = { viewModel.setProfileTab(1) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Liked",
                        tint = if (uiState.profileTab == 1) textColor else subTextColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            )
            Tab(
                selected = uiState.profileTab == 2,
                onClick = { viewModel.setProfileTab(2) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Private",
                        tint = if (uiState.profileTab == 2) textColor else subTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }

        // 3x3 Video Grid matching user's screenshot
        val displayedClips = when (uiState.profileTab) {
            1 -> clips.filter { it.isLiked }
            2 -> clips.filter { it.isSaved }
            else -> clips
        }

        if (displayedClips.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = subTextColor, modifier = Modifier.size(44.dp))
                    Text("No videos in this section yet", color = subTextColor, fontSize = 13.sp)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp)
            ) {
                itemsIndexed(displayedClips) { index, clip ->
                    ProfileVideoGridItem(
                        clip = clip,
                        onClick = {
                            val targetIndex = clips.indexOf(clip)
                            if (targetIndex >= 0) {
                                viewModel.setScreen(AppScreen.FEED)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    count: String,
    label: String,
    textColor: Color,
    subTextColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = subTextColor
        )
    }
}

@Composable
private fun ProfileVideoGridItem(
    clip: ClipEntity,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
            .background(Color.Black)
            .clickable { onClick() }
    ) {
        val drawableRes = SampleClipsProvider.getDrawableIdByName(clip.thumbnailDrawableName)
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = clip.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark gradient at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                    )
                )
        )

        // Views / Likes Counter in bottom left matching screenshot
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = if (clip.likesCount > 999) "${clip.likesCount / 1000}K" else "${clip.likesCount}",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
