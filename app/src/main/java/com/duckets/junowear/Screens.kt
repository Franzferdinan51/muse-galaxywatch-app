package com.duckets.junowear

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
 * Muse + Hermes screens, mirroring the ESP32 firmwares' structure:
 *  - muse-gadget-206: home (avatar + state word + clock), chat with PTT,
 *    menu, settings — gloss blue, tokens from muse_theme.h.
 *  - HermesGadget: home with link status, pairing area, yes/no prompt card —
 *    gloss gold on deep indigo.
 * v0.1: UI shells only. Nothing here is linked; every status says so.
 */

/** Placeholder for the muse_pixel avatar — gloss disc + status dot.
 *  Real avatar rendering is a later wave. The dot/word use Dim, not a state
 *  accent, because there is no live link state to report yet. */
@Composable
private fun AvatarDisc() {
    Box(
        modifier = Modifier
            .size(92.dp)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    listOf(MuseTokens.BubbleTop, MuseTokens.Bubble, MuseTokens.BlueDeep)
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(MuseTokens.Dim)
        )
    }
}

@Composable
fun MuseScreen(onNavigate: (Screen) -> Unit) {
    val tick = rememberTick()
    val timeText = remember(tick) { SimpleDateFormat("h:mm a", Locale.US).format(Date(tick)) }
    val listState = rememberScalingLazyListState()

    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
            item { AvatarDisc() }
            item {
                Text(
                    text = "NOT LINKED",
                    style = MaterialTheme.typography.caption1,
                    color = MuseTokens.Dim,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            item {
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.title1,
                    color = MuseTokens.Text,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    text = "Muse pairs only through Meta's app — this is a shell.",
                    style = MaterialTheme.typography.caption2,
                    color = MuseTokens.Caption,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            item {
                NavChip("Chat", "Caption + talk button shell", MuseTokens.Bubble) {
                    onNavigate(Screen.MUSE_CHAT)
                }
            }
            item {
                NavChip("Menu", "Muse sections", MuseTokens.Bubble) {
                    onNavigate(Screen.MUSE_MENU)
                }
            }
            item {
                NavChip("Settings", "Account, voice, about", MuseTokens.Bubble) {
                    onNavigate(Screen.MUSE_SETTINGS)
                }
            }
            item { BackChip { onNavigate(Screen.HOME) } }
        }
    }
}

@Composable
fun MuseChatScreen(onBack: () -> Unit) {
    val listState = rememberScalingLazyListState()
    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
            item {
                Text(
                    text = "Chat",
                    style = MaterialTheme.typography.title2,
                    color = MuseTokens.Blue,
                    textAlign = TextAlign.Center
                )
            }
            item {
                // Reply bubble: surface gradient per muse_theme_bubble.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(MuseTokens.BubbleTop, MuseTokens.Bubble, MuseTokens.BubbleBot)
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(16.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "No conversation yet — Muse isn't linked.",
                        style = MaterialTheme.typography.body2,
                        color = MuseTokens.Caption
                    )
                }
            }
            item {
                // Push-to-talk button (UI only in v0.1).
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(MuseTokens.BlueTop, MuseTokens.Blue, MuseTokens.BlueBot)
                            )
                        )
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "TALK",
                        style = MaterialTheme.typography.button,
                        color = Color.White
                    )
                }
            }
            item {
                Text(
                    text = "Not linked",
                    style = MaterialTheme.typography.caption2,
                    color = MuseTokens.Dim,
                    textAlign = TextAlign.Center
                )
            }
            item { BackChip(onBack) }
        }
    }
}

@Composable
fun MuseMenuScreen(onBack: () -> Unit, onOpenChat: () -> Unit) {
    val listState = rememberScalingLazyListState()
    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
            item {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.title2,
                    color = MuseTokens.Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            item { NavChip("Chat", "Open the chat shell", MuseTokens.Bubble, onOpenChat) }
            item { NavChip("My AI", "Later wave", MuseTokens.Bubble) {} }
            item { NavChip("Memories", "Later wave", MuseTokens.Bubble) {} }
            item { NavChip("Reminders", "Later wave", MuseTokens.Bubble) {} }
            item { BackChip(onBack) }
        }
    }
}

@Composable
fun MuseSettingsScreen(onBack: () -> Unit) {
    val listState = rememberScalingLazyListState()
    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
            item {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.title2,
                    color = MuseTokens.Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            item { NavChip("Account", "Not signed in", MuseTokens.Bubble) {} }
            item { NavChip("Voice", "Not linked", MuseTokens.Bubble) {} }
            item { NavChip("Brightness", "System", MuseTokens.Bubble) {} }
            item { NavChip("About", "Charm v0.1 — pseudo Muse", MuseTokens.Bubble) {} }
            item { BackChip(onBack) }
        }
    }
}

@Composable
fun HermesScreen(onBack: () -> Unit) {
    val listState = rememberScalingLazyListState()
    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
            item {
                // Hermes mark: gold-ringed disc on indigo.
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(HermesTokens.Surface, HermesTokens.SurfaceBot)
                            )
                        )
                        .border(1.dp, HermesTokens.Gold.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "H",
                        style = MaterialTheme.typography.display2,
                        color = HermesTokens.Gold
                    )
                }
            }
            item {
                Text(
                    text = "HERMES",
                    style = MaterialTheme.typography.title2,
                    color = HermesTokens.Gold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }
            item {
                NavChip("Link", "Offline — hub not paired", HermesTokens.Surface) {}
            }
            item {
                NavChip("Pairing code", "No code yet — pair from the hub", HermesTokens.Surface) {}
            }
            item {
                // Yes/No prompt card — UI shell, no live prompt behind it.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(HermesTokens.Surface)
                        .border(1.dp, HermesTokens.Gold.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "PROMPT",
                            style = MaterialTheme.typography.caption1,
                            color = HermesTokens.Gold
                        )
                        Text(
                            text = "Hermes yes/no prompts will appear here.",
                            style = MaterialTheme.typography.body2,
                            color = HermesTokens.Text,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = "UI shell — no live prompt",
                            style = MaterialTheme.typography.caption2,
                            color = HermesTokens.Dim
                        )
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(HermesTokens.Gold)
                                    .clickable { }
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "YES",
                                    style = MaterialTheme.typography.button,
                                    color = Color(0xFF241A00)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(HermesTokens.Indigo)
                                    .border(1.dp, HermesTokens.Dim, RoundedCornerShape(10.dp))
                                    .clickable { }
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "NO",
                                    style = MaterialTheme.typography.button,
                                    color = HermesTokens.Text
                                )
                            }
                        }
                    }
                }
            }
            item { BackChip(onBack) }
        }
    }
}
