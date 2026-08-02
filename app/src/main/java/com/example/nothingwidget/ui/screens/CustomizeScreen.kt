package com.example.nothingwidget.ui.screens

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
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
            ) {
                Text("Sections Placeholder", color = Color.White)
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
                .background(Color.Black)
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.White)
            )
            
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
            Text(
                text = "22:47",
                color = Color.White,
                fontFamily = SpaceMono,
                fontWeight = FontWeight.Bold,
                fontSize = 80.sp,
                modifier = Modifier.align(Alignment.Center)
            )
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
