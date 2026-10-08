package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppScreen
import com.example.ui.StudioUiState
import com.example.ui.TikTokStudioViewModel
import com.example.ui.components.LoginModalSheet
import com.example.ui.screens.CameraScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedClipsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink

class MainActivity : ComponentActivity() {
    private val viewModel: TikTokStudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            // Dynamic White and Dark theme support
            MyApplicationTheme(darkTheme = uiState.isDarkMode) {
                val context = LocalContext.current
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(uiState.toastMessage) {
                    uiState.toastMessage?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        viewModel.clearToast()
                    }
                }

                // Handle back press
                BackHandler(enabled = uiState.currentScreen != AppScreen.STUDIO) {
                    viewModel.setScreen(AppScreen.STUDIO)
                }

                val isDark = uiState.isDarkMode
                val mainBg = if (isDark) Slate950 else Color(0xFFF8FAFC)

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(mainBg),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        // Do not show standard bottom bar when in camera recording mode
                        if (uiState.currentScreen != AppScreen.CAMERA) {
                            StudioBottomNavigationBar(
                                currentScreen = uiState.currentScreen,
                                isDark = isDark,
                                onSelectScreen = { viewModel.setScreen(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = if (uiState.currentScreen != AppScreen.CAMERA) innerPadding.calculateBottomPadding() else 0.dp)
                            .then(
                                if (uiState.currentScreen != AppScreen.FEED && uiState.currentScreen != AppScreen.CAMERA) {
                                    Modifier.statusBarsPadding()
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        AnimatedContent(
                            targetState = uiState.currentScreen,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                AppScreen.STUDIO -> StudioScreen(uiState = uiState, viewModel = viewModel)
                                AppScreen.FEED -> FeedScreen(uiState = uiState, viewModel = viewModel)
                                AppScreen.CAMERA -> CameraScreen(uiState = uiState, viewModel = viewModel)
                                AppScreen.PROFILE -> ProfileScreen(uiState = uiState, viewModel = viewModel)
                                AppScreen.SAVED -> SavedClipsScreen(uiState = uiState, viewModel = viewModel)
                                AppScreen.SETTINGS -> SettingsScreen(uiState = uiState, viewModel = viewModel)
                            }
                        }

                        // White and Dark Login Options Modal
                        if (uiState.showLoginModal) {
                            LoginModalSheet(
                                uiState = uiState,
                                viewModel = viewModel,
                                onDismiss = { viewModel.closeLoginModal() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudioBottomNavigationBar(
    currentScreen: AppScreen,
    isDark: Boolean,
    onSelectScreen: (AppScreen) -> Unit
) {
    val barColor = if (isDark) Slate900.copy(alpha = 0.98f) else Color.White.copy(alpha = 0.98f)
    val borderColor = if (isDark) Slate800 else Color(0xFFE2E8F0)

    Surface(
        color = barColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Studio Tab (Scissors)
            BottomNavItem(
                icon = if (currentScreen == AppScreen.STUDIO) Icons.Filled.ContentCut else Icons.Outlined.ContentCut,
                label = "Studio",
                isSelected = currentScreen == AppScreen.STUDIO,
                isDark = isDark,
                testTag = "nav_studio",
                onClick = { onSelectScreen(AppScreen.STUDIO) }
            )

            // Feed Tab (Video / Reel)
            BottomNavItem(
                icon = if (currentScreen == AppScreen.FEED) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircle,
                label = "Feed",
                isSelected = currentScreen == AppScreen.FEED,
                isDark = isDark,
                testTag = "nav_feed",
                onClick = { onSelectScreen(AppScreen.FEED) }
            )

            // Center Camera (+) Create Button (Authentic TikTok dual-color badge)
            TikTokCreateButton(
                onClick = { onSelectScreen(AppScreen.CAMERA) }
            )

            // Profile Tab (User Profile matching user's screenshot left side!)
            BottomNavItem(
                icon = if (currentScreen == AppScreen.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                label = "Profile",
                isSelected = currentScreen == AppScreen.PROFILE,
                isDark = isDark,
                testTag = "nav_profile",
                onClick = { onSelectScreen(AppScreen.PROFILE) }
            )

            // Clips / Library Tab
            BottomNavItem(
                icon = if (currentScreen == AppScreen.SAVED) Icons.Filled.Layers else Icons.Outlined.Layers,
                label = "Clips",
                isSelected = currentScreen == AppScreen.SAVED,
                isDark = isDark,
                testTag = "nav_clips",
                onClick = { onSelectScreen(AppScreen.SAVED) }
            )

            // Settings Tab
            BottomNavItem(
                icon = if (currentScreen == AppScreen.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                label = "Settings",
                isSelected = currentScreen == AppScreen.SETTINGS,
                isDark = isDark,
                testTag = "nav_settings",
                onClick = { onSelectScreen(AppScreen.SETTINGS) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    isDark: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val activeColor = if (isDark) Indigo400 else Indigo600
    val inactiveColor = if (isDark) Slate400 else Color(0xFF64748B)
    val textActiveColor = if (isDark) Color.White else Color(0xFF0F172A)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 3.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(21.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) textActiveColor else inactiveColor
        )
    }
}

@Composable
private fun TikTokCreateButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(44.dp)
            .height(28.dp)
            .clickable(onClick = onClick)
            .testTag("nav_camera"),
        contentAlignment = Alignment.Center
    ) {
        // Cyan and Pink background layer offset
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(17.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(TikTokCyan)
            )
            Box(
                modifier = Modifier
                    .width(17.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(TikTokPink)
            )
        }

        // Center white/black button
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(28.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create / Record",
                tint = Color.Black,
                modifier = Modifier.size(19.dp)
            )
        }
    }
}
