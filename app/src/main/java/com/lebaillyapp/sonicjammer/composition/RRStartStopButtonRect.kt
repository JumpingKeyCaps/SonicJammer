package com.lebaillyapp.sonicjammer.composition


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.sonicjammer.R


/**
 * Bouton Start/Stop rectangulaire arrondi avec effet de profondeur, animation douce et effet glow coloré.
 *
 * @param modifier Modificateur Compose pour personnaliser la taille et la position.
 * @param width Largeur du bouton en Dp.
 * @param height Hauteur du bouton en Dp.
 * @param isStarted État du bouton : true = Start (en marche), false = Stop (arrêté).
 * @param onToggle Callback appelé à chaque appui pour inverser l’état.
 * @param startColor Couleur d’accent lorsque le bouton est en mode "Start" (activé).
 * @param stopColor Couleur d’accent lorsque le bouton est en mode "Stop" (désactivé).
 * @param backgroundColor Couleur de fond du bouton (sombre, avec profondeur).
 * @param shadowColor Couleur de l’ombre pour l’effet enfoncé.
 * @param cornerRadius Rayon des coins arrondis en Dp.
 * @param glowRadius Rayon du glow sous le bouton en Dp.
 */
@Composable
fun RRStartStopButtonRect(
    modifier: Modifier = Modifier,
    width: Dp = 200.dp,
    height: Dp = 60.dp,
    isStarted: Boolean,
    onToggle: () -> Unit,
    startColor: Color = Color(0xFFFF6B35),
    stopColor: Color = Color(0xFF888888),
    textA: String = "STOP",
    textB: String = "START",
    backgroundColor: Color = Color(0xFF1A1A1A),
    shadowColor: Color = Color.Black.copy(alpha = 0.6f),
    cornerRadius: Dp = 16.dp,
    glowRadius: Dp = 20.dp,
) {
    val animatedScale by animateFloatAsState(targetValue = if (isStarted) 1.02f else 1f, animationSpec = tween(250))
    val animatedColor by animateColorAsState(if (isStarted) startColor else stopColor)

    Box(
        modifier = modifier.size(width, height),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val glowPx = glowRadius.toPx()
            val radiusPx = cornerRadius.toPx()

            // On crée un rectangle plus grand que le bouton (glowPx en plus tout autour)
            // pour dessiner le glow autour, avec gradient radial dégradé transparent.

            // Définition du dégradé radial pour le glow
            val glowGradient = Brush.radialGradient(
                colors = listOf(animatedColor.copy(alpha = 0.4f), Color.Transparent),
                center = Offset(w / 2, h / 2),
                radius = (maxOf(w, h) / 2) + glowPx
            )

            // Dessiner un rounded rect glow plus large (on déplace la position pour centrer)
            drawRoundRect(
                brush = glowGradient,
                topLeft = Offset(-glowPx, -glowPx),
                size = Size(w + glowPx * 2, h + glowPx * 2),
                cornerRadius = CornerRadius(radiusPx + glowPx, radiusPx + glowPx)
            )
        }

        Box(
            modifier = Modifier
                .size(width, height)
                .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
                .clickable { onToggle() }
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            backgroundColor.copy(alpha = 0.95f),
                            backgroundColor.copy(alpha = 0.85f)
                        )
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(cornerRadius),
                    clip = false,
                    ambientColor = shadowColor,
                    spotColor = shadowColor
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            shadowColor.copy(alpha = 0.3f),
                            shadowColor.copy(alpha = 0.6f)
                        ),
                        startY = 0f,
                        endY = h
                    ),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                )
            }

            Text(
                text = if (isStarted) textA else textB,
                color = animatedColor,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.micro_regular)),
                fontSize = 20.sp,
                letterSpacing = 2.sp
            )
        }
    }
}