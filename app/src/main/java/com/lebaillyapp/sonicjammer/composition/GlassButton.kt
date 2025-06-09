package com.lebaillyapp.sonicjammer.composition


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun GlassButton(
    modifier: Modifier = Modifier,
    text: String = "",
    isOn: Boolean = false,
    color: Color = Color.Blue,
    textColor: Color = Color.White,
    width: Dp = 120.dp,
    height: Dp = 60.dp,
    cornerRadius: Dp = 12.dp,
    textSize: TextUnit = 16.sp,
    glowRadius: Float = 0.5f,
    onClick: () -> Unit = {}
) {
    // État pour les variations aléatoires de luminosité
    val infiniteTransition = rememberInfiniteTransition()

    // Animation principale de l'intensité
    val baseIntensity by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Petites fluctuations aléatoires
    val randomFluctuation by infiniteTransition.animateFloat(
        initialValue = 0.87f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = Random.nextInt(300, 500),
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Combiner les animations pour un effet réaliste
    val currentIntensity = if (isOn) baseIntensity * randomFluctuation else 0f



    val pressedOffsetY by animateFloatAsState(
        targetValue =  0f,
        animationSpec = tween(150)
    )

    Box(
        modifier = modifier
            .padding(horizontal = 2.dp, vertical = 2.dp) // Pour l'espace du halo
            .width(width)
            .height(height)
            .scale(1f)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onClick()
                    }
                )
            }
            .drawBehind {
                val cornerRadiusPx = cornerRadius.toPx()
                val buttonWidth = size.width
                val buttonHeight = size.height

                // Dessiner le bouton complet avec tous les effets
                drawRealisticButton(
                    buttonWidth = buttonWidth,
                    buttonHeight = buttonHeight,
                    cornerRadius = cornerRadiusPx,
                    color = color,
                    isOn = isOn,
                    intensity = currentIntensity,
                    isPressed = false,
                    pressedOffsetY = pressedOffsetY,
                    glowRadius = glowRadius
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor.copy(alpha = if (isOn) 0.95f else 0.7f),
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .alpha(if (isOn) 1f else 0.8f)
        )
    }
}

private fun DrawScope.drawRealisticButton(
    buttonWidth: Float,
    buttonHeight: Float,
    cornerRadius: Float,
    color: Color,
    isOn: Boolean,
    intensity: Float,
    isPressed: Boolean,
    pressedOffsetY: Float,
    glowRadius: Float
) {
    // Dessiner dans l'ordre pour un effet réaliste

    // 1. Socle/Base plastique (boîtier extérieur)
    drawButtonPlasticBase(buttonWidth, buttonHeight, cornerRadius, isPressed, pressedOffsetY)

    // 2. Le dôme en verre du bouton
    if (isOn) {
        drawButtonGlassDome(buttonWidth, buttonHeight, cornerRadius, color, isOn, intensity, isPressed, pressedOffsetY)
    }

    // 3. Si le bouton est allumé, dessiner le halo lumineux par-dessus tout
    if (isOn) {
        drawButtonGlow(buttonWidth, buttonHeight, cornerRadius, color, intensity, glowRadius)
    }
}

private fun DrawScope.drawButtonPlasticBase(
    width: Float,
    height: Float,
    cornerRadius: Float,
    isPressed: Boolean,
    pressedOffsetY: Float
) {
    // Base plastique plus large et plus haute que le bouton lui-même
    val baseExpandX = width * 0.05f
    val baseExpandY = height * 0.10f
    val baseWidth = width + baseExpandX * 2
    val baseHeight = height + baseExpandY * 2

    // Base plastique (boîtier extérieur) légèrement plus grande que le bouton
    val baseBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF0C0B0B),
            Color(0xFF0C0B0B)
        ),
        center = Offset(baseWidth / 2, baseHeight / 2),
        radius = baseWidth.coerceAtLeast(baseHeight) / 1.5f
    )

    // Dessiner la base avec un déplacement pour centrer
    drawRoundRect(
        brush = baseBrush,
        topLeft = Offset(-baseExpandX, -baseExpandY + pressedOffsetY * 0.5f),
        size = Size(baseWidth, baseHeight),
        cornerRadius = CornerRadius(cornerRadius * 1.2f)
    )

    // Rebord intérieur du boîtier (où s'insère le verre)
    val innerBorderWidth = 1f
    drawRoundRect(
        color = Color(0x28000000),
        topLeft = Offset(-baseExpandX + innerBorderWidth, -baseExpandY + innerBorderWidth + pressedOffsetY * 0.5f),
        size = Size(baseWidth - innerBorderWidth * 2, baseHeight - innerBorderWidth * 2),
        cornerRadius = CornerRadius(cornerRadius * 1.1f),
        style = Stroke(width = 1.5f)
    )

    // Effet de profondeur pour le boîtier (intérieur plus sombre)
    val innerShadowColor = Color.Black.copy(alpha = 0.6f)
    drawRoundRect(
        color = innerShadowColor,
        topLeft = Offset(0f, 0f + pressedOffsetY),
        size = Size(width, height),
        cornerRadius = CornerRadius(cornerRadius),
        style = Fill
    )
}

private fun DrawScope.drawButtonGlassDome(
    width: Float,
    height: Float,
    cornerRadius: Float,
    color: Color,
    isOn: Boolean,
    intensity: Float,
    isPressed: Boolean,
    pressedOffsetY: Float
) {
    // Couleur du verre avec opacité variable selon l'état
    val glassBaseColor = if (isOn) {
        color.copy(alpha = 0.6f + (intensity * 0.4f))
    } else {
        color.copy(alpha = 0.3f)
    }

    // Corps du bouton en verre (légèrement surélevé pour effet 3D)
    val buttonElevation = if (isPressed) 0f else 2f
    val buttonTopLeft = Offset(0f, 0f + pressedOffsetY - buttonElevation)

    // Léger effet d'ombre sous le bouton
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.4f),
        topLeft = Offset(1f, 3f + pressedOffsetY),
        size = Size(width - 2f, height - 2f),
        cornerRadius = CornerRadius(cornerRadius)
    )

    // Base du verre translucide
    drawRoundRect(
        color = glassBaseColor,
        topLeft = buttonTopLeft,
        size = Size(width, height),
        cornerRadius = CornerRadius(cornerRadius)
    )

    // Couche pour l'effet de verre avec dégradé
    val glassGradient = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = if (isOn) 0.3f else 0.15f),
            glassBaseColor.copy(alpha = if (isOn) 0.7f else 0.2f),
            glassBaseColor.copy(alpha = if (isOn) 0.8f else 0.25f)
        ),
        start = Offset(width / 2, 0f),
        end = Offset(width / 2, height)
    )

    drawRoundRect(
        brush = glassGradient,
        topLeft = buttonTopLeft,
        size = Size(width, height),
        cornerRadius = CornerRadius(cornerRadius),
        alpha = if (isOn) 0.85f * intensity else 0.6f
    )

    // Bordure fine du verre
    drawRoundRect(
        color = Color.White.copy(alpha = if (isOn) 0.25f else 0.15f),
        topLeft = buttonTopLeft,
        size = Size(width, height),
        cornerRadius = CornerRadius(cornerRadius),
        style = Stroke(width = 1f)
    )

    // Ajouter les reflets subtils sur le verre
    drawGlassReflections(width, height, cornerRadius, isOn, intensity, isPressed, buttonTopLeft)
}

private fun DrawScope.drawGlassReflections(
    width: Float,
    height: Float,
    cornerRadius: Float,
    isOn: Boolean,
    intensity: Float,
    isPressed: Boolean,
    buttonTopLeft: Offset
) {
    // Créer un masque pour limiter les reflets à l'intérieur du bouton
    val buttonPath = Path().apply {
        addRoundRect(
            RoundRect(
                left = buttonTopLeft.x,
                top = buttonTopLeft.y,
                right = buttonTopLeft.x + width,
                bottom = buttonTopLeft.y + height,
                cornerRadius = CornerRadius(cornerRadius)
            )
        )
    }

    clipPath(buttonPath) {
        // 1. Reflet principal supérieur (large bande horizontale)
        val mainReflectionHeight = height * 0.35f
        val mainReflectionAlpha = if (isOn) 0.5f * intensity else 0.3f

        val mainReflectionBrush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = mainReflectionAlpha * 0.1f),
                Color.White.copy(alpha = mainReflectionAlpha),
                Color.White.copy(alpha = mainReflectionAlpha * 0.8f),
                Color.White.copy(alpha = mainReflectionAlpha * 0.1f)
            ),
            start = Offset(0f, buttonTopLeft.y),
            end = Offset(width, buttonTopLeft.y)
        )

        // Reflet légèrement incliné
        withTransform({
            rotate(-8f, Offset(width / 2, buttonTopLeft.y))
        }) {
            drawRect(
                brush = mainReflectionBrush,
                topLeft = Offset(0f, buttonTopLeft.y),
                size = Size(width, mainReflectionHeight),
                alpha = 0.7f,
                blendMode = BlendMode.Screen
            )
        }




        // 3. Reflet inférieur subtil (pour la profondeur)
        val bottomReflectionHeight = height * 0.15f
        val bottomReflectionY = height - bottomReflectionHeight + buttonTopLeft.y
        val bottomReflectionAlpha = if (isOn) 0.2f * intensity else 0.1f

        val bottomReflectionBrush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0f),
                Color.White.copy(alpha = bottomReflectionAlpha),
                Color.White.copy(alpha = 0f)
            ),
            start = Offset(0f, bottomReflectionY),
            end = Offset(width, bottomReflectionY)
        )

        // Légèrement incliné dans l'autre sens
        withTransform({
            rotate(12f, Offset(width / 2, bottomReflectionY))
        }) {
            drawRect(
                brush = bottomReflectionBrush,
                topLeft = Offset(0f, bottomReflectionY),
                size = Size(width, bottomReflectionHeight),
                alpha = 0.6f,
                blendMode = BlendMode.Screen
            )
        }
    }
}

private fun DrawScope.drawButtonGlow(
    width: Float,
    height: Float,
    cornerRadius: Float,
    color: Color,
    intensity: Float,
    glowRadiuS: Float
) {
    // 1. Halo externe - subtil et progressif
    val glowRadius = width.coerceAtLeast(height) * glowRadiuS

    // Créer un dégradé pour le halo externe
    val outerGlowBrush = Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = 0.4f * intensity),
            color.copy(alpha = 0.25f * intensity),
            color.copy(alpha = 0.15f * intensity),
            color.copy(alpha = 0.08f * intensity),
            color.copy(alpha = 0.04f * intensity),
            color.copy(alpha = 0.01f * intensity),
            color.copy(alpha = 0f)
        ),
        center = Offset(width / 2, height / 2),
        radius = glowRadius,
        tileMode = TileMode.Clamp
    )

    // Taille du halo plus grande que le bouton
    val glowExpand = width * glowRadius

    // Dessiner le halo externe
    drawRoundRect(
        brush = outerGlowBrush,
        topLeft = Offset(-glowExpand / 2, -glowExpand / 2),
        size = Size(width + glowExpand, height + glowExpand),
        cornerRadius = CornerRadius(cornerRadius * 2),
        blendMode = BlendMode.Screen
    )

    // 2. Halo interne plus intense
    val innerGlowBrush = Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = 0.15f * intensity),
            color.copy(alpha = 0.1f * intensity),
            color.copy(alpha = 0.05f * intensity),
            color.copy(alpha = 0f)
        ),
        center = Offset(width / 2, height / 2),
        radius = width.coerceAtLeast(height) * 0.5f
    )

    // Dessiner le halo interne
    drawRoundRect(
        brush = innerGlowBrush,
        topLeft = Offset(0f, 0f),
        size = Size(width, height),
        cornerRadius = CornerRadius(cornerRadius),
        blendMode = BlendMode.Screen
    )


}
