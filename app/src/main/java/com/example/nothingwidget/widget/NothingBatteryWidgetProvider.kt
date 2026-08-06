package com.example.nothingwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.nothingwidget.R

class NothingBatteryWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_battery_layout)
            views.setTextViewText(R.id.widget_battery_percent, "88%")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
