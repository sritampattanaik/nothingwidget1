package com.example.nothingwidget.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nothingwidget.ui.theme.AccentRed
import com.example.nothingwidget.ui.theme.BackgroundColor
import com.example.nothingwidget.ui.theme.PrimaryText

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(BackgroundColor)
            .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Default.Home,
            isActive = currentRoute == "home",
            onClick = { navController.navigate("home") }
        )
        BottomNavItem(
            icon = Icons.Default.Dashboard,
            isActive = currentRoute == "customize",
            onClick = { navController.navigate("customize/clock") }
        )
        BottomNavItem(
            icon = Icons.Default.Code,
            isActive = currentRoute == "terminal",
            onClick = {  }
        )
        BottomNavItem(
            icon = Icons.Default.Settings,
            isActive = currentRoute == "settings",
            onClick = { navController.navigate("settings") },
            activeColor = AccentRed
        )
    }
}

@Composable
fun RowScope.BottomNavItem(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    activeColor: Color = PrimaryText
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .background(if (isActive) activeColor else BackgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) BackgroundColor else PrimaryText
        )
    }
}
