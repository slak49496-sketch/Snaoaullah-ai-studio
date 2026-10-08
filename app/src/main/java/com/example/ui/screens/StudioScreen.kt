package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.CropOriginal
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleClipsProvider
import com.example.ui.StudioUiState
import com.example.ui.TikTokStudioViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StudioScreen(
    uiState: StudioUiState,
    viewModel: TikTokStudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Video Picker Launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setVideoUri(uri)
        }
    }

    val isDark = uiState.isDarkMode
    val bgColor = if (isDark) Slate950 else Color(0xFFF8FAFC)
    val cardColor = if (isDark) Slate900 else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Slate400 else Color(0xFF64748B)
    val inputBg = if (isDark) Slate950 else Color(0xFFF1F5F9)
    val borderColor = if (isDark) Slate800 else Color(0xFFE2E8F0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .widthIn(max = 600.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header matching HTML mockup with Sanaullah Studio branding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Studio Sparkles",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Sanaullah Studio",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 0.3.sp
                    )
                    Text(
                        text = "AI 9:16 Shorts & Reels Studio",
                        fontSize = 11.sp,
                        color = subTextColor
                    )
                }
            }

            // Right Actions: White/Dark Theme Switcher & Google/Account Status Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // White / Dark Switch Icon
                IconButton(
                    onClick = { viewModel.toggleTheme() },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(cardColor)
                        .border(1.dp, borderColor, CircleShape)
                        .testTag("studio_theme_toggle")
                ) {
                    Icon(
                        imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Toggle Theme",
                        tint = if (isDark) TikTokCyan else Color(0xFFF59E0B),
                        modifier = Modifier.size(17.dp)
                    )
                }

                // Google Account Status / Login Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = cardColor,
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier.clickable { viewModel.openLoginModal() }.testTag("studio_login_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen)
                        )
                        Text(
                            text = if (uiState.isSignedIn) "Sanaullah • Pro" else "Log In",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                    }
                }
            }
        }

        // Demo Video Selection Chips
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "DEMO SAMPLE VIDEOS (INSTANT PLAYBACK)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = subTextColor,
                letterSpacing = 0.5.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val samples = listOf("🎙️ Sanaullah AI", "📱 2035 Tech", "🔥 Street Dance")
                samples.forEachIndexed { index, name ->
                    val isSelected = uiState.selectedVideoUri == null && uiState.selectedDemoSampleIndex == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectDemoSample(index) },
                        label = { Text(name, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Indigo600,
                            selectedLabelColor = Color.White,
                            containerColor = cardColor,
                            labelColor = if (isDark) Slate300 else Slate700
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) TikTokCyan else borderColor
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // File Upload Box matching HTML dashed box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(
                    BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(Indigo500.copy(alpha = 0.7f), TikTokCyan.copy(alpha = 0.7f)))),
                    RoundedCornerShape(16.dp)
                )
                .background(cardColor)
                .clickable { videoPickerLauncher.launch("video/*") }
                .padding(vertical = 20.dp, horizontal = 16.dp)
                .testTag("upload_video_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Indigo600.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Upload Video",
                        tint = if (isDark) TikTokCyan else Indigo600,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Text(
                    text = if (uiState.selectedVideoUri != null) "Selected: ${uiState.videoFileName}" else "Tap to Select Device Video",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
                Text(
                    text = "MP4, MOV, WEBM • Up to 4K • Real-time 9:16 Crop",
                    fontSize = 11.sp,
                    color = subTextColor
                )
            }
        }

        // 9:16 TikTok Preview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(TikTokPink)
                        )
                        Text(
                            text = "9:16 TIKTOK VERTICAL PREVIEW",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Framing Grid Toggle Button
                    OutlinedButton(
                        onClick = { viewModel.toggleGridOverlay() },
                        modifier = Modifier.height(28.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (uiState.isSpeakerGridOverlayVisible) TikTokCyan else Slate400
                        ),
                        border = BorderStroke(1.dp, if (uiState.isSpeakerGridOverlayVisible) TikTokCyan.copy(alpha = 0.5f) else Slate800)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Grid",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (uiState.isSpeakerGridOverlayVisible) "Grid On" else "Grid Off",
                            fontSize = 10.sp
                        )
                    }
                }

                // 9:16 Aspect Ratio Frame Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(9f / 16f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black)
                            .border(BorderStroke(1.dp, Slate700), RoundedCornerShape(14.dp))
                    ) {
                        // Thumbnail or preview video graphic
                        val drawableRes = if (uiState.selectedVideoUri == null) {
                            when (uiState.selectedDemoSampleIndex) {
                                1 -> SampleClipsProvider.getDrawableIdByName("sample_tech_frame_1791336825993")
                                2 -> SampleClipsProvider.getDrawableIdByName("sample_dance_frame_1791336840514")
                                else -> SampleClipsProvider.getDrawableIdByName("sample_podcast_frame_1791336810933")
                            }
                        } else {
                            SampleClipsProvider.getDrawableIdByName("sample_podcast_frame_1791336810933")
                        }

                        Image(
                            painter = painterResource(id = drawableRes),
                            contentDescription = "Clip preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Smart AI Face / Speaker Tracking simulation box
                        if (uiState.cropMode.contains("Smart") && uiState.isSpeakerGridOverlayVisible) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(90.dp)
                                    .align(Alignment.TopCenter)
                                    .padding(top = 36.dp)
                                    .border(BorderStroke(1.5.dp, TikTokCyan), RoundedCornerShape(8.dp))
                                    .background(TikTokCyan.copy(alpha = 0.1f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .background(TikTokCyan)
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Face,
                                        contentDescription = "Face Detect",
                                        tint = Slate950,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = "AI SPEAKER LOCK",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate950
                                    )
                                }
                            }
                        }

                        // Rule of Thirds Grid overlay
                        if (uiState.isSpeakerGridOverlayVisible) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.15f)))
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.15f)))
                            }
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White.copy(alpha = 0.15f)))
                                Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White.copy(alpha = 0.15f)))
                            }
                        }

                        // Right overlay action icons preview (matching TikTok)
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Slate800.copy(alpha = 0.7f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "Avatar",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text("142K", fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("3.8K", fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        // Bottom Overlay caption preview
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                    )
                                )
                                .padding(8.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "@creator_pro",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = uiState.topicText.ifBlank { "Viral Clip Hook..." },
                                    fontSize = 10.sp,
                                    color = Slate200,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = "Music",
                                        tint = TikTokCyan,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = "Trending Viral Audio ♫",
                                        fontSize = 9.sp,
                                        color = TikTokCyan
                                    )
                                }
                            }
                        }

                        // Play/Pause center overlay toggle
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .align(Alignment.Center)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable { viewModel.togglePreviewPlayback() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (uiState.isPlayingPreview) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Playback scrub bar and timestamp
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val progress = if (uiState.endTimeSec > uiState.startTimeSec) {
                        (uiState.previewCurrentPosSec - uiState.startTimeSec).toFloat() /
                                (uiState.endTimeSec - uiState.startTimeSec).toFloat()
                    } else 0f

                    LinearProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = TikTokPink,
                        trackColor = Slate800
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pos: ${formatSeconds(uiState.previewCurrentPosSec)}",
                            fontSize = 10.sp,
                            color = Slate400
                        )
                        Text(
                            text = "Clip: ${formatSeconds(uiState.startTimeSec)} - ${formatSeconds(uiState.endTimeSec)} (${uiState.endTimeSec - uiState.startTimeSec}s)",
                            fontSize = 10.sp,
                            color = TikTokCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Trimming Controls matching HTML mockup
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CLIP SETTINGS & TRIMMING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = subTextColor,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${uiState.endTimeSec - uiState.startTimeSec}s selected",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TikTokPink
                    )
                }

                // Start & End Sec Inputs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Start (Sec)", fontSize = 11.sp, color = subTextColor, modifier = Modifier.padding(bottom = 4.dp))
                        OutlinedTextField(
                            value = uiState.startTimeSec.toString(),
                            onValueChange = { newVal ->
                                val s = newVal.toIntOrNull() ?: 0
                                viewModel.updateTrimBounds(s, uiState.endTimeSec)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = inputBg,
                                unfocusedContainerColor = inputBg,
                                focusedBorderColor = Indigo500,
                                unfocusedBorderColor = borderColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("start_time_input")
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("End (Sec)", fontSize = 11.sp, color = subTextColor, modifier = Modifier.padding(bottom = 4.dp))
                        OutlinedTextField(
                            value = uiState.endTimeSec.toString(),
                            onValueChange = { newVal ->
                                val e = newVal.toIntOrNull() ?: (uiState.startTimeSec + 1)
                                viewModel.updateTrimBounds(uiState.startTimeSec, e)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = inputBg,
                                unfocusedContainerColor = inputBg,
                                focusedBorderColor = Indigo500,
                                unfocusedBorderColor = borderColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("end_time_input")
                        )
                    }
                }

                // Quick Preset Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf(15, 30, 45, 60)
                    presets.forEach { sec ->
                        val isCurrent = (uiState.endTimeSec - uiState.startTimeSec) == sec
                        OutlinedButton(
                            onClick = { viewModel.setQuickPresetDuration(sec) },
                            modifier = Modifier.weight(1f).height(34.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isCurrent) Indigo600 else inputBg,
                                contentColor = if (isCurrent) Color.White else textColor
                            ),
                            border = BorderStroke(1.dp, if (isCurrent) TikTokCyan else borderColor)
                        ) {
                            Text("${sec}s", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Crop Mode Selection (Center Crop vs Smart AI Speaker Tracking)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Crop Mode", fontSize = 11.sp, color = subTextColor)
                    var cropExpanded by remember { mutableStateOf(false) }
                    val cropModes = listOf("Center Crop (9:16 TikTok Ratio)", "Smart AI Speaker Tracking")

                    ExposedDropdownMenuBox(
                        expanded = cropExpanded,
                        onExpandedChange = { cropExpanded = !cropExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.cropMode,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = inputBg,
                                unfocusedContainerColor = inputBg,
                                focusedBorderColor = Indigo500,
                                unfocusedBorderColor = borderColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = cropExpanded,
                            onDismissRequest = { cropExpanded = false },
                            modifier = Modifier.background(cardColor)
                        ) {
                            cropModes.forEach { mode ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (mode.contains("Smart")) Icons.Default.Face else Icons.Default.CropOriginal,
                                                contentDescription = null,
                                                tint = if (mode == uiState.cropMode) TikTokCyan else subTextColor,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = mode,
                                                fontSize = 13.sp,
                                                color = if (mode == uiState.cropMode) textColor else subTextColor
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.setCropMode(mode)
                                        cropExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Topic / Title Input
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Topic / Title", fontSize = 11.sp, color = subTextColor)
                    OutlinedTextField(
                        value = uiState.topicText,
                        onValueChange = { viewModel.setTopic(it) },
                        placeholder = { Text("e.g. Viral Shorts Clip, AI Secrets...", color = Slate500, fontSize = 13.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = inputBg,
                            unfocusedContainerColor = inputBg,
                            focusedBorderColor = Indigo500,
                            unfocusedBorderColor = borderColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("topic_input")
                    )
                }

                // Cut & Generate Clip Main Button
                Button(
                    onClick = { viewModel.processVideoAndGenerateClip() },
                    enabled = !uiState.isProcessing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("cut_and_generate_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo600,
                        disabledContainerColor = Slate800
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    if (uiState.isProcessing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "AI Analyzing & Cutting...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ContentCut,
                            contentDescription = "Cut",
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cut & Generate 9:16 Clip",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Auto Caption & Virality Card matching HTML auto caption card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Caption",
                            tint = TikTokCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "AI VIRAL CAPTIONS & TAGS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = subTextColor,
                            letterSpacing = 0.5.sp
                        )
                    }

                    if (uiState.generatedResult != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TikTokPink.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, TikTokPink.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "Virality: ${uiState.generatedResult.viralityScore}%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TikTokPink,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                val captionText = uiState.generatedResult?.caption
                    ?: "🔥 ${uiState.topicText.ifBlank { "Viral Shorts Clip" }}\n⏱ Clip duration: ${uiState.startTimeSec}s - ${uiState.endTimeSec}s | Mode: ${uiState.cropMode}\n\n#sanaullah #fyp #viral #trending #tiktok";

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = inputBg,
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = captionText,
                            fontSize = 12.sp,
                            color = textColor,
                            lineHeight = 18.sp
                        )

                        // Action Buttons: Copy & Share
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("TikTok Caption", captionText))
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp), tint = subTextColor)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy Caption", fontSize = 11.sp, color = subTextColor)
                            }

                            TextButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, captionText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share TikTok Caption"))
                                }
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(14.dp), tint = TikTokCyan)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share", fontSize = 11.sp, color = TikTokCyan)
                            }
                        }
                    }
                }

                // AI Hashtags row
                val tags = uiState.generatedResult?.hashtags ?: listOf("#fyp", "#viral", "#trending", "#tiktokstudio", "#contentcreator")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Slate850,
                            border = BorderStroke(1.dp, Slate800)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Indigo400,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // Success Generation Dialog
    if (uiState.showClipGeneratedDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissGeneratedDialog() },
            containerColor = cardColor,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Clip Ready for Sanaullah Studio!", color = textColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Successfully cropped to 9:16 vertical video ratio with ${uiState.cropMode}.",
                        fontSize = 13.sp,
                        color = textColor
                    )
                    Text(
                        text = "Duration: ${uiState.endTimeSec - uiState.startTimeSec}s\nSaved in your Sanaullah Studio clips & added to your creator profile grid!",
                        fontSize = 12.sp,
                        color = subTextColor
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissGeneratedDialog()
                        viewModel.setScreen(com.example.ui.AppScreen.FEED)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokPink)
                ) {
                    Text("Watch on For You Feed 🔥", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissGeneratedDialog() }) {
                    Text("Stay in Studio", color = subTextColor)
                }
            }
        )
    }
}

private fun formatSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}

