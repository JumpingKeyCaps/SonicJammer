package com.lebaillyapp.sonicjammer.composition

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.random.Random

import androidx.compose.ui.graphics.drawscope.withTransform

@Composable
fun RealisticLED(
    modifier: Modifier = Modifier,
    isOn: Boolean = false,
    color: Color = Color.Red,
    size: Float = 50f,
    haloSpacer: Int = 3,
    blinkInterval: Int = 0 // 0 = pas de clignotement, sinon intervalle en ms
) {
    // État pour les variations aléatoires de luminosité
    val infiniteTransition = rememberInfiniteTransition()

    // Animation de clignotement si blinkInterval > 0
    val blinkState by if (blinkInterval > 0) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(blinkInterval, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    // Animation principale de l'intensité
    val baseIntensity by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Petites fluctuations aléatoires
    val randomFluctuation by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = Random.nextInt(300, 700),
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Combiner les animations pour un effet réaliste
    val isCurrentlyOn = isOn && (blinkInterval <= 0 || blinkState > 0.5f)
    val currentIntensity = if (isCurrentlyOn) baseIntensity * randomFluctuation else 0f

    Box(
        modifier = modifier
            .size(size.dp * haloSpacer) // Pour laisser de l'espace pour le halo
            .drawBehind {
                // Vue du dessus de la LED
                drawLedTopView(color, size, isCurrentlyOn, currentIntensity)
            },
        contentAlignment = Alignment.Center
    ) {}
}

private fun DrawScope.drawLedTopView(color: Color, size: Float, isOn: Boolean, intensity: Float) {
    val center = Offset(this.size.width / 2, this.size.height / 2)
    val ledRadius = size * 0.8f

    // Corps extérieur (partie plastique) - DESSINÉ EN PREMIER
    drawLedPlasticBody(center, size, ledRadius)

    // Dôme en verre transparent - DESSINÉ ENSUITE
    drawGlassDome(center, color, size, ledRadius, isOn, intensity)

    // Glow - visible uniquement quand allumé - DESSINÉ EN DERNIER POUR ÊTRE AU-DESSUS
    if (isOn) {
        drawLedGlow(center, color, size, intensity)
    }
}

private fun DrawScope.drawLedGlow(center: Offset, color: Color, size: Float, intensity: Float) {
    // Rayon maximal du halo
    val maxRadius = size * 2.5f

    // Créer un dégradé radial très progressif pour le halo
    val glowBrush = Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = 0.5f * intensity),
            color.copy(alpha = 0.3f * intensity),
            color.copy(alpha = 0.2f * intensity),
            color.copy(alpha = 0.1f * intensity),
            color.copy(alpha = 0.05f * intensity),
            color.copy(alpha = 0.02f * intensity),
            color.copy(alpha = 0f)
        ),
        center = center,
        radius = maxRadius,
        tileMode = TileMode.Clamp
    )

    // Dessiner le halo principal avec le dégradé progressif
    drawCircle(
        brush = glowBrush,
        radius = maxRadius,
        center = center,
        blendMode = BlendMode.Screen
    )

    // Ajouter un éclat interne pour un effet plus réaliste
    val innerGlowRadius = size * 1.2f
    val innerGlowBrush = Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = 0.4f * intensity),
            color.copy(alpha = 0.2f * intensity),
            color.copy(alpha = 0.0f)
        ),
        center = center,
        radius = innerGlowRadius
    )

    drawCircle(
        brush = innerGlowBrush,
        radius = innerGlowRadius,
        center = center,
        blendMode = BlendMode.Plus
    )
}

private fun DrawScope.drawLedPlasticBody(center: Offset, size: Float, ledRadius: Float) {
    // Partie plastique extérieure (corps de la LED)
    val outerRadius = ledRadius * 1.2f

    // Gradient pour simuler une surface en plastique
    val plasticBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF232323),
            Color(0xFF1A1A1A)
        ),
        center = center,
        radius = outerRadius
    )

    // Dessiner le corps extérieur
    drawCircle(
        brush = plasticBrush,
        radius = outerRadius,
        center = center
    )

    // Bordure fine
    drawCircle(
        color = Color(0xFF1A1A1A),
        radius = outerRadius,
        center = center,
        style = Stroke(width = 1f)
    )
}

private fun DrawScope.drawGlassDome(
    center: Offset,
    color: Color,
    size: Float,
    ledRadius: Float,
    isOn: Boolean,
    intensity: Float
) {
    // Cercle intérieur (le dôme en verre transparent)
    val glassColor = if (isOn) {
        color.copy(alpha = 0.7f + (intensity * 0.3f))
    } else {
        color.copy(alpha = 0.2f)
    }

    // Teinte de base pour la LED
    drawCircle(
        color = glassColor,
        radius = ledRadius,
        center = center
    )

    // Effet de verre transparent avec dégradé
    val glassBrush = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = if (isOn) 0.7f * intensity else 0.2f),
            glassColor.copy(alpha = if (isOn) 0.8f else 0.15f),
            glassColor.copy(alpha = if (isOn) 0.6f else 0.1f)
        ),
        center = center,
        radius = ledRadius
    )

    // Dégradé pour donner un effet de transparence au verre
    drawCircle(
        brush = glassBrush,
        radius = ledRadius,
        center = center,
        alpha = if (isOn) 0.8f * intensity else 0.3f,
        blendMode = BlendMode.Overlay
    )

    // Effet de "dome" - courbure du verre
    val domeCurvature = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.1f),
            Color.Transparent
        ),
        center = Offset(center.x, center.y - ledRadius * 0.1f),
        radius = ledRadius * 0.9f
    )

    // Appliquer l'effet de courbure
    drawCircle(
        brush = domeCurvature,
        radius = ledRadius * 0.9f,
        center = center
    )

    // Ajouter des reflets pour l'effet de verre
    drawLedReflections(center, ledRadius, isOn, intensity)

    // Léger contour pour définir les bords du dôme en verre
    drawCircle(
        color = Color.White.copy(alpha = 0.2f),
        radius = ledRadius,
        center = center,
        style = Stroke(width = 0.5f)
    )
}

private fun DrawScope.drawLedReflections(
    center: Offset,
    ledRadius: Float,
    isOn: Boolean,
    intensity: Float
) {
    // Reflet principal - en haut à gauche
    val mainReflectionRadius = ledRadius * 0.45f
    val mainReflectionOffset = Offset(
        center.x - ledRadius * 0.25f,
        center.y - ledRadius * 0.25f
    )

    val mainReflectionAlpha = if (isOn) 0.7f * intensity else 0.3f

    val mainReflectionBrush = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = mainReflectionAlpha),
            Color.White.copy(alpha = mainReflectionAlpha * 0.5f),
            Color.White.copy(alpha = 0f)
        ),
        center = mainReflectionOffset,
        radius = mainReflectionRadius
    )

    // Dessiner le reflet principal avec une transformation pour créer une ellipse
    withTransform({
        scale(0.8f, 0.6f, mainReflectionOffset)
        rotate(-15f, mainReflectionOffset)
    }) {
        drawCircle(
            brush = mainReflectionBrush,
            radius = mainReflectionRadius,
            center = mainReflectionOffset,
            blendMode = BlendMode.Screen
        )
    }

    // Reflet secondaire - plus petit, en bas à droite
    val secondReflectionRadius = ledRadius * 0.2f
    val secondReflectionOffset = Offset(
        center.x + ledRadius * 0.3f,
        center.y + ledRadius * 0.2f
    )

    val secondReflectionAlpha = if (isOn) 0.4f * intensity else 0.15f

    val secondReflectionBrush = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = secondReflectionAlpha),
            Color.White.copy(alpha = 0f)
        ),
        center = secondReflectionOffset,
        radius = secondReflectionRadius
    )

    // Dessiner le reflet secondaire
    withTransform({
        scale(0.7f, 0.4f, secondReflectionOffset)
        rotate(30f, secondReflectionOffset)
    }) {
        drawCircle(
            brush = secondReflectionBrush,
            radius = secondReflectionRadius,
            center = secondReflectionOffset,
            blendMode = BlendMode.Screen
        )
    }

    // Point de lumière brillant (pour simuler une réflexion très intense)
    if (isOn) {
        val brightSpotRadius = ledRadius * 0.05f
        val brightSpotOffset = Offset(
            center.x - ledRadius * 0.15f,
            center.y - ledRadius * 0.2f
        )

        drawCircle(
            color = Color.White,
            radius = brightSpotRadius,
            center = brightSpotOffset,
            alpha = 0.9f * intensity
        )
    }
}
