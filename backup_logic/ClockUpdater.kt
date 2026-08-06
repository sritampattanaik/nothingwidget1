package com.example.nothingwidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.nothingwidget.widgets.ClockWidget

object ClockUpdater {
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ClockWidget::class.java).apply {
            action = "android.appwidget.action.APPWIDGET_UPDATE"
            // We do not pass appWidgetIds, so the receiver should update all widgets
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // For Phase 0, we can just use setInexactRepeating for 1 minute intervals
        // Phase 2 will implement exact next-minute boundary logic
        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            System.currentTimeMillis() + 60000,
            60000,
            pendingIntent
        )
    }
}
