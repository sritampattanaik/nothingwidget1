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
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeJoin
import com.example.nothingwidget.widgets.ClockWidget
import com.example.nothingwidget.widgets.DateWidget
import com.example.nothingwidget.widgets.BatteryWidget

@Composable
fun CustomizeScreen(navController: NavController, widgetType: String = "clock") {
    var activeSize by remember { mutableStateOf(if (widgetType == "date") "4x1" else "2x2") }
    var activeStyle by remember { mutableStateOf("Minimal") }
    var activeColor by remember { mutableStateOf(PrimaryText) }
    var activeFormat by remember { mutableStateOf("MON 28 JULY") }
    var activeShowPercentage by remember { mutableStateOf("on") }

    val context = LocalContext.current
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val prefs = context.getSharedPreferences("widget_prefs", android.content.Context.MODE_PRIVATE)

    val selectedColorHex = when (activeColor) {
        AccentRed -> "#FF5540"
        MutedOutline -> "#8E9192"
        else -> "#FFFFFF"
    }

    LaunchedEffect(activeColor, activeStyle, activeSize, activeFormat, activeShowPercentage) { 
        prefs.edit()
            .putString("${widgetType}_color", selectedColorHex)
            .putString("${widgetType}_style", activeStyle)
            .putString("${widgetType}_size", activeSize)
            .putString("${widgetType}_format", activeFormat)
            .putString("${widgetType}_show_percentage", activeShowPercentage)
            .apply()
    }

    val screenTitle = when (widgetType) {
        "clock" -> "DIGITAL CLOCK"
        "date" -> "DATE WIDGET"
        "battery" -> "BATTERY WIDGET"
        "weather" -> "WEATHER WIDGET"
        else -> "CUSTOMIZE"
    }

    Scaffold(
        topBar = {
            NothingTopAppBar(
                title = screenTitle,
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
                        val widgetClass = when (widgetType) {
                            "clock" -> ClockWidget::class.java
                            "date" -> DateWidget::class.java
                            "battery" -> BatteryWidget::class.java
                            else -> ClockWidget::class.java
                        }
                        val provider = ComponentName(context, widgetClass)
                        if (appWidgetManager.isRequestPinAppWidgetSupported) {
                            appWidgetManager.requestPinAppWidget(provider, null, null)
                        }
                        
                        val ids = appWidgetManager.getAppWidgetIds(provider)
                        val updateIntent = Intent(context, widgetClass).apply {
                            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                        }
                        context.sendBroadcast(updateIntent)
                        
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
                    val fontSize = when (activeSize) {
                        "2x1" -> 28.sp
                        "2x2" -> 44.sp
                        "4x1" -> 32.sp
                        "4x2" -> 58.sp
                        else -> 44.sp
                    }

                    val textStyle = when (activeStyle) {
                        "Bold" -> TextStyle(color = activeColor, fontWeight = FontWeight.Bold, fontSize = (fontSize.value + 4f).sp)
                        "Outlined" -> TextStyle(
                            color = Color.Transparent,
                            drawStyle = Stroke(miter = 10f, width = 4f, join = StrokeJoin.Round),
                            fontSize = fontSize,
                            fontWeight = FontWeight.Bold
                        )
                        else -> TextStyle(color = activeColor, fontWeight = FontWeight.Normal, fontSize = fontSize)
                    }

                    when (widgetType) {
                        "clock" -> {
                            val dotStyle = when (activeStyle) {
                                "Outlined" -> TextStyle(
                                    color = Color.Transparent,
                                    drawStyle = Stroke(miter = 10f, width = 4f, join = StrokeJoin.Round),
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Bold
                                )
                                else -> TextStyle(color = AccentRed, fontSize = fontSize)
                            }
                            val currentTime = LocalTime.now()
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentTime.format(DateTimeFormatter.ofPattern("HH")),
                                    fontFamily = SpaceMono,
                                    style = textStyle
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
                                    style = textStyle
                                )
                            }
                        }
                        "date" -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val dateText = when (activeFormat) {
                                    "28.07.2024" -> "28.07.2024"
                                    "MONDAY" -> "MONDAY"
                                    else -> "MON · 28 JULY"
                                }
                                Text(
                                    text = dateText,
                                    fontFamily = SpaceMono,
                                    style = textStyle,
                                    color = activeColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PrimaryText))
                            }
                        }
                        "battery" -> {
                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                                if (activeShowPercentage == "on") {
                                    Text(
                                        text = "87%",
                                        fontFamily = SpaceMono,
                                        style = textStyle,
                                        color = activeColor
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
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
                                            .background(activeColor)
                                    )
                                }
                            }
                        }
                        else -> {
                            // Weather or other defaults
                        }
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
                val sizes = when (widgetType) {
                    "clock" -> listOf("2x1", "2x2", "4x1", "4x2")
                    "date" -> listOf("4x1", "2x2")
                    "battery" -> listOf("2x1", "4x1")
                    else -> listOf("2x2")
                }
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
                val styles = when (widgetType) {
                    "clock" -> listOf("Minimal", "Bold", "Outlined")
                    else -> listOf("Minimal", "Bold")
                }
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
            
            if (widgetType == "date") {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("DATE FORMAT")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("MON 28 JULY", "28.07.2024", "MONDAY").forEach { fmt ->
                        SelectableButton(
                            text = fmt,
                            isSelected = activeFormat == fmt,
                            onClick = { activeFormat = fmt }
                        )
                    }
                }
            }

            if (widgetType == "battery") {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("SHOW PERCENTAGE")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("on", "off").forEach { pct ->
                        SelectableButton(
                            text = pct.uppercase(),
                            isSelected = activeShowPercentage == pct,
                            onClick = { activeShowPercentage = pct },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
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
