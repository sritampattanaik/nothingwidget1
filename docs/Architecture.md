NothingWidget — Architecture Document
Based on actual codebase analysis of 
sritampattanaik/nothingwidget1
1. Current Architecture (as-is)
What the code actually does today
com.example.nothingwidget/
├── MainActivity.kt              # ComponentActivity, hosts NavHost directly
├── ui/
│   ├── components/
│   │   ├── BottomNavBar.kt      # Composable, NavController passed in
│   │   ├── TopAppBar.kt         # Composable, stateless
│   │   └── WidgetCard.kt        # Composable, slot-based card
│   ├── screens/
│   │   ├── HomeScreen.kt        # Composable, NO ViewModel — state is local
│   │   ├── CustomizeScreen.kt   # Composable, reads SharedPreferences directly via LocalContext
│   │   └── SettingsScreen.kt    # Composable, local state only, settings don't actually persist
│   └── theme/
│       ├── Color.kt             # 6 color constants (no dynamic color support)
│       ├── Theme.kt             # Hardcoded dark-only MaterialTheme
│       └── Type.kt              # SpaceMono FontFamily + 3 text styles
└── widgets/
    ├── ClockWidget.kt           # AppWidgetProvider — reads SharedPrefs, sets text color only
    ├── DateWidget.kt            # AppWidgetProvider — reads SharedPrefs, sets text color only
    ├── BatteryWidget.kt         # AppWidgetProvider — reads battery level ✅, onReceive ✅
    └── WeatherWidget.kt         # AppWidgetProvider — hardcoded "24°", no real data
Architecture Pattern: None formally applied
No ViewModel
 anywhere — all state lives in 
remember {}
 inside Composables
No Repository
 — data access is direct 
SharedPreferences
 in Composables
No DataStore
 — using legacy 
SharedPreferences
No Hilt/Koin
 — no dependency injection
No StateFlow/LiveData
 — local Compose state only
Navigation
: String-literal routes (
"home"
, 
"customize/{widgetType}"
, 
"settings"
) — no type safety
What actually works
Feature
Status
Clock widget shows time
✅ Via 
TextClock
 XML (OS-driven, not app-driven)
Clock widget updates per minute
❌ 
updatePeriodMillis=60000
 is silently capped to 30min by Android
Clock color customization applies to widget
✅ 
setTextColor
 is called
Date widget shows date
✅ Via 
TextClock
 XML
Date color applies
✅
Battery widget shows %
✅
Battery updates on charge change
✅ 
onReceive
 handles 
ACTION_BATTERY_CHANGED
Battery color applies
✅
Weather shows real data
❌ Hardcoded "24°" / "CLEAR SKY"
Settings persist
❌ Local Compose state only, no persistence
Widgets survive reboot
❌ No BOOT_COMPLETED receiver
Widget update after reboot
❌ No WorkManager or AlarmManager scheduling
2. Target Architecture (to-be)
Pattern: Clean MVVM + Repository
Presentation Layer (Compose UI)
    ↓ observes StateFlow
ViewModel Layer (per screen)
    ↓ calls
Repository Layer (widget settings, system data)
    ↓ reads/writes
Data Layer (DataStore, BroadcastReceiver, AlarmManager, WorkManager, HTTP)
Folder Structure (target)
com.sritam.nothingwidget/          ← Fix: no com.example
├── di/
│   └── AppModule.kt               ← Hilt module
├── data/
│   ├── datastore/
│   │   ├── WidgetPreferencesDataStore.kt
│   │   └── AppPreferencesDataStore.kt
│   ├── repository/
│   │   ├── WidgetRepository.kt    ← Interface
│   │   ├── WidgetRepositoryImpl.kt
│   │   ├── BatteryRepository.kt
│   │   └── WeatherRepository.kt
│   ├── model/
│   │   ├── WidgetConfig.kt        ← Data class per widget type
│   │   ├── BatteryState.kt
│   │   └── WeatherData.kt
│   └── remote/
│       └── OpenMeteoApi.kt        ← Retrofit / Ktor for weather
├── domain/
│   └── usecase/
│       ├── GetWidgetConfigUseCase.kt
│       ├── SaveWidgetConfigUseCase.kt
│       └── GetBatteryStateUseCase.kt
├── ui/
│   ├── components/
│   │   ├── BottomNavBar.kt
│   │   ├── TopAppBar.kt
│   │   ├── WidgetCard.kt
│   │   └── SharpToggle.kt         ← Extract from SettingsScreen
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   └── HomeViewModel.kt
│   │   ├── customize/
│   │   │   ├── CustomizeScreen.kt
│   │   │   └── CustomizeViewModel.kt
│   │   ├── settings/
│   │   │   ├── SettingsScreen.kt
│   │   │   └── SettingsViewModel.kt
│   │   └── onboarding/
│   │       └── OnboardingScreen.kt
│   ├── navigation/
│   │   ├── NavGraph.kt
│   │   └── Screen.kt              ← Sealed class or @Serializable routes
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt               ← Add light scheme + dynamic color
│       └── Type.kt
├── widgets/
│   ├── clock/
│   │   ├── ClockWidget.kt         ← AppWidgetProvider
│   │   └── ClockWidgetUpdater.kt  ← AlarmManager scheduling logic
│   ├── date/
│   │   ├── DateWidget.kt
│   │   └── DateWidgetUpdater.kt
│   ├── battery/
│   │   └── BatteryWidget.kt       ← BroadcastReceiver (already good)
│   └── weather/
│       ├── WeatherWidget.kt
│       └── WeatherWorker.kt       ← WorkManager worker
├── receiver/
│   └── BootReceiver.kt            ← BOOT_COMPLETED → reschedule all widgets
└── MainActivity.kt
3. Navigation
Current (fragile string routes)
NavHost(startDestination = "home") {
    composable("home") { HomeScreen(navController) }
    composable("customize/{widgetType}") { ... }
    composable("settings") { ... }
}
Target (type-safe sealed class routes)
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Onboarding : Screen("onboarding")
    data class Customize(val widgetType: String) : Screen("customize/{widgetType}") {
        fun createRoute() = "customize/$widgetType"
    }
}
Or use Navigation Compose 2.8+ 
@Serializable
 objects for full type safety.
4. Widget Architecture
Current Problem
updatePeriodMillis = 60000
 → Android caps this at 1,800,000ms (30 min). The clock widget cannot update every minute using this mechanism alone.
Target Widget Update Architecture
ClockWidget (needs per-minute updates)
└── AlarmManager.setExactAndAllowWhileIdle()
    └── Fires ClockUpdateReceiver every 60s
        └── Calls AppWidgetManager.updateAppWidget()
DateWidget (needs midnight update)
└── AlarmManager.setExactAndAllowWhileIdle() at next midnight
    └── Fires DateUpdateReceiver
        └── Reschedules for the following midnight
BatteryWidget (needs event-driven updates)
└── BroadcastReceiver for ACTION_BATTERY_CHANGED ✅ (already done)
    └── Also listens for ACTION_POWER_CONNECTED / DISCONNECTED
WeatherWidget (needs periodic network fetch)
└── WorkManager PeriodicWorkRequest (min 15 min interval)
    └── WeatherWorker → OpenMeteo API → DataStore → updateAppWidget()
BootReceiver (ACTION_BOOT_COMPLETED)
└── Reschedules ClockWidget AlarmManager
└── Reschedules DateWidget AlarmManager
└── Enqueues WeatherWorker if not already running
Widget Info XML Changes Needed
<!-- Clock: set updatePeriodMillis to 0, use AlarmManager instead -->
<appwidget-provider
    android:updatePeriodMillis="0"
    android:targetCellWidth="4"        ← Add for Android 12+
    android:targetCellHeight="2"
    android:minResizeWidth="110dp"
    android:maxResizeWidth="250dp"
    android:previewLayout="@layout/widget_clock"   ← Add for Android 12 preview
    android:description="@string/widget_clock_description"  ← Accessibility
    ... />
5. Data Flow
Current (broken) flow
User taps "ADD" in CustomizeScreen
→ SharedPreferences.edit().putString(...).apply()
→ sendBroadcast(ACTION_APPWIDGET_UPDATE)
→ ClockWidget.onUpdate() reads SharedPreferences
→ sets textColor only (not textViewText for time)
Target flow
User adjusts slider in CustomizeScreen
→ CustomizeViewModel.updateColor(color)
→ WidgetRepository.saveConfig(widgetType, config)
→ DataStore.updateData { ... }
→ StateFlow emits new WidgetConfigState
→ CustomizeScreen recomposes with new preview
User taps "APPLY"
→ CustomizeViewModel.applyToWidget()
→ WidgetRepository.triggerUpdate(widgetType)
→ AppWidgetManager.updateAppWidget() called with new RemoteViews
→ Widget on home screen updates within 3 seconds
6. State Management
Per-screen ViewModel pattern
// CustomizeViewModel.kt
@HiltViewModel
class CustomizeViewModel @Inject constructor(
    private val widgetRepository: WidgetRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CustomizeUiState())
    val uiState: StateFlow<CustomizeUiState> = _uiState.asStateFlow()
    fun loadConfig(widgetType: WidgetType) {
        viewModelScope.launch {
            widgetRepository.getConfig(widgetType).collect { config ->
                _uiState.update { it.copy(config = config) }
            }
        }
    }
    fun updateColor(color: String) {
        _uiState.update { it.copy(config = it.config.copy(color = color)) }
    }
    fun applyToWidget(context: Context) {
        viewModelScope.launch {
            widgetRepository.saveAndApply(_uiState.value.config, context)
        }
    }
}
data class CustomizeUiState(
    val config: WidgetConfig = WidgetConfig(),
    val isLoading: Boolean = false,
    val error: String? = null
)
7. Dependency Graph (Target with Hilt)
@HiltAndroidApp Application
    ↓
@AndroidEntryPoint MainActivity
    ↓
@HiltViewModel CustomizeViewModel
    ↓ @Inject
WidgetRepositoryImpl : WidgetRepository
    ↓ @Inject
WidgetPreferencesDataStore   AppWidgetManagerWrapper
Hilt Module
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideWidgetPreferencesDataStore(@ApplicationContext ctx: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create { ctx.preferencesDataStoreFile("widget_prefs") }
    @Provides @Singleton
    fun provideWidgetRepository(dataStore: DataStore<Preferences>): WidgetRepository =
        WidgetRepositoryImpl(dataStore)
}
8. Persistence Layer
Current: SharedPreferences (fragile)
// BAD — direct SharedPreferences in a Composable
val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
prefs.edit().putString("clock_color", "#FF0000").apply()
Target: DataStore Preferences
object WidgetPreferencesKeys {
    val CLOCK_COLOR = stringPreferencesKey("clock_color")
    val CLOCK_STYLE = stringPreferencesKey("clock_style")
    val CLOCK_FONT = stringPreferencesKey("clock_font")
    val CLOCK_SIZE = stringPreferencesKey("clock_size")
    val CLOCK_BACKGROUND = stringPreferencesKey("clock_background")
    val CLOCK_FORMAT_24H = booleanPreferencesKey("clock_format_24h")
    // ... date, battery, weather equivalents
}
9. Third-Party Libraries (Current vs Target)
Current dependencies
[versions]
agp = "9.3.1"           # ⚠ Very new AGP version (verify stability)
kotlin = "2.2.10"       # ✅ Recent
composeBom = "2026.02.01" # ✅ Recent
navigationCompose = "2.8.5" # ✅ Good
Missing libraries
 (none of these are in 
libs.versions.toml
):
No Hilt
No DataStore
No WorkManager
No Retrofit/Ktor
No Coroutines (technically included via lifecycle-ktx but not explicit)
No Coil
No Kotlin Serialization
Target dependencies to add
[versions]
hilt = "2.51.1"
datastore = "1.1.1"
workmanager = "2.9.0"
retrofit = "2.11.0"
kotlinxSerialization = "1.7.1"
coil = "2.7.0"
[libraries]
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }
datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
workmanager-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "workmanager" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-kotlinx-serialization = { group = "com.squareup.retrofit2", name = "converter-kotlinx-serialization", version.ref = "retrofit" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
10. Network Layer (Weather Widget)
OpenMeteo API (free, no API key needed)
URL: https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true
WeatherWorker (WorkManager)
→ OkHttpClient or Retrofit call
→ Parse response (Kotlin Serialization)
→ Write to DataStore
→ AppWidgetManager.updateAppWidget() with new temperature text
No API key required for OpenMeteo — ideal for an offline-first, privacy-respecting app.
11. Testing Strategy
Current tests: 2 placeholder tests
ExampleUnitTest.kt
 — 
assertEquals(4, 2 + 2)
 (autogenerated, untouched)
ExampleInstrumentedTest.kt
 — package name check (autogenerated)
Target testing pyramid
Unit Tests (JUnit4 + MockK)
WidgetRepositoryImpl: verify DataStore reads/writes
CustomizeViewModel: verify state transitions
BatteryRepository: verify % calculation logic
DateFormatting: verify correct output for each format option
Integration Tests (Hilt testing + Robolectric)
DataStore: verify preferences survive simulated process restart
WorkManager: test WeatherWorker in isolation
UI Tests (Compose Testing)
HomeScreen: widget cards visible and clickable
CustomizeScreen: bottom sheet opens, size selector works, color selection works
Navigation: back stack correct after each navigation
Widget Tests
Manual QA on physical device for: post-reboot widget presence, Doze survival, AlarmManager firing
12. CI/CD Suggestions
# .github/workflows/android.yml
name: Android CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '21', distribution: 'temurin' }
      - run: ./gradlew test
      - run: ./gradlew lintDebug
      - run: ./gradlew assembleRelease   # with signing config
      - uses: actions/upload-artifact@v4
        with: { name: release-apk, path: app/build/outputs/apk/release/ }
13. Scalability Plan
Concern
Solution
More widget types
Add new package under 
widgets/
, register in Manifest, add DataStore keys
More customization options
Extend 
WidgetConfig
 data class, add UI section in CustomizeScreen
Multiple widget instances with different configs
Key DataStore by 
appWidgetId
 (Int), not by 
widgetType
 string
Tablet layouts
WindowSizeClass
 in HomeScreen/CustomizeScreen, responsive Compose layout
Localization
Move all 
"FIRMWARE_V2.0.4"
 etc. strings to 
strings.xml
; add 
translatable="false"
 for decorative ones