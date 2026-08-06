package com.example.nothingwidget.worker

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nothingwidget.widgets.WeatherWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Phase 7 will fetch actual data here and save to DataStore/Room.
            // For now, we just broadcast the update to the widget.
            
            val updateIntent = Intent(context, WeatherWidget::class.java).apply {
                action = "android.appwidget.action.APPWIDGET_UPDATE"
            }
            context.sendBroadcast(updateIntent)
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
