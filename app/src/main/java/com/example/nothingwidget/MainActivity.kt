package com.example.nothingwidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nothingwidget.ui.screens.HomeScreen
import com.example.nothingwidget.ui.screens.CustomizeScreen
import com.example.nothingwidget.ui.screens.SettingsScreen
import com.example.nothingwidget.ui.theme.NothingWidgetTheme
import com.example.nothingwidget.ui.theme.BackgroundColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NothingWidgetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundColor
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { HomeScreen(navController) }
                        composable("customize/{widgetType}") { backStackEntry ->
                            val widgetType = backStackEntry.arguments?.getString("widgetType") ?: "clock"
                            CustomizeScreen(navController = navController, widgetType = widgetType)
                        }
                        composable("settings") { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}