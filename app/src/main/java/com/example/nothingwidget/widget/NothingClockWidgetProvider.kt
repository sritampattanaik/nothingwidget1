package com.example.nothingwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.nothingwidget.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NothingClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateClockWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateClockWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_clock_layout)
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val currentDate = SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date()).uppercase()

            views.setTextViewText(R.id.widget_clock_time, currentTime)
            views.setTextViewText(R.id.widget_clock_date, currentDate)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
