package com.example.nothingwidget.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nothingwidget.ui.components.BottomNavBar
import com.example.nothingwidget.ui.components.NothingTopAppBar
import com.example.nothingwidget.ui.theme.*

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            NothingTopAppBar(
                title = "SETTINGS",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = "settings") },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape)
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DEVICE STATUS", color = MutedOutline, style = Typography.labelSmall)
                    Text(
                        text = "ONLINE",
                        color = PrimaryText,
                        fontSize = 48.sp,
                        fontFamily = SpaceMono
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.size(8.dp).background(AccentRed))
                        Box(modifier = Modifier.size(8.dp).background(PrimaryText))
                        Box(modifier = Modifier.size(8.dp).background(PrimaryText))
                    }
                }
            }

            // SYSTEM
            SettingsSection(title = "SYSTEM") {
                SettingsRow(title = "WIDGET REFRESH RATE", subtitle = "15 MIN")
                var bgActivity by remember { mutableStateOf(true) }
                SettingsRow(
                    title = "BACKGROUND ACTIVITY",
                    subtitle = "OPTIMIZED FOR BATTERY",
                    trailing = {
                        SharpToggle(checked = bgActivity, onCheckedChange = { bgActivity = it })
                    }
                )
            }

            // DISPLAY
            SettingsSection(title = "DISPLAY") {
                SettingsRow(title = "THEME", subtitle = "PURE BLACK")
                var intensity by remember { mutableFloatStateOf(50f) }
                SettingsRow(
                    title = "DOT-MATRIX INTENSITY",
                    subtitle = "${intensity.toInt()}%",
                    trailing = {
                        Slider(
                            value = intensity,
                            onValueChange = { intensity = it },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryText,
                                activeTrackColor = PrimaryText,
                                inactiveTrackColor = SurfaceContainer
                            ),
                            modifier = Modifier.width(120.dp)
                        )
                    }
                )
            }

            // ACCOUNT
            SettingsSection(title = "ACCOUNT") {
                SettingsRow(
                    title = "SYNC DATA",
                    trailing = { Icon(Icons.Default.Sync, contentDescription = "Sync", tint = PrimaryText) }
                )
                SettingsRow(
                    title = "LOG OUT",
                    titleColor = AccentRed,
                    trailing = { Icon(Icons.Default.Logout, contentDescription = "Log out", tint = AccentRed) },
                    onClick = { /* TODO */ }
                )
            }

            // ABOUT
            SettingsSection(title = "ABOUT") {
                SettingsRow(
                    title = "FIRMWARE",
                    trailing = { Text("V2.0.4", color = MutedOutline, style = Typography.labelSmall) }
                )
                SettingsRow(title = "TERMS OF SERVICE")
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "NothingWidget © 2024",
                color = MutedOutline,
                style = Typography.labelSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(AccentRed))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, color = MutedOutline, style = Typography.labelSmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape)
        ) {
            content()
        }
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String? = null,
    titleColor: Color = PrimaryText,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, color = titleColor, style = Typography.labelSmall)
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subtitle, color = MutedOutline, style = Typography.labelSmall)
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}

@Composable
fun SharpToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(24.dp)
            .background(if (checked) AccentRed else SurfaceContainer)
            .border(1.dp, if (checked) AccentRed else MutedOutline, androidx.compose.ui.graphics.RectangleShape)
            .clickable { onCheckedChange(!checked) }
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(if (checked) PrimaryText else MutedOutline)
                .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
        )
    }
}
