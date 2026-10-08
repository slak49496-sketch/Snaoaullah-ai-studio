package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.StudioUiState
import com.example.ui.TikTokStudioViewModel
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink

@Composable
fun CameraScreen(
    uiState: StudioUiState,
    viewModel: TikTokStudioViewModel,
    modifier: Modifier = Modifier
) {
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setVideoUri(uri)
            viewModel.setScreen(com.example.ui.AppScreen.STUDIO)
        }
    }

    val pulseTransition = rememberInfiniteTransition(label = "RecordPulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Camera Viewfinder Background Graphic
        Image(
            painter = painterResource(id = R.drawable.sample_podcast_frame_1791336810933),
            contentDescription = "Camera Viewfinder",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Camera filter tint overlay
        val filterTint = when (uiState.activeCameraFilter) {
            "Cyber Glow" -> TikTokCyan.copy(alpha = 0.08f)
            "Golden Hour" -> Color(0xFFF59E0B).copy(alpha = 0.1f)
            "Neon Noir" -> TikTokPink.copy(alpha = 0.08f)
            else -> Color.Transparent
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(filterTint)
        )

        // Recording Progress Bar at top
        if (uiState.isRecording) {
            LinearProgressIndicator(
                progress = { (uiState.recordedDurationSec / 60f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.TopCenter),
                color = TikTokPink,
                trackColor = Slate800
            )
        }

        // Top Bar: Close (X), Sounds Selector Pill, Flip Camera
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close (X) button -> takes back to studio
            IconButton(
                onClick = { viewModel.setScreen(com.example.ui.AppScreen.STUDIO) },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Camera",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Sounds Selector Pill (center)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.55f),
                border = BorderStroke(1.dp, Slate700)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Music",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = uiState.selectedCameraSound,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Flip Camera button
            IconButton(
                onClick = { viewModel.flipCamera() },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Flip Camera",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Right Vertical Tool Column (matching user's screenshot left mockup)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Speed toggle
            CameraSideToolItem(
                icon = Icons.Default.Speed,
                label = "${uiState.cameraSpeed}x",
                isActive = uiState.cameraSpeed != 1.0f,
                onClick = { viewModel.cycleSpeed() }
            )

            // Beauty Filter
            CameraSideToolItem(
                icon = Icons.Default.Star,
                label = "Beauty",
                isActive = uiState.isBeautyOn,
                onClick = { viewModel.toggleBeauty() }
            )

            // Timer (3s / 10s)
            CameraSideToolItem(
                icon = Icons.Default.Timer,
                label = if (uiState.cameraTimerSec == 0) "Timer" else "${uiState.cameraTimerSec}s",
                isActive = uiState.cameraTimerSec > 0,
                onClick = { viewModel.cycleTimer() }
            )

            // Flash / Torch
            CameraSideToolItem(
                icon = if (uiState.isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                label = if (uiState.isFlashOn) "Flash On" else "Flash",
                isActive = uiState.isFlashOn,
                onClick = { viewModel.toggleFlash() }
            )

            // Filter / Color
            CameraSideToolItem(
                icon = Icons.Default.Visibility,
                label = uiState.activeCameraFilter,
                isActive = uiState.activeCameraFilter != "Normal",
                onClick = { viewModel.cycleFilter() }
            )
        }

        // Recording Duration Timer (if recording)
        if (uiState.isRecording) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 70.dp),
                shape = RoundedCornerShape(12.dp),
                color = TikTokPink.copy(alpha = 0.9f)
            ) {
                Text(
                    text = "RECORDING: ${uiState.recordedDurationSec}s / 60s",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // Bottom Controls: Effects, Big Red Record Button, Upload Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Effects button (left)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { viewModel.cycleFilter() }
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, Slate700, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Effects",
                        tint = TikTokCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text("Effects", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }

            // Big Red Record Button (center)
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .border(4.dp, Color.White, CircleShape)
                    .padding(6.dp)
                    .clickable { viewModel.toggleRecording() }
                    .testTag("camera_record_button"),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isRecording) {
                    // Stop recording red square
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TikTokPink)
                            .scale(pulseScale)
                    )
                } else {
                    // Idle red circle
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(TikTokPink)
                    )
                }
            }

            // Upload from Gallery button (right)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { videoPickerLauncher.launch("video/*") }
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, Slate700, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Upload",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text("Upload", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CameraSideToolItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isActive) TikTokPink.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.4f))
                .border(1.dp, if (isActive) TikTokPink else Color.Transparent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) TikTokPink else Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = label,
            color = if (isActive) TikTokPink else Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
