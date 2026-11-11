package org.maplibre.compose.map

import org.maplibre.nativeffi.MapOptions

internal actual fun MapOptions.withPlatformFontOptions(): MapOptions =
  copy(localIdeographFontFamily = "sans-serif")
