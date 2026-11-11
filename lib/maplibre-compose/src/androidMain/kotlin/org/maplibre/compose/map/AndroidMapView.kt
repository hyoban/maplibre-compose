package org.maplibre.compose.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import org.maplibre.compose.mlnffi.AndroidMlnFfiSurface
import org.maplibre.compose.mlnffi.MapRenderBackend

@Composable internal actual fun mapPresentationHostIdentity(): Any = Unit

@Composable
internal actual fun rememberComposeMapPresentation(
  state: MapState,
  presentationOwner: MapPresentationOwnerToken,
  options: MapViewOptions,
): ComposeMapPresentation? {
  val logger = state.runtime.logger
  val runtimeBackends = remember { loadRuntimeBackends(logger) }
  val renderBackend =
    remember(runtimeBackends) { runtimeBackends.firstOrNull() ?: MapRenderBackend.OpenGl }
  val renderMode = options.uiOptions.renderMode
  return key(renderMode, renderBackend) {
    rememberMlnFfiComposeMapPresentation(
      renderBackend = renderBackend,
      surface = { renderer, surfaceModifier, surfaceLogger, _ ->
        key(renderMode, renderBackend) {
          AndroidMlnFfiSurface(
            renderer = renderer,
            runtimeBackends = runtimeBackends,
            backend = renderBackend,
            renderMode = renderMode,
            loadColor = options.uiOptions.loadColor,
            maximumFps = options.renderOptions.maximumFps,
            modifier = surfaceModifier,
            logger = surfaceLogger,
            // Create the Android window while the style loads. The Compose load-color layers
            // remain above it until both style readiness and its first presented buffer, so this
            // overlaps SurfaceView traversal with style work without exposing the black hole.
            presentWindow = true,
          )
        }
      },
      state = state,
      presentationOwner = presentationOwner,
      options = options,
    )
  }
}
