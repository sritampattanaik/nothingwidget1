package com.example.nothingwidget.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nothingwidget.ui.components.BottomNavBar
import com.example.nothingwidget.ui.components.NothingTopAppBar
import com.example.nothingwidget.ui.components.WidgetCard
import com.example.nothingwidget.ui.theme.*
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { NothingTopAppBar(title = "NOTHINGWIDGET") },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = "home") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = AccentRed,
                contentColor = PrimaryText,
                shape = androidx.compose.ui.graphics.RectangleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Section Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(AccentRed)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "YOUR WIDGETS",
                    color = MutedOutline,
                    style = Typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Card 1: Digital Clock (Full Width)
                item(span = { GridItemSpan(2) }) {
                    WidgetCard(title = "Digital Clock", sizeLabel = "4x2 SIZE") {
                        var currentTime by remember { mutableStateOf(LocalTime.now()) }
                        var showDot by remember { mutableStateOf(true) }

                        LaunchedEffect(Unit) {
                            while (true) {
                                delay(500)
                                showDot = !showDot
                                if (showDot) currentTime = LocalTime.now()
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentTime.format(DateTimeFormatter.ofPattern("HH")),
                                fontSize = 48.sp,
                                fontFamily = SpaceMono,
                                color = PrimaryText
                            )
                            Text(
                                text = "·",
                                fontSize = 48.sp,
                                fontFamily = SpaceMono,
                                color = if (showDot) AccentRed else Color.Transparent,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Text(
                                text = currentTime.format(DateTimeFormatter.ofPattern("mm")),
                                fontSize = 48.sp,
                                fontFamily = SpaceMono,
                                color = PrimaryText
                            )
                        }
                    }
                }

                // Card 2: Calendar
                item {
                    WidgetCard(title = "Calendar", sizeLabel = "2x2 SIZE", modifier = Modifier.aspectRatio(1f)) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("MON", color = PrimaryText, style = Typography.labelSmall)
                            Text("28", fontSize = 48.sp, fontFamily = SpaceMono, color = PrimaryText)
                            Text("JULY", color = AccentRed, style = Typography.labelSmall)
                        }
                    }
                }

                // Card 3: Weather Dot
                item {
                    WidgetCard(title = "Weather Dot", sizeLabel = "2x2 SIZE", modifier = Modifier.aspectRatio(1f)) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopRight)
                                    .size(6.dp)
                                    .background(AccentRed)
                            )
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.Center)
                                    .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.WbSunny, contentDescription = "Sunny", tint = PrimaryText)
                            }
                        }
                    }
                }

                // Card 4: Power Level (Full Width)
                item(span = { GridItemSpan(2) }) {
                    WidgetCard(title = "Power Level", sizeLabel = "2x1 SIZE") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("POWER LEVEL", color = PrimaryText, style = Typography.labelSmall)
                                Text("87%", color = PrimaryText, style = Typography.labelSmall)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(SurfaceLowest)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.87f)
                                        .height(2.dp)
                                        .background(PrimaryText)
                                )
                            }
                        }
                    }
                }

                // Firmware Section
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .border(
                                1.dp,
                                MutedOutline,
                                androidx.compose.ui.graphics.RectangleShape
                            ) // Need dashed border, will simplify to regular border or custom modifier
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("FIRMWARE_V2.0.4", color = PrimaryText, style = Typography.labelSmall)
                        Box(
                            modifier = Modifier
                                .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("DOWNLOAD", color = MutedOutline, style = Typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}
