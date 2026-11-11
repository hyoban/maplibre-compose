package org.maplibre.compose.map

import kotlin.test.Test
import kotlin.test.assertEquals
import org.maplibre.nativeffi.LogicalExtent
import org.maplibre.nativeffi.MapOptions

class PlatformMapOptionsTest {
  @Test
  fun androidUsesTheSystemSansFontForIdeographs() {
    val options = MapOptions(initialExtent = LogicalExtent(1u, 1u, 1.0))

    assertEquals("sans-serif", options.withPlatformFontOptions().localIdeographFontFamily)
  }
}
