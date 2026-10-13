package com.duckets.junowear

import android.content.Context
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay

/**
 * JunoWear v0.1 — "Charm": a Wear OS conversion of muse-gadget-206 (Muse side,
 * gloss blue) and HermesGadget (Hermes side, gloss gold on indigo).
 * v0.1 is UI shells + real clock/battery only. No link, no network, no fake data.
 */

enum class Screen { HOME, MUSE, MUSE_CHAT, MUSE_MENU, MUSE_SETTINGS, HERMES }

private val JunoWearColors = Colors(
    primary = MuseTokens.Blue,
    primaryVariant = MuseTokens.BlueDeep,
    secondary = MuseTokens.Blue,
    secondaryVariant = MuseTokens.BlueDeep,
    background = MuseTokens.Night,
    surface = MuseTokens.Bubble,
    error = MuseTokens.StateError,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = MuseTokens.Text,
    onSurface = MuseTokens.Text,
    onError = Color.Black
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = JunoWearColors) {
                CharmApp()
            }
        }
    }
}

/** Wall-clock milliseconds, refreshed 4x/sec so clock + battery stay live. */
@Composable
fun rememberTick(): Long {
    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            tick = System.currentTimeMillis()
            delay(250)
        }
    }
    return tick
}

fun readBatteryPercent(context: Context): Int {
    val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
}

@Composable
private fun CharmApp() {
    var screen by remember { mutableStateOf(Screen.HOME) }
    BackHandler(enabled = screen != Screen.HOME) { screen = Screen.HOME }

    when (screen) {
        Screen.HOME -> HomeScreen(onNavigate = { screen = it })
        Screen.MUSE -> MuseScreen(onNavigate = { screen = it })
        Screen.MUSE_CHAT -> MuseChatScreen(onBack = { screen = Screen.MUSE })
        Screen.MUSE_MENU -> MuseMenuScreen(
            onBack = { screen = Screen.MUSE },
            onOpenChat = { screen = Screen.MUSE_CHAT }
        )
        Screen.MUSE_SETTINGS -> MuseSettingsScreen(onBack = { screen = Screen.MUSE })
        Screen.HERMES -> HermesScreen(onBack = { screen = Screen.HOME })
    }
}

@Composable
private fun HomeScreen(onNavigate: (Screen) -> Unit) {
    val context = LocalContext.current
    val tick = rememberTick()
    val timeText = remember(tick) { SimpleDateFormat("h:mm:ss a", Locale.US).format(Date(tick)) }
    val dateText = remember(tick) { SimpleDateFormat("EEE, MMM d", Locale.US).format(Date(tick)) }
    val batteryPct = remember(tick) { readBatteryPercent(context) }
    val listState = rememberScalingLazyListState()

    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
            item {
                Text(
                    text = "JunoWear",
                    style = MaterialTheme.typography.title1,
                    color = MuseTokens.Blue,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.display1,
                    color = MuseTokens.Text,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.body1,
                    color = MuseTokens.Dim,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            item {
                NavChip(
                    label = "Battery",
                    value = if (batteryPct in 0..100) "$batteryPct%" else "unknown",
                    background = MuseTokens.Bubble
                ) {}
            }
            item {
                Text(
                    text = "MUSE",
                    style = MaterialTheme.typography.caption1,
                    color = MuseTokens.Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                )
            }
            item {
                NavChip(
                    label = "Muse",
                    value = "Pseudo charm — not linked",
                    background = MuseTokens.Bubble
                ) { onNavigate(Screen.MUSE) }
            }
            item {
                Text(
                    text = "HERMES",
                    style = MaterialTheme.typography.caption1,
                    color = HermesTokens.Gold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                )
            }
            item {
                NavChip(
                    label = "Hermes",
                    value = "Hub link offline",
                    background = HermesTokens.Surface
                ) { onNavigate(Screen.HERMES) }
            }
            item {
                NavChip(
                    label = "Weather",
                    value = "No data yet",
                    background = MuseTokens.Bubble
                ) {}
            }
            item {
                NavChip(
                    label = "Health",
                    value = "Sensors not wired yet",
                    background = MuseTokens.Bubble
                ) {}
            }
        }
    }
}

@Composable
fun NavChip(label: String, value: String, background: Color, onClick: () -> Unit) {
    Chip(
        onClick = onClick,
        label = { Text(text = label, style = MaterialTheme.typography.button) },
        secondaryLabel = { Text(text = value, color = MuseTokens.Dim) },
        colors = ChipDefaults.chipColors(backgroundColor = background),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    )
}

@Composable
fun BackChip(onBack: () -> Unit) {
    Chip(
        onClick = onBack,
        label = {
            Text(
                text = "Back",
                style = MaterialTheme.typography.button,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = ChipDefaults.chipColors(backgroundColor = MuseTokens.Input),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    )
}
