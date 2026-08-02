package com.example.nothingwidget.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.nothingwidget.R

class DateWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val savedColor = prefs.getString("clock_color", "#FFFFFF") ?: "#FFFFFF"
        val savedStyle = prefs.getString("clock_style", "Minimal") ?: "Minimal"

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_date)
            
            views.setTextColor(R.id.widget_date_text, android.graphics.Color.parseColor(savedColor))
            
            if (savedStyle == "Bold") {
                views.setInt(R.id.widget_date_text, "setTypeface", android.graphics.Typeface.BOLD)
            }
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
