package com.lebaillyapp.sonicjammer.composition

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SyncedDualModalSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    topSheetRatio: Float = 0.35f,
    bottomSheetRatio: Float = 0.65f,
    peekHeight: Dp = 62.dp,
    autoDismissThresholdRatio: Float = 0.35f,
    cornerRadius: Dp = 15.dp,
    scrimColor: Color = Color.Black.copy(alpha = 0.5f),
    scrimMaxAlpha: Float = 0.5f,
    modalBackgroundColor: Color = Color.Black,
    topContent: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(1f) } // 1 = fermé, 0 = ouvert

    val density = LocalDensity.current
    val screenHeightPx = with(LocalConfiguration.current) {
        density.run { screenHeightDp.dp.toPx() }
    }

    val topPx = screenHeightPx * topSheetRatio
    val bottomPx = screenHeightPx * bottomSheetRatio
    val totalRangePx = topPx + bottomPx
    val peekPx = with(density) { peekHeight.toPx() }
    val dismissPx = totalRangePx * autoDismissThresholdRatio

    LaunchedEffect(visible) {
        if (visible) {
            offset.animateTo(0f, tween(durationMillis = 400, easing = FastOutSlowInEasing))
        } else {
            offset.snapTo(1f)
        }
    }

    fun onStopDrag() {
        scope.launch {
            val currentPx = offset.value * totalRangePx
            if (currentPx > dismissPx) {
                offset.animateTo(
                    1f,
                    tween(durationMillis = 400, easing = FastOutSlowInEasing)
                )
                onDismiss()
            } else {
                offset.animateTo(
                    0f,
                    tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    val topDragState = rememberDraggableState { delta ->
        scope.launch {
            val normalized = -delta / totalRangePx
            offset.snapTo((offset.value + normalized).coerceIn(0f, 1f))
        }
    }

    val bottomDragState = rememberDraggableState { delta ->
        scope.launch {
            val normalized = delta / totalRangePx
            offset.snapTo((offset.value + normalized).coerceIn(0f, 1f))
        }
    }

    if (visible || offset.value < 1f) {
        Box(modifier.fillMaxSize()) {

            // Scrim
            val scrimAlpha = (1f - offset.value).coerceIn(0f, scrimMaxAlpha)
            Box(
                Modifier
                    .fillMaxSize()
                    .background(scrimColor.copy(alpha = scrimAlpha))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // Animate instead of snapping!
                        scope.launch {
                            offset.animateTo(
                                1f,
                                tween(durationMillis = 400, easing = FastOutSlowInEasing)
                            )
                            onDismiss()
                        }
                    }
            )

            // Top sheet
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset {
                        val y = (-topPx + peekPx) * offset.value
                        IntOffset(0, y.roundToInt())
                    }
                    .draggable(
                        state = topDragState,
                        orientation = Orientation.Vertical,
                        onDragStopped = { onStopDrag() }
                    )
                    .fillMaxWidth()
                    .height(with(density) { topPx.toDp() })
                    .background(
                        color = modalBackgroundColor,
                        shape = RoundedCornerShape(
                            bottomStart = cornerRadius,
                            bottomEnd = cornerRadius
                        )
                    )
            ) {
                topContent()
            }

            // Bottom sheet
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset {
                        val y = (bottomPx - peekPx) * offset.value
                        IntOffset(0, y.roundToInt())
                    }
                    .draggable(
                        state = bottomDragState,
                        orientation = Orientation.Vertical,
                        onDragStopped = { onStopDrag() }
                    )
                    .fillMaxWidth()
                    .height(with(density) { bottomPx.toDp() })
                    .background(
                        color = modalBackgroundColor,
                        shape = RoundedCornerShape(
                            topStart = cornerRadius,
                            topEnd = cornerRadius
                        )
                    )
            ) {
                bottomContent()
            }
        }
    }
}