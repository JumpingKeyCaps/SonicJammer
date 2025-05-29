package com.lebaillyapp.sonicjammer.composable.led

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * ### RealisticLED
 *
 * Composant Composable simulant une LED réaliste avec des options de couleur, de taille, de clignotement et un halo lumineux.
 *
 * @param modifier Modificateur à appliquer au conteneur de la LED.
 * @param isOn État de la LED (allumée ou éteinte). Par défaut à `false`.
 * @param color Couleur de la LED lorsqu'elle est allumée. Par défaut à `Color.Red`.
 * @param size Taille de la LED en DP (rayon approximatif). Par défaut à `50f`.
 * @param haloSpacer Multiplicateur pour la taille du conteneur afin d'accommoder le halo. Par défaut à `3`.
 * @param blinkInterval Intervalle de clignotement en millisecondes. `0` désactive le clignotement. Par défaut à `0`.
 */
@Composable
fun RealisticLED(
    modifier: Modifier = Modifier,
    isOn: Boolean = false,
    color: Color = Color.Red,
    size: Float = 50f,
    haloSpacer: Int = 3,
    blinkInterval: Int = 0 // 0 = pas de clignotement, sinon intervalle en ms
) {
    // ### État pour les variations de luminosité et le clignotement

    // `rememberInfiniteTransition` est utilisé pour créer des animations en boucle infinie.
    val infiniteTransition = rememberInfiniteTransition()

    // État pour l'animation de clignotement.
    val blinkState by if (blinkInterval > 0) {
        // Si `blinkInterval` est supérieur à 0, on crée une animation de clignotement.
        infiniteTransition.animateFloat(
            initialValue = 0f, // LED éteinte au début de l'intervalle
            targetValue = 1f, // LED allumée à la fin de l'intervalle
            animationSpec = infiniteRepeatable(
                animation = tween(blinkInterval, easing = LinearEasing), // Animation linéaire sur la durée de l'intervalle
                repeatMode = RepeatMode.Reverse // L'animation se répète en sens inverse (on -> off -> on)
            )
        )
    } else {
        // Si `blinkInterval` est 0, la LED ne clignote pas, on maintient un état "allumé" (intensité maximale).
        remember { mutableStateOf(1f) }
    }

    // Animation principale de l'intensité lumineuse (subtile variation).
    val baseIntensity by infiniteTransition.animateFloat(
        initialValue = 0.85f, // Intensité de départ
        targetValue = 1.0f, // Intensité maximale
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), // Animation sur 1 seconde avec un easing linéaire
            repeatMode = RepeatMode.Reverse // Répète l'animation en sens inverse
        )
    )

    // Petites fluctuations aléatoires d'intensité pour plus de réalisme.
    val randomFluctuation by infiniteTransition.animateFloat(
        initialValue = 0.95f, // Intensité de départ
        targetValue = 1.0f, // Intensité maximale
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = Random.nextInt(300, 700), // Durée aléatoire entre 300 et 700 ms
                easing = FastOutSlowInEasing // Easing pour une variation douce
            ),
            repeatMode = RepeatMode.Reverse // Répète l'animation en sens inverse
        )
    )

    // ### Calcul de l'intensité actuelle de la LED

    // La LED est considérée comme "actuellement allumée" si l'état `isOn` est vrai
    // et soit elle ne clignote pas (`blinkInterval` est 0), soit la phase de clignotement est dans la partie "allumée".
    val isCurrentlyOn = isOn && (blinkInterval <= 0 || blinkState > 0.5f)

    // L'intensité actuelle est basée sur l'animation principale et les fluctuations aléatoires si la LED est allumée, sinon elle est à 0.
    val currentIntensity = if (isCurrentlyOn) baseIntensity * randomFluctuation else 0f

    // ### Conteneur de la LED

    Box(
        modifier = modifier
            .size(size.dp * haloSpacer) // Définit la taille du conteneur pour inclure l'espace du halo
            .drawBehind {
                // Appel à la fonction de dessin pour la vue de dessus de la LED
                drawLedTopView(color, size, isCurrentlyOn, currentIntensity)
            },
        contentAlignment = Alignment.Center // Centre le contenu (bien qu'il n'y en ait pas explicitement ici)
    ) {}
}

/**
 * ### drawLedTopView
 *
 * Fonction de dessin responsable de la représentation visuelle de la LED (vue de dessus).
 *
 * @param color Couleur principale de la LED.
 * @param size Taille de référence pour les dimensions de la LED.
 * @param isOn Indique si la LED est allumée.
 * @param intensity Intensité lumineuse actuelle de la LED (entre 0 et 1).
 */
private fun DrawScope.drawLedTopView(color: Color, size: Float, isOn: Boolean, intensity: Float) {
    val center = Offset(this.size.width / 2, this.size.height / 2) // Centre du DrawScope
    val ledRadius = size * 0.8f // Rayon approximatif du dôme de la LED

    // Dessiner le corps extérieur en plastique de la LED (en premier plan)
    drawLedPlasticBody(center, size, ledRadius)

    // Dessiner le dôme en verre transparent de la LED (par-dessus le corps en plastique)
    drawGlassDome(center, color, size, ledRadius, isOn, intensity)

    // Dessiner le halo lumineux si la LED est allumée (au-dessus de tout)
    if (isOn) {
        drawLedGlow(center, color, size, intensity)
    }
}

/**
 * ### drawLedGlow
 *
 * Fonction de dessin pour l'effet de halo lumineux autour de la LED.
 *
 * @param center Centre de la LED.
 * @param color Couleur du halo.
 * @param size Taille de référence pour le rayon du halo.
 * @param intensity Intensité du halo.
 */
private fun DrawScope.drawLedGlow(center: Offset, color: Color, size: Float, intensity: Float) {
    // Rayon maximal du halo
    val maxRadius = size * 2.5f

    // Créer un dégradé radial progressif pour le halo
    val glowBrush = Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = 0.5f * intensity), // Centre plus intense
            color.copy(alpha = 0.3f * intensity),
            color.copy(alpha = 0.2f * intensity),
            color.copy(alpha = 0.1f * intensity),
            color.copy(alpha = 0.05f * intensity),
            color.copy(alpha = 0.02f * intensity), // Bordure très faible
            Color.Transparent // Extérieur transparent
        ),
        center = center,
        radius = maxRadius,
        tileMode = TileMode.Clamp // Le dégradé s'étend jusqu'au rayon sans répétition
    )

    // Dessiner le halo principal avec le dégradé
    drawCircle(
        brush = glowBrush,
        radius = maxRadius,
        center = center,
        blendMode = BlendMode.Screen // Mode de fusion pour un effet lumineux
    )

    // Ajouter un éclat interne plus concentré pour plus de réalisme
    val innerGlowRadius = size * 1.2f
    val innerGlowBrush = Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = 0.4f * intensity),
            color.copy(alpha = 0.2f * intensity),
            Color.Transparent
        ),
        center = center,
        radius = innerGlowRadius
    )

    drawCircle(
        brush = innerGlowBrush,
        radius = innerGlowRadius,
        center = center,
        blendMode = BlendMode.Plus // Mode de fusion pour ajouter de la luminosité
    )
}

/**
 * ### drawLedPlasticBody
 *
 * Fonction de dessin pour le corps en plastique extérieur de la LED.
 *
 * @param center Centre de la LED.
 * @param size Taille de référence.
 * @param ledRadius Rayon du dôme en verre (utilisé pour dimensionner le corps).
 */
private fun DrawScope.drawLedPlasticBody(center: Offset, size: Float, ledRadius: Float) {
    // Rayon du corps en plastique (légèrement plus grand que le dôme)
    val outerRadius = ledRadius * 1.2f

    // Dégradé radial pour simuler l'aspect d'une surface en plastique sombre
    val plasticBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF232323), // Couleur sombre intérieure
            Color(0xFF1A1A1A)  // Couleur plus sombre extérieure
        ),
        center = center,
        radius = outerRadius
    )

    // Dessiner le cercle pour le corps en plastique
    drawCircle(
        brush = plasticBrush,
        radius = outerRadius,
        center = center
    )

    // Dessiner une fine bordure pour définir le contour du corps en plastique
    drawCircle(
        color = Color(0xFF1A1A1A),
        radius = outerRadius,
        center = center,
        style = Stroke(width = 1f) // Style de dessin en mode contour
    )
}

/**
 * ### drawGlassDome
 *
 * Fonction de dessin pour le dôme en verre transparent de la LED.
 *
 * @param center Centre de la LED.
 * @param color Couleur de la LED.
 * @param size Taille de référence.
 * @param ledRadius Rayon du dôme en verre.
 * @param isOn Indique si la LED est allumée.
 * @param intensity Intensité lumineuse actuelle.
 */
private fun DrawScope.drawGlassDome(
    center: Offset,
    color: Color,
    size: Float,
    ledRadius: Float,
    isOn: Boolean,
    intensity: Float
) {
    // Couleur de base du dôme en verre, plus ou moins transparente selon l'état et l'intensité
    val glassColor = if (isOn) {
        color.copy(alpha = 0.7f + (intensity * 0.3f)) // Plus intense et opaque quand allumée
    } else {
        color.copy(alpha = 0.2f) // Plus transparente quand éteinte
    }

    // Dessiner le cercle de base pour le dôme en verre
    drawCircle(
        color = glassColor,
        radius = ledRadius,
        center = center
    )

    // Créer un dégradé radial pour simuler la transparence et la brillance du verre
    val glassBrush = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = if (isOn) 0.7f * intensity else 0.2f), // Reflet blanc au centre
            glassColor.copy(alpha = if (isOn) 0.8f else 0.15f), // Couleur principale
            glassColor.copy(alpha = if (isOn) 0.6f else 0.1f)  // Bordure légèrement plus sombre
        ),
        center = center,
        radius = ledRadius
    )

    // Appliquer le dégradé avec un mode de fusion "Overlay" pour mélanger les couleurs de manière lumineuse
    drawCircle(
        brush = glassBrush,
        radius = ledRadius,
        center = center,
        alpha = if (isOn) 0.8f * intensity else 0.3f,
        blendMode = BlendMode.Overlay
    )

    // Créer un léger effet de "dôme" en ajoutant un dégradé blanc en haut
    val domeCurvature = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.1f), // Léger reflet blanc en haut
            Color.Transparent
        ),
        center = Offset(center.x, center.y - ledRadius * 0.1f), // Centre légèrement décalé vers le haut
        radius = ledRadius * 0.9f
    )

    // Dessiner l'effet de courbure
    drawCircle(
        brush = domeCurvature,
        radius = ledRadius * 0.9f,
        center = center
    )

    // Ajouter des reflets pour renforcer l'aspect "verre"
    drawLedReflections(center, ledRadius, isOn, intensity)

    // Dessiner un léger contour blanc pour définir les bords du dôme en verre
    drawCircle(
        color = Color.White.copy(alpha = 0.2f),
        radius = ledRadius,
        center = center,
        style = Stroke(width = 0.5f) // Fine bordure
    )
}

/**
 * ### drawLedReflections
 *
 * Fonction de dessin pour ajouter des reflets réalistes sur le dôme en verre de la LED.
 *
 * @param center Centre de la LED.
 * @param ledRadius Rayon du dôme en verre.
 * @param isOn Indique si la LED est allumée.
 * @param intensity Intensité lumineuse actuelle.
 */
private fun DrawScope.drawLedReflections(
    center: Offset,
    ledRadius: Float,
    isOn: Boolean,
    intensity: Float
) {
    // ### Reflet principal (en haut à gauche)
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
            Color.Transparent
        ),
        center = mainReflectionOffset,
        radius = mainReflectionRadius
    )
    withTransform({
        scale(0.8f, 0.6f, mainReflectionOffset) // Étirer pour un effet plus ovale
        rotate(-15f, mainReflectionOffset) // Légère rotation
    }) {
        drawCircle(
            brush = mainReflectionBrush,
            radius = mainReflectionRadius,
            center = mainReflectionOffset,
            blendMode = BlendMode.Screen // Mode de fusion lumineux
        )
    }

    // ### Reflet secondaire (plus petit, en bas à droite)
    val secondReflectionRadius = ledRadius * 0.2f
    val secondReflectionOffset = Offset(
        center.x + ledRadius * 0.3f,
        center.y + ledRadius * 0.2f
    )
    val secondReflectionAlpha = if (isOn) 0.4f * intensity else 0.15f
    val secondReflectionBrush = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = secondReflectionAlpha),
            Color.Transparent
        ),
        center = secondReflectionOffset,
        radius = secondReflectionRadius
    )
    withTransform({
        scale(0.7f, 0.4f, secondReflectionOffset) // Étirer légèrement
        rotate(30f, secondReflectionOffset) // Légère rotation
    }) {
        drawCircle(
            brush = secondReflectionBrush,
            radius = secondReflectionRadius,
            center = secondReflectionOffset,
            blendMode = BlendMode.Screen
        )
    }

    // ### Point de lumière brillant (pour simuler une réflexion intense)
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