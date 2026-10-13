package com.duckets.junowear

import androidx.compose.ui.graphics.Color

/**
 * Color tokens ported verbatim from muse-gadget-206
 * (esp32/components/muse/muse_theme.h, "Muse Gloss" v1.0).
 * Chrome is always the blue family. State accents appear ONLY on state
 * elements (state word, status dot, glow) — never as surfaces.
 */
object MuseTokens {
    val Blue = Color(0xFF1877F2)        // MUSE_BLUE — primary chrome
    val BlueDeep = Color(0xFF0A3F86)    // MUSE_BLUE_DEEP
    val BlueTop = Color(0xFF4B95F5)     // MUSE_BLUE_TOP
    val BlueBot = Color(0xFF1467D4)     // MUSE_BLUE_BOT
    val Bubble = Color(0xFF14305A)      // MUSE_BUBBLE — THE surface color
    val BubbleTop = Color(0xFF3A5174)   // MUSE_BUBBLE_TOP
    val BubbleBot = Color(0xFF123465)   // MUSE_BUBBLE_BOT
    val Ink = Color(0xFF07101C)         // MUSE_INK
    val Night = Color(0xFF0C1828)       // MUSE_NIGHT — app background
    val NightDeep = Color(0xFF0C1D33)   // MUSE_NIGHT_DEEP
    val Input = Color(0xFF13233C)       // MUSE_INPUT
    val Text = Color(0xFFF2EFFF)        // MUSE_TEXT
    val Caption = Color(0xFFD8D2FF)     // MUSE_CAPTION — reply/caption text
    val Dim = Color(0xFF8B84A8)         // MUSE_DIM — secondary text
    val Ok = Color(0xFF6FF0BF)          // MUSE_OK

    // State accents (muse_theme.h section 3) — state elements ONLY.
    val StateReady = Color(0xFFA77DFF)
    val StateListening = Color(0xFF5CB8FF)
    val StateThinking = Color(0xFFE07BFF)
    val StateSpeaking = Color(0xFF6FF0BF)
    val StateError = Color(0xFFFF5C5C)
}

/**
 * Hermes identity (HermesGadget): same gloss geometry, gold on deep indigo.
 */
object HermesTokens {
    val Gold = Color(0xFFF2B33D)
    val Indigo = Color(0xFF171233)      // deep indigo page
    val Surface = Color(0xFF251B52)     // bubble surface on indigo
    val SurfaceBot = Color(0xFF1B1245)
    val Text = Color(0xFFF5EFE0)
    val Dim = Color(0xFFA79FD0)
}
