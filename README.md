# JunoWear — "Charm" for Galaxy Watch

A Wear OS conversion of two ESP32 gadget firmwares into one app for the
Samsung Galaxy Watch6 (SM-R935U):

- **Muse side** — converts [muse-gadget-206](https://github.com/Franzferdinan51/muse-gadget-206)
  (LVGL/C on ESP32) to Jetpack Compose for Wear OS. Its look is ported from
  `muse_theme.h` ("Muse Gloss"): chrome is always the blue family
  (MUSE_BLUE `#1877F2`, bubble `#14305A`, ink `#07101C`, night `#0C1828`),
  and the state accents (READY `#A77DFF`, LISTENING `#5CB8FF`,
  THINKING `#E07BFF`, SPEAKING `#6FF0BF`, ERROR `#FF5C5C`) appear only on
  state elements. Screens mirrored: home (avatar + state word + clock),
  chat/caption with a push-to-talk button, menu, settings.
  The Muse side is a **pseudo charm**: a real Muse gadget pairs only
  through Meta's official phone app, so this side is a UI/link shell —
  it never pretends to be linked.
- **Hermes side** — converts [HermesGadget](https://github.com/Franzferdinan51/HermesGadget).
  Same gloss geometry, gold `#F2B33D` on deep indigo. Home screen with
  link status, a pairing-code area, and a yes/no prompt card shell.
  Unlike Muse, Hermes **can** become a real client: the hub protocol v1 is
  a plain WebSocket (hello → challenge → HMAC auth → pairing code →
  text/prompt frames, plus a PCM audio channel). That client is a later
  wave; v0.1 ships the shell with an honest offline state.

## v0.1 scope

Proof of build: a standalone Wear OS app that compiles to a real APK.
Real live clock and real battery level on the home screen; Muse and
Hermes screens behind two home sections. No network calls, no fake live
data, no watch face / tiles / complications yet.

## Build

On the Mac (JDK 17, Android SDK with platform 36):

```sh
./gradlew :app:assembleDebug
# -> app/build/outputs/apk/debug/app-debug.apk
```

Stack: AGP 8.7.3 · Kotlin 2.0.20 · Gradle 8.9 · compileSdk 36 ·
minSdk 30 · targetSdk 35 · package `com.duckets.junowear`

## Versioning

`versionName` is semver (`0.1.0`), `versionCode` increments by 1 per
release. The app label stays "JunoWear" for now; the in-app name for the
combined Muse + Hermes experience is "Charm".

## Privacy

No secrets, tokens, Wi-Fi names, IP addresses, or device identifiers in
this repo — placeholders only. Configuration belongs on the device or in
untracked local files, never in source.
