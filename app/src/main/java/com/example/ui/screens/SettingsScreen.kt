package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun SettingsScreen(
    uiState: StudioUiState,
    viewModel: TikTokStudioViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var exportRes by remember { mutableStateOf("1080x1920 (TikTok 60FPS 9:16)") }
    var captionStyle by remember { mutableStateOf("TikTok Bold Highlight") }
    var autoHashtagsEnabled by remember { mutableStateOf(true) }
    var highBitrateEnabled by remember { mutableStateOf(true) }

    val isDark = uiState.isDarkMode
    val bgColor = if (isDark) Slate950 else Color(0xFFF8FAFC)
    val cardColor = if (isDark) Slate900 else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Slate400 else Color(0xFF64748B)
    val borderColor = if (isDark) Slate800 else Color(0xFFE2E8F0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .widthIn(max = 600.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = "Sanaullah Studio Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = "Theme & AI Virality Engine",
                    fontSize = 11.sp,
                    color = subTextColor
                )
            }
        }

        // Theme Switcher Card (White & Dark Mode)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isDark) "Dark Theme Active" else "White / Light Theme Active",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = if (isDark) "High-contrast dark TikTok aesthetic" else "Clean, bright white studio aesthetic",
                        fontSize = 11.sp,
                        color = subTextColor
                    )
                }
                Switch(
                    checked = !isDark,
                    onCheckedChange = { viewModel.toggleTheme() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Indigo600
                    )
                )
            }
        }

        // Creator Profile & Login Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Indigo600),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = uiState.userName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = TikTokCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "${uiState.userHandle} • ${uiState.followersCount} Followers",
                            fontSize = 12.sp,
                            color = subTextColor
                        )
                        Text(
                            text = "${uiState.authProvider} Connected",
                            fontSize = 10.sp,
                            color = EmeraldGreen,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Button(
                    onClick = { viewModel.openLoginModal() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isDark) Slate800 else Color(0xFFF1F5F9), contentColor = textColor),
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Text("Change Login Method / Account", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // AI Engine Configuration Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TikTokCyan, modifier = Modifier.size(18.dp))
                    Text("AI Virality Engine", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) Slate950 else Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Active Model: Google Gemini 3.5 Flash", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TikTokCyan)
                        Text(
                            "Generates hook psychology, sound suggestions, caption wording, and speaker crop coordinates optimized for TikTok retention algorithms.",
                            fontSize = 11.sp,
                            color = subTextColor,
                            lineHeight = 16.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Auto-generate trending #tags", fontSize = 13.sp, color = textColor, fontWeight = FontWeight.Medium)
                        Text("Includes #sanaullah, #fyp, #viral niches", fontSize = 11.sp, color = subTextColor)
                    }
                    Switch(
                        checked = autoHashtagsEnabled,
                        onCheckedChange = { autoHashtagsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TikTokPink
                        )
                    )
                }
            }
        }

        // Export Resolution Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.HighQuality, contentDescription = null, tint = Indigo500, modifier = Modifier.size(18.dp))
                    Text("9:16 Video Export Format", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                val resolutions = listOf(
                    "1080x1920 (TikTok 60FPS 9:16)",
                    "720x1280 (Fast Mobile Render)",
                    "2160x3840 (4K Vertical Ultra)"
                )
                resolutions.forEach { res ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (res == exportRes),
                            onClick = { exportRes = res },
                            colors = RadioButtonDefaults.colors(selectedColor = TikTokPink)
                        )
                        Text(
                            text = res,
                            fontSize = 12.sp,
                            color = if (res == exportRes) textColor else subTextColor
                        )
                    }
                }
            }
        }

        // Caption Styles Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Subtitles, contentDescription = null, tint = TikTokPink, modifier = Modifier.size(18.dp))
                    Text("Subtitle & Caption Style", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                val styles = listOf("TikTok Bold Highlight", "Hormozi Kinetic Yellow", "Clean Minimal White")
                styles.forEach { style ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (style == captionStyle),
                            onClick = { captionStyle = style },
                            colors = RadioButtonDefaults.colors(selectedColor = TikTokCyan)
                        )
                        Text(
                            text = style,
                            fontSize = 12.sp,
                            color = if (style == captionStyle) textColor else subTextColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
