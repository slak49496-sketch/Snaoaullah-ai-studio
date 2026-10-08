package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.StudioUiState
import com.example.ui.TikTokStudioViewModel
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginModalSheet(
    uiState: StudioUiState,
    viewModel: TikTokStudioViewModel,
    onDismiss: () -> Unit
) {
    var emailInput by remember { mutableStateOf(uiState.userEmail) }
    var passwordInput by remember { mutableStateOf("••••••••") }
    var selectedAuthTab by remember { mutableStateOf("google") } // google, tiktok, email

    val isDark = uiState.isDarkMode
    val sheetContainerColor = if (isDark) Slate900 else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDark) Slate400 else Color(0xFF64748B)
    val cardBg = if (isDark) Slate950 else Color(0xFFF8FAFC)
    val borderColor = if (isDark) Slate800 else Color(0xFFE2E8F0)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = sheetContainerColor,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with White/Dark Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Sanaullah Studio Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = "Choose your sign-in method & visual theme",
                        fontSize = 12.sp,
                        color = subTextColor
                    )
                }

                // White / Dark Theme Toggle Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = cardBg,
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier.clickable { viewModel.toggleTheme() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Theme",
                            tint = if (isDark) TikTokCyan else Color(0xFFF59E0B),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isDark) "Dark" else "White",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }

            // Current Logged In Profile Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = cardBg,
                border = BorderStroke(1.dp, borderColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Indigo600),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "S",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = uiState.userName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(EmeraldGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Active", fontSize = 10.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(
                            text = "${uiState.userHandle} • ${uiState.authProvider}",
                            fontSize = 11.sp,
                            color = subTextColor
                        )
                    }

                    if (uiState.isSignedIn) {
                        OutlinedButton(
                            onClick = { viewModel.signOut() },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, borderColor),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TikTokPink)
                        ) {
                            Text("Log Out", fontSize = 11.sp)
                        }
                    }
                }
            }

            // Authentication Methods
            Text(
                text = "SIGN IN OPTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = subTextColor,
                letterSpacing = 0.5.sp
            )

            // 1. Google Sign-In Option (matching user's HTML prototype)
            Button(
                onClick = { viewModel.signInWithGoogle() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("google_login_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color(0xFF1E293B) else Color.White,
                    contentColor = textColor
                ),
                border = BorderStroke(1.dp, if (isDark) Slate700 else Color(0xFFCBD5E1))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Google 'G' stylized badge
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = Color(0xFF4285F4)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign in with Google",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 2. TikTok Account Login Option
            Button(
                onClick = { viewModel.signInWithTikTok() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("tiktok_login_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color.Black else Color(0xFF111827),
                    contentColor = Color.White
                ),
                border = BorderStroke(1.dp, TikTokPink.copy(alpha = 0.5f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "TikTok",
                        tint = TikTokPink,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Continue with TikTok",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = borderColor)
                Text(
                    text = "  OR EMAIL  ",
                    fontSize = 10.sp,
                    color = subTextColor,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = borderColor)
            }

            // 3. Email & Password Input
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Creator Email", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = subTextColor, modifier = Modifier.size(18.dp)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cardBg,
                        unfocusedContainerColor = cardBg,
                        focusedBorderColor = Indigo600,
                        unfocusedBorderColor = borderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = subTextColor, modifier = Modifier.size(18.dp)) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cardBg,
                        unfocusedContainerColor = cardBg,
                        focusedBorderColor = Indigo600,
                        unfocusedBorderColor = borderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { viewModel.signInWithEmail(emailInput) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Text(
                        text = "Sign In to Sanaullah Studio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
