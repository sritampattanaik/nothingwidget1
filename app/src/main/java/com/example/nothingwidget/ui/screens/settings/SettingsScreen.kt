package com.example.nothingwidget.ui.screens.settings

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nothingwidget.ui.components.NothingGlassCard
import com.example.nothingwidget.ui.theme.NothingDotFontFamily
import com.example.nothingwidget.ui.theme.NothingRed

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val cities = listOf("London", "Tokyo", "New York", "Berlin")

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                }
                Text(
                    text = "SYSTEM & ENGINE SETTINGS",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontFamily = NothingDotFontFamily,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .testTag("settings_screen_scroll"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "PREFERENCES",
                color = NothingRed,
                fontSize = 11.sp,
                fontFamily = NothingDotFontFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            // Setting Item: Dark Theme
            SettingToggleItem("ENABLE DARK THEME", "Use Nothing OS pitch black dark mode", state.isDarkTheme) {
                viewModel.toggleTheme()
            }

            // Setting Item: 24h Clock
            SettingToggleItem("24-HOUR FORMAT", "Display clocks in 24-hour matrix format", state.is24HourClock) {
                viewModel.toggle24h()
            }

            // Setting Item: Haptic Feedback
            SettingToggleItem("HAPTIC MATRIX FEEDBACK", "Vibrate gently on widget preview interactions", state.isHapticFeedbackEnabled) {
                viewModel.toggleHaptics()
            }

            // Setting Item: Temperature Unit
            SettingToggleItem("USE CELSIUS (°C)", "Display weather metrics in Celsius", state.isCelsius) {
                viewModel.toggleCelsius()
            }

            // City Selection
            Text(
                text = "PRIMARY WEATHER CITY",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontFamily = NothingDotFontFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cities.forEach { city ->
                    val isSelected = city.equals(state.selectedCity, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NothingRed else MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, if (isSelected) NothingRed else MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .clickable { viewModel.setSelectedCity(city) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = city.uppercase(),
                            color = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 10.sp,
                            fontFamily = NothingDotFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Engine Info Card
            NothingGlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = NothingRed)
                        Text("NOTHING OS ENGINE v2.5", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, fontFamily = NothingDotFontFamily, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Built with Jetpack Compose, Material You dynamic color tokens, WorkManager background task engine, and Room database persistence.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        fontFamily = NothingDotFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingToggleItem(title: String, subtitle: String, checked: Boolean, onCheckedChange: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontFamily = NothingDotFontFamily,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                fontFamily = NothingDotFontFamily
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            colors = SwitchDefaults.colors(checkedThumbColor = NothingRed)
        )
    }
}
