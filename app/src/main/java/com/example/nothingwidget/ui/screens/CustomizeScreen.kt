package com.example.nothingwidget.ui.screens

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nothingwidget.ui.theme.*
import com.example.nothingwidget.widgets.ClockWidget
import com.example.nothingwidget.widgets.DateWidget
import com.example.nothingwidget.widgets.BatteryWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(navController: NavController, widgetType: String = "clock") {
    val context = LocalContext.current
    val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    var selectedSize by remember { mutableStateOf("2x2") }
    var selectedStyle by remember { mutableStateOf("BOLD") }
    var selectedColor by remember { mutableStateOf("#FFFFFF") }
    var selectedFont by remember { mutableStateOf("Space Mono") }
    var selectedBackground by remember { mutableStateOf("OUTLINED BOX") }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                }
                Text(
                    text = "DIGITAL CLOCK",
                    color = PrimaryText,
                    fontFamily = SpaceMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Box(
                    modifier = Modifier
                        .background(PrimaryText)
                        .clickable {
                            Toast.makeText(context, "Widget updated", Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "ADD",
                        color = BackgroundColor,
                        fontFamily = SpaceMono,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .background(Color(0xFF1A1A1A))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Section 1 - SIZE
                Text("SIZE", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = SpaceMono)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val sizes = listOf("2x1", "2x2", "4x2", "5x3", "7x3")
                    items(sizes) { size ->
                        val isSelected = selectedSize == size
                        Box(
                            modifier = Modifier
                                .size(88.dp, 52.dp)
                                .background(if (isSelected) Color.White else Color(0xFF2A2A2A))
                                .border(1.dp, if (isSelected) Color.Transparent else Color.White)
                                .clickable { selectedSize = size },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = size,
                                color = if (isSelected) Color.Black else Color.White,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = SpaceMono
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Section 2 - STYLE
                Text("STYLE", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = SpaceMono)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("MINIMAL", "BOLD", "OUTLINED").forEach { style ->
                        val isSelected = selectedStyle == style
                        Box(
                            modifier = Modifier
                                .size(160.dp, 80.dp)
                                .background(Color(0xFF1A1A1A))
                                .border(if (isSelected) 2.dp else 1.dp, if (isSelected) Color.White else Color.Gray)
                                .clickable { selectedStyle = style },
                            contentAlignment = Alignment.Center
                        ) {
                            val textStyle = when(style) {
                                "BOLD" -> TextStyle(fontWeight = FontWeight.Black)
                                "OUTLINED" -> TextStyle(
                                    drawStyle = Stroke(miter = 10f, width = 4f, join = StrokeJoin.Round),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Transparent
                                )
                                else -> TextStyle(fontWeight = FontWeight.Normal)
                            }
                            Text(
                                text = style,
                                color = if (style == "OUTLINED") Color.Transparent else Color.White,
                                fontFamily = SpaceMono,
                                style = textStyle,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                // Section 3 - COLOR
                Text("COLOR", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = SpaceMono)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val colors = listOf(
                        "#FFFFFF" to Color.White,
                        "#FF3B30" to Color(0xFFFF3B30),
                        "#8E8E93" to Color(0xFF8E8E93),
                        "#34C759" to Color(0xFF34C759),
                        "#007AFF" to Color(0xFF007AFF),
                        "#FFD60A" to Color(0xFFFFD60A),
                        "#FF9500" to Color(0xFFFF9500),
                        "#AF52DE" to Color(0xFFAF52DE)
                    )
                    items(colors) { (hex, composeColor) ->
                        val isSelected = selectedColor == hex
                        Box(
                            modifier = Modifier
                                .size(72.dp, 72.dp)
                                .background(composeColor)
                                .border(if (isSelected) 2.dp else 0.dp, Color.White)
                                .clickable { selectedColor = hex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 4 - FONT
                Text("FONT", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = SpaceMono)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val fonts = listOf(
                        "Sans Thin" to (FontFamily.SansSerif to FontWeight.Light),
                        "Sans Regular" to (FontFamily.SansSerif to FontWeight.Normal),
                        "Space Mono" to (SpaceMono to FontWeight.Bold),
                        "Serif" to (FontFamily.Serif to FontWeight.Normal),
                        "Monospace" to (FontFamily.Monospace to FontWeight.Normal)
                    )
                    items(fonts) { (name, fontData) ->
                        val isSelected = selectedFont == name
                        Box(
                            modifier = Modifier
                                .size(100.dp, 72.dp)
                                .background(Color(0xFF1A1A1A))
                                .border(if (isSelected) 2.dp else 1.dp, if (isSelected) Color.White else Color.Gray)
                                .clickable { selectedFont = name },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "22:47",
                                color = Color.White,
                                fontFamily = fontData.first,
                                fontWeight = fontData.second,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 5 - BACKGROUND
                Text("BACKGROUND", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = SpaceMono)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val bgs = listOf("TRANSPARENT", "SOLID BLACK", "OUTLINED BOX")
                    bgs.forEach { bg ->
                        val isSelected = selectedBackground == bg
                        Box(
                            modifier = Modifier
                                .size(160.dp, 90.dp)
                                .background(if (bg == "SOLID BLACK" || bg == "OUTLINED BOX") Color.Black else Color(0xFF1A1A1A))
                                .border(if (isSelected) 2.dp else if (bg == "OUTLINED BOX") 2.dp else 1.dp, if (isSelected) Color.White else Color.Gray)
                                .clickable { selectedBackground = bg },
                            contentAlignment = Alignment.Center
                        ) {
                            if (bg == "TRANSPARENT") {
                                // Checkerboard overlay representation could just be dots for now
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    for (x in 0..size.width.toInt() step 20) {
                                        for (y in 0..size.height.toInt() step 20) {
                                            drawRect(Color.DarkGray, Offset(x.toFloat(), y.toFloat()), androidx.compose.ui.geometry.Size(10f, 10f))
                                        }
                                    }
                                }
                            }
                            Text(text = bg, color = if (bg == "TRANSPARENT") Color.Gray else Color.White, fontFamily = SpaceMono, fontSize = 12.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        },
        sheetContainerColor = Color(0xFF1A1A1A),
        sheetDragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    when (selectedBackground) {
                        "SOLID BLACK", "OUTLINED BOX" -> Color.Black
                        else -> Color(0xFF1A1A1A)
                    }
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val dotSpacing = 8.dp.toPx()
                val dotRadius = 1.dp.toPx()
                for (x in 0..size.width.toInt() step dotSpacing.toInt()) {
                    for (y in 0..size.height.toInt() step dotSpacing.toInt()) {
                        drawCircle(
                            color = Color.DarkGray.copy(alpha = 0.3f),
                            radius = dotRadius,
                            center = Offset(x.toFloat(), y.toFloat())
                        )
                    }
                }
                val lineSpacing = 4.dp.toPx()
                for (y in 0..size.height.toInt() step lineSpacing.toInt()) {
                    drawLine(
                        color = Color.Black.copy(alpha = 0.2f),
                        start = Offset(0f, y.toFloat()),
                        end = Offset(size.width, y.toFloat()),
                        strokeWidth = 1f
                    )
                }
            }
            
            if (selectedBackground == "OUTLINED BOX") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.White)
                )
            }
            
            Text(
                text = "SYSTEM_TIME_LOCAL",
                color = Color.Gray,
                fontFamily = SpaceMono,
                fontSize = 10.sp,
                modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
            )
            Text(
                text = "SYNC_ACTIVE",
                color = AccentRed,
                fontFamily = SpaceMono,
                fontSize = 10.sp,
                modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)
            )
            
            // Dynamic Preview applying selected styles
            val previewFont = when(selectedFont) {
                "Sans Thin" -> FontFamily.SansSerif
                "Sans Regular" -> FontFamily.SansSerif
                "Space Mono" -> SpaceMono
                "Serif" -> FontFamily.Serif
                "Monospace" -> FontFamily.Monospace
                else -> SpaceMono
            }
            val previewFontWeight = when(selectedFont) {
                "Sans Thin" -> FontWeight.Light
                "Space Mono" -> FontWeight.Bold
                else -> FontWeight.Normal
            }
            val previewColor = Color(android.graphics.Color.parseColor(selectedColor))
            
            val baseFontSize = when (selectedSize) {
                "2x1" -> 40.sp
                "2x2" -> 60.sp
                "4x2" -> 90.sp
                "5x3" -> 110.sp
                else -> 60.sp
            }
            
            val textStyle = when(selectedStyle) {
                "BOLD" -> TextStyle(color = previewColor, fontWeight = FontWeight.Black, fontFamily = previewFont, fontSize = baseFontSize)
                "OUTLINED" -> TextStyle(
                    color = Color.Transparent,
                    drawStyle = Stroke(miter = 10f, width = 6f, join = StrokeJoin.Round),
                    fontWeight = FontWeight.Bold,
                    fontFamily = previewFont,
                    fontSize = baseFontSize
                )
                else -> TextStyle(color = previewColor, fontWeight = previewFontWeight, fontFamily = previewFont, fontSize = baseFontSize)
            }
            
            Box(
                modifier = Modifier.align(Alignment.Center).animateContentSize()
            ) {
                Text(
                    text = "22:47",
                    style = textStyle
                )
            }
            
            Text(
                text = "PRECISION_QUARTZ_ENGINE LON: -0.1273 | LAT: 51.6974",
                color = Color.Gray,
                fontFamily = SpaceMono,
                fontSize = 10.sp,
                modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter)
            )
        }
    }
}
