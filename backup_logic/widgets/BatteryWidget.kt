package com.example.nothingwidget.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.RemoteViews
import com.example.nothingwidget.R

class BatteryWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val batteryIntent = context.applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = if (level != -1 && scale != -1) {
            (level * 100 / scale.toFloat()).toInt()
        } else {
            100
        }

        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val savedColor = prefs.getString("battery_color", "#FFFFFF") ?: "#FFFFFF"
        val savedStyle = prefs.getString("battery_style", "Minimal") ?: "Minimal"

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_battery)
            views.setTextViewText(R.id.widget_battery_text, "$batteryPct%")
            views.setProgressBar(R.id.battery_progress, 100, batteryPct, false)
            
            views.setTextColor(R.id.widget_battery_text, android.graphics.Color.parseColor(savedColor))
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, BatteryWidget::class.java)
            )
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = if (level != -1 && scale != -1) {
                (level * 100 / scale.toFloat()).toInt()
            } else {
                -1
            }

            if (batteryPct != -1) {
                val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
                val savedColor = prefs.getString("battery_color", "#FFFFFF") ?: "#FFFFFF"
                val savedStyle = prefs.getString("battery_style", "Minimal") ?: "Minimal"

                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context.packageName, R.layout.widget_battery)
                    views.setTextViewText(R.id.widget_battery_text, "$batteryPct%")
                    views.setProgressBar(R.id.battery_progress, 100, batteryPct, false)
                    
                    views.setTextColor(R.id.widget_battery_text, android.graphics.Color.parseColor(savedColor))
                    
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }
    }
}
