package com.example.nothingwidget.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Color
import android.widget.RemoteViews
import com.example.nothingwidget.R
import com.example.nothingwidget.data.local.AppDatabase
import com.example.nothingwidget.data.repository.WidgetRepository
import com.example.nothingwidget.domain.model.WidgetType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClockWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val pendingResult = goAsync()
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val repo = WidgetRepository(db.widgetConfigDao())
                val configs = repo.allConfigs.first()
                val clockConfig = configs.find { it.type == WidgetType.DIGITAL_CLOCK }
                
                // Parse color from config, fallback to white if not found
                val colorHex = clockConfig?.accentColorHex ?: "#FFFFFF"
                val parsedColor = try { Color.parseColor(colorHex) } catch (e: Exception) { Color.WHITE }

                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context.packageName, R.layout.widget_clock_layout)
                    
                    // Apply color if supported by the layout (widget_clock_time might not exist in old layout, but it does in widget_clock_layout)
                    views.setTextColor(R.id.widget_clock_time, parsedColor)
                    views.setTextColor(R.id.widget_clock_date, parsedColor)

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
