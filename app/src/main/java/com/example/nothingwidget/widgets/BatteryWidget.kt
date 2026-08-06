package com.example.nothingwidget.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import com.example.nothingwidget.R
import com.example.nothingwidget.data.local.AppDatabase
import com.example.nothingwidget.data.repository.BatteryRepository
import com.example.nothingwidget.data.repository.WidgetRepository
import com.example.nothingwidget.domain.model.WidgetType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BatteryWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_POWER_CONNECTED || intent.action == Intent.ACTION_POWER_DISCONNECTED) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, BatteryWidget::class.java)
            )
            updateWidgets(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        if (appWidgetIds.isEmpty()) return

        val pendingResult = goAsync()
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val repo = WidgetRepository(db.widgetConfigDao())
                val configs = repo.allConfigs.first()
                val batteryConfig = configs.find { it.type == WidgetType.BATTERY_CIRCLE }
                
                // Parse color from config, fallback to white if not found
                val colorHex = batteryConfig?.accentColorHex ?: "#FFFFFF"
                val parsedColor = try { Color.parseColor(colorHex) } catch (e: Exception) { Color.WHITE }

                val batteryInfo = BatteryRepository.getBatteryInfoSync(context)

                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context.packageName, R.layout.widget_battery)
                    
                    views.setTextViewText(R.id.widget_battery_text, "${batteryInfo.percentage}%")
                    views.setProgressBar(R.id.battery_progress, 100, batteryInfo.percentage, false)
                    
                    // The original widget_battery had text view we can tint
                    views.setTextColor(R.id.widget_battery_text, parsedColor)
                    
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
