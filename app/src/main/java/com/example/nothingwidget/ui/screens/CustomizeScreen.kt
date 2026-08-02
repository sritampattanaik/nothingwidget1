package com.example.nothingwidget.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nothingwidget.ui.components.NothingTopAppBar
import com.example.nothingwidget.ui.theme.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeJoin
import com.example.nothingwidget.widgets.ClockWidget

@Composable
fun CustomizeScreen(navController: NavController) {
    var activeSize by remember { mutableStateOf("2x2") }
    var activeStyle by remember { mutableStateOf("Minimal") }
    var activeColor by remember { mutableStateOf(PrimaryText) }

    val context = LocalContext.current
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val clockProvider = ComponentName(context, ClockWidget::class.java)
    val prefs = context.getSharedPreferences("widget_prefs", android.content.Context.MODE_PRIVATE)

    val selectedColorHex = when (activeColor) {
        AccentRed -> "#FF5540"
        MutedOutline -> "#8E9192"
        else -> "#FFFFFF"
    }

    LaunchedEffect(activeColor) { prefs.edit().putString("clock_color", selectedColorHex).apply() }
    LaunchedEffect(activeStyle) { prefs.edit().putString("clock_style", activeStyle).apply() }
    LaunchedEffect(activeSize) { prefs.edit().putString("clock_size", activeSize).apply() }

    Scaffold(
        topBar = {
            NothingTopAppBar(
                title = "DIGITAL CLOCK",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(PrimaryText)
                    .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape)
                    .clickable {
                        if (appWidgetManager.isRequestPinAppWidgetSupported) {
                            appWidgetManager.requestPinAppWidget(clockProvider, null, null)
                        }
                        
                        val updateIntentClock = android.content.Intent(context, ClockWidget::class.java).apply {
                            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetManager.getAppWidgetIds(clockProvider))
                        }
                        context.sendBroadcast(updateIntentClock)

                        val updateIntentDate = android.content.Intent(context, com.example.nothingwidget.widgets.DateWidget::class.java).apply {
                            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                            val dateProvider = ComponentName(context, com.example.nothingwidget.widgets.DateWidget::class.java)
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetManager.getAppWidgetIds(dateProvider))
                        }
                        context.sendBroadcast(updateIntentDate)

                        val updateIntentBattery = android.content.Intent(context, com.example.nothingwidget.widgets.BatteryWidget::class.java).apply {
                            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                            val batteryProvider = ComponentName(context, com.example.nothingwidget.widgets.BatteryWidget::class.java)
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetManager.getAppWidgetIds(batteryProvider))
                        }
                        context.sendBroadcast(updateIntentBattery)

                        Toast.makeText(context, "Adding widget to home screen...", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ADD TO HOME SCREEN",
                    color = BackgroundColor,
                    style = Typography.bodyLarge
                )
            }
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Preview Box (4:3 ratio)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateContentSize()
                    .background(BackgroundColor)
                    .border(1.dp, SurfaceLowest, androidx.compose.ui.graphics.RectangleShape)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "$activeSize Widget Mode",
                    color = MutedOutline,
                    style = Typography.labelSmall,
                    modifier = Modifier.align(Alignment.TopStart)
                )

                // The Widget Canvas
                val (canvasWidth, canvasHeight) = when (activeSize) {
                    "2x1" -> 180.dp to 90.dp
                    "2x2" -> 180.dp to 180.dp
                    "4x1" -> 360.dp to 90.dp
                    "4x2" -> 360.dp to 180.dp
                    else -> 180.dp to 90.dp
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(width = canvasWidth, height = canvasHeight)
                        .border(1.dp, PrimaryText, androidx.compose.ui.graphics.RectangleShape)
                        .background(BackgroundColor)
                        .animateContentSize()
                        .padding(24.dp)
                        .wrapContentSize(unbounded = true),
                    contentAlignment = Alignment.Center
                ) {
                    val clockFontSize = when (activeSize) {
                        "2x1" -> 28.sp
                        "2x2" -> 44.sp
                        "4x1" -> 32.sp
                        "4x2" -> 58.sp
                        else -> 44.sp
                    }

                    val clockTextStyle = when (activeStyle) {
                        "Bold" -> TextStyle(color = activeColor, fontWeight = FontWeight.Bold, fontSize = (clockFontSize.value + 4f).sp)
                        "Outlined" -> TextStyle(
                            color = Color.Transparent,
                            drawStyle = Stroke(miter = 10f, width = 4f, join = StrokeJoin.Round),
                            fontSize = clockFontSize,
                            fontWeight = FontWeight.Bold
                        )
                        else -> TextStyle(color = activeColor, fontWeight = FontWeight.Normal, fontSize = clockFontSize)
                    }

                    val dotStyle = when (activeStyle) {
                        "Outlined" -> TextStyle(
                            color = Color.Transparent,
                            drawStyle = Stroke(miter = 10f, width = 4f, join = StrokeJoin.Round),
                            fontSize = clockFontSize,
                            fontWeight = FontWeight.Bold
                        )
                        else -> TextStyle(color = AccentRed, fontSize = clockFontSize)
                    }

                    val currentTime = LocalTime.now()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentTime.format(DateTimeFormatter.ofPattern("HH")),
                            fontFamily = SpaceMono,
                            style = clockTextStyle
                        )
                        Text(
                            text = "·",
                            fontFamily = SpaceMono,
                            style = dotStyle,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Text(
                            text = currentTime.format(DateTimeFormatter.ofPattern("mm")),
                            fontFamily = SpaceMono,
                            style = clockTextStyle
                        )
                    }
                }

                // Two small white squares bottom right
                Row(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(PrimaryText))
                    Box(modifier = Modifier.size(6.dp).background(PrimaryText))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SIZE row
            SectionHeader("SIZE")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val sizes = listOf("2x1", "2x2", "4x1", "4x2")
                sizes.forEach { size ->
                    SelectableButton(
                        text = size,
                        isSelected = activeSize == size,
                        onClick = { activeSize = size },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // STYLE row
            SectionHeader("STYLE")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val styles = listOf("Minimal", "Bold", "Outlined")
                styles.forEach { style ->
                    SelectableButton(
                        text = style,
                        isSelected = activeStyle == style,
                        onClick = { activeStyle = style }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // COLOR row
            SectionHeader("COLOR")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ColorButton(color = PrimaryText, isSelected = activeColor == PrimaryText) { activeColor = PrimaryText }
                ColorButton(color = AccentRed, isSelected = activeColor == AccentRed) { activeColor = AccentRed }
                ColorButton(color = MutedOutline, isSelected = activeColor == MutedOutline) { activeColor = MutedOutline }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).background(AccentRed))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, color = MutedOutline, style = Typography.labelSmall)
    }
}

@Composable
fun SelectableButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(if (isSelected) PrimaryText else BackgroundColor)
            .border(1.dp, if (isSelected) PrimaryText else MutedOutline, androidx.compose.ui.graphics.RectangleShape)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) BackgroundColor else MutedOutline,
            style = Typography.labelSmall
        )
    }
}

@Composable
fun ColorButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PrimaryText else MutedOutline,
                shape = androidx.compose.ui.graphics.RectangleShape
            )
            .clickable(onClick = onClick)
    )
}
