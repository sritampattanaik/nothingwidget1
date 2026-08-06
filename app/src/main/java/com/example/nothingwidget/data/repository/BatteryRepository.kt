package com.example.nothingwidget.data.repository

import com.example.nothingwidget.domain.model.BatteryInfo
import com.example.nothingwidget.domain.model.ConnectedDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BatteryRepository {
    private val _batteryState = MutableStateFlow(
        BatteryInfo(
            percentage = 88,
            isCharging = false,
            isPowerSaveMode = false,
            estimatedHoursRemaining = 19,
            temperatureCelsius = 30.2f,
            healthStatus = "Good",
            connectedDevices = listOf(
                ConnectedDevice("Ear (2)", 95, true),
                ConnectedDevice("Watch Pro", 72, true)
            )
        )
    )

    val batteryState: Flow<BatteryInfo> = _batteryState.asStateFlow()

    fun toggleCharging() {
        val current = _batteryState.value
        _batteryState.value = current.copy(isCharging = !current.isCharging)
    }

    fun togglePowerSave() {
        val current = _batteryState.value
        _batteryState.value = current.copy(isPowerSaveMode = !current.isPowerSaveMode)
    }
}
