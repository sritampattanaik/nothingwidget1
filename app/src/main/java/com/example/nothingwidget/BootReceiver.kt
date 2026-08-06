package com.example.nothingwidget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nothingwidget.widgets.BatteryWidget
import com.example.nothingwidget.widgets.ClockWidget
import com.example.nothingwidget.widgets.DateWidget
import com.example.nothingwidget.widgets.WeatherWidget
import com.example.nothingwidget.worker.WeatherWorker
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            
            // Manually ping all widgets so they aren't blank for 10-15s
            val widgetManager = AppWidgetManager.getInstance(context)
            val widgetClasses = listOf(
                ClockWidget::class.java,
                DateWidget::class.java,
                BatteryWidget::class.java,
                WeatherWidget::class.java
            )

            widgetClasses.forEach { providerClass ->
                val ids = widgetManager.getAppWidgetIds(ComponentName(context, providerClass))
                if (ids.isNotEmpty()) {
                    val updateIntent = Intent(context, providerClass).apply {
                        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                    }
                    context.sendBroadcast(updateIntent)
                }
            }

            val weatherWorkRequest = PeriodicWorkRequestBuilder<WeatherWorker>(
                15, TimeUnit.MINUTES,
                5, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "WeatherUpdateWork",
                ExistingPeriodicWorkPolicy.KEEP,
                weatherWorkRequest
            )
        }
    }
}
