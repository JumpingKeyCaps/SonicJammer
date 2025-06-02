package com.lebaillyapp.sonicjammer.composable.card

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun InnerShadowCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    backgroundColor: Color = Color(0xFF0C0C0C),
    shadowColorLight: Color = Color(0xFF181818),
    shadowColorDark: Color = Color(0xFF141414),
    shadowBlur: Dp = 2.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val density = LocalDensity.current
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }
    val blurPx = with(density) { shadowBlur.toPx() }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .drawBehind {
                // Ombre claire (haut/gauche)
                drawIntoCanvas { canvas ->
                    val paint = android.graphics.Paint().apply {
                        color = shadowColorLight.copy(alpha = 0.7f).toArgb()
                        maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = blurPx
                    }

                    canvas.nativeCanvas.drawRoundRect(
                        blurPx / 2,
                        blurPx / 2,
                        size.width - blurPx / 2,
                        size.height - blurPx / 2,
                        cornerRadiusPx,
                        cornerRadiusPx,
                        paint
                    )
                }

                // Ombre foncée (bas/droite)
                drawIntoCanvas { canvas ->
                    val paint = android.graphics.Paint().apply {
                        color = shadowColorDark.copy(alpha = 0.4f).toArgb()
                        maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = blurPx
                    }

                    canvas.nativeCanvas.drawRoundRect(
                        -blurPx / 2,
                        -blurPx / 2,
                        size.width + blurPx / 2,
                        size.height + blurPx / 2,
                        cornerRadiusPx,
                        cornerRadiusPx,
                        paint
                    )
                }
            },
        content = content
    )
}