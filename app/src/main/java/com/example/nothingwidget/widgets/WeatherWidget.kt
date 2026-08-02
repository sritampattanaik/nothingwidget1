package com.example.nothingwidget.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.nothingwidget.R
import android.graphics.Color

class WeatherWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val savedColor = prefs.getString("weather_color", "#FFFFFF") ?: "#FFFFFF"
        val savedUnit = prefs.getString("weather_unit", "°C") ?: "°C"
        
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_weather)
            
            views.setTextColor(R.id.widget_weather_temp, Color.parseColor(savedColor))
            views.setTextViewText(R.id.widget_weather_temp, if (savedUnit == "°C") "24°" else "75°")
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
