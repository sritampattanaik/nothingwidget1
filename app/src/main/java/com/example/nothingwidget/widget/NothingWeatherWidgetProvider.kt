package com.example.nothingwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.nothingwidget.R

class NothingWeatherWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_weather_layout)
            views.setTextViewText(R.id.widget_weather_city, "LONDON")
            views.setTextViewText(R.id.widget_weather_temp, "22°C • PARTLY CLOUDY")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
