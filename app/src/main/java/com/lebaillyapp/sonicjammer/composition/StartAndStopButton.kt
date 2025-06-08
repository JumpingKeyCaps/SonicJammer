package com.lebaillyapp.sonicjammer.composition


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


// Couleurs (inchangées par rapport à la version précédente)
val UltraBezelOuterColor = Color(0xFF282828)
val UltraBezelMidColor = Color(0xFF484848)
val UltraBezelInnerColor = Color(0xFF303030)
val UltraSwitchSurfaceBase = Color(0xFF212121)
val UltraSwitchHighlight = Color(0xFF3F3F3F)
val UltraSwitchShadow = Color(0xFF151515)
// --- MODIFICATION : Couleur du reflet spéculaire moins intense ---
val UltraSpecularHighlightColor = Color(0x61656565) // Gris plus doux
val UltraMarkingPressedColor = Color(0x5C9E9E9E)
val UltraMarkingReleasedColor = Color(0x7CD0D0D0)

@Composable
fun ButtonRockerSwitchVertical(
    modifier: Modifier = Modifier,
    isChecked: Boolean, // true = 'I' (haut) enfoncé, false = 'O' (bas) enfoncé
    onToggle: (Boolean) -> Unit,
    width: Dp = 40.dp,
    height: Dp = 70.dp,
    cornerRadius: Dp = 8.dp,
    animationDuration: Int = 200,
    iconSource: Painter
) {
    val interactionSource = remember { MutableInteractionSource() }
    val density = LocalDensity.current

    // --- Configuration Animation ---
    // --- MODIFICATION : Angle légèrement augmenté ---
    val pressedAngleX = 5.0f
    val releasedAngleX = -5.0f
    // --- MODIFICATION : Ajout de l'échelle pour l'effet 3D ---
    val pressedScaleY = 0.96f // Légèrement plus petit si enfoncé
    val releasedScaleY = 1.04f // Légèrement plus grand si relevé

    val cameraDist = 12 * density.density // Gardons 12 pour l'instant

    // Angles
    val targetTopAngle = if (isChecked) pressedAngleX else releasedAngleX
    val targetBottomAngle = if (isChecked) releasedAngleX else pressedAngleX
    val topAngle by animateFloatAsState(targetTopAngle, tween(animationDuration), label = "Top Angle")
    val bottomAngle by animateFloatAsState(targetBottomAngle, tween(animationDuration), label = "Bottom Angle")

    // Échelle Y
    val targetTopScaleY = if (isChecked) pressedScaleY else releasedScaleY
    val targetBottomScaleY = if (isChecked) releasedScaleY else pressedScaleY
    val topScaleY by animateFloatAsState(targetTopScaleY, tween(animationDuration), label = "Top ScaleY")
    val bottomScaleY by animateFloatAsState(targetBottomScaleY, tween(animationDuration), label = "Bottom ScaleY")


    // Couleurs de dégradé Base (Diffuse) - Inchangé
    val targetTopColorStart = if (isChecked) UltraSwitchShadow else UltraSwitchHighlight
    val targetTopColorEnd = UltraSwitchSurfaceBase
    val targetBottomColorStart = UltraSwitchSurfaceBase
    val targetBottomColorEnd = if (isChecked) UltraSwitchHighlight else UltraSwitchShadow
    val topGradientStart by animateColorAsState(targetTopColorStart, tween(animationDuration), label = "Top Grad Start")
    val topGradientEnd by animateColorAsState(targetTopColorEnd, tween(animationDuration), label = "Top Grad End")
    val bottomGradientStart by animateColorAsState(targetBottomColorStart, tween(animationDuration), label = "Bottom Grad Start")
    val bottomGradientEnd by animateColorAsState(targetBottomColorEnd, tween(animationDuration), label = "Bottom Grad End")

    // Couleur des Marquages - Inchangé
    val targetTopMarkingColor = if (isChecked) UltraMarkingPressedColor else UltraMarkingReleasedColor
    val targetBottomMarkingColor = if (isChecked) UltraMarkingReleasedColor else UltraMarkingPressedColor
    val topMarkingColor by animateColorAsState(targetTopMarkingColor, tween(animationDuration), label = "Top Mark Color")
    val bottomMarkingColor by animateColorAsState(targetBottomMarkingColor, tween(animationDuration), label = "Bottom Mark Color")

    // Intensité du reflet spéculaire
    // --- MODIFICATION : Alpha max réduit pour un reflet plus doux ---
    val targetTopSpecularAlpha = if (isChecked) 0.0f else 0.4f // Max 0.4 au lieu de 0.8
    val targetBottomSpecularAlpha = if (isChecked) 0.4f else 0.0f // Max 0.4 au lieu de 0.8
    val topSpecularAlpha by animateFloatAsState(targetTopSpecularAlpha, tween(animationDuration), label = "Top Spec Alpha")
    val bottomSpecularAlpha by animateFloatAsState(targetBottomSpecularAlpha, tween(animationDuration), label = "Bottom Spec Alpha")

    // --- Structure Visuelle ---
    Box( // Conteneur principal / Bezel
        modifier = modifier
            .width(width)
            .height(height)
            //.shadow(1.dp, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            // Cadre multi-étapes
            .background(UltraBezelOuterColor)
            .padding(1.dp)
            .background(
                Brush.verticalGradient(listOf(UltraBezelMidColor, UltraBezelInnerColor)),
                RoundedCornerShape(cornerRadius - 1.dp)
            )
            .border(0.5.dp, Color.Black.copy(alpha = 0.6f), RoundedCornerShape(cornerRadius - 1.dp))
            .padding(2.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onToggle(!isChecked) },
        contentAlignment = Alignment.Center
    ) {
        // Préparer le pinceau spéculaire
        val specularBrush = Brush.radialGradient(
            colors = listOf(UltraSpecularHighlightColor, Color.Transparent), // Couleur déjà atténuée
            radius = width.value * 5f, // Rayon peut-être un peu plus petit aussi
            center = Offset(width.value / 2f, height.value * 0.1f) // Positionné vers le haut
        )

        Column( // Surface basculante
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(cornerRadius - 3.dp))
        ) {
            // --- Moitié Haute ('I' / On) ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationX = topAngle
                        // --- MODIFICATION : Application de l'échelle Y ---
                        scaleY = topScaleY
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                        cameraDistance = cameraDist
                    }
                    .background(
                        Brush.verticalGradient(listOf(topGradientStart, topGradientEnd))
                    )
                    .drawWithContent {
                        drawContent()
                        val alpha = topSpecularAlpha
                        if (alpha > 0.01f) {
                            clipRect {
                                drawRect(
                                    brush = specularBrush,
                                    alpha = alpha, // Alpha animé (max 0.4)
                                    blendMode = BlendMode.Plus
                                )
                            }
                        }
                    }
                    .clip(RoundedCornerShape(topStart = cornerRadius - 3.dp, topEnd = cornerRadius - 3.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = iconSource,
                    contentDescription = "On",
                    tint = topMarkingColor,
                    modifier = Modifier.size(width * 0.4f)
                )
            }

            Spacer(modifier = Modifier.height(0.5.dp).fillMaxWidth().background(Color.Black.copy(alpha = 0.6f)))

            // --- Moitié Basse ('O' / Off) ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationX = bottomAngle
                        // --- MODIFICATION : Application de l'échelle Y ---
                        scaleY = bottomScaleY
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                        cameraDistance = cameraDist
                    }
                    .background(
                        Brush.verticalGradient(listOf(bottomGradientStart, bottomGradientEnd))
                    )
                    .drawWithContent {
                        drawContent()
                        val alpha = bottomSpecularAlpha
                        if (alpha > 0.01f) {
                            clipRect{
                                drawRect(
                                    brush = specularBrush,
                                    alpha = alpha, // Alpha animé (max 0.4)
                                    blendMode = BlendMode.Plus
                                )
                            }
                        }
                    }
                    .clip(RoundedCornerShape(bottomStart = cornerRadius - 3.dp, bottomEnd = cornerRadius - 3.dp)),
                contentAlignment = Alignment.Center
            ) {
                RealisticLED(
                    isOn = isChecked,
                    color = Color(0xFFAB0020),
                    size = 10.0f,
                    haloSpacer = 3,
                    blinkInterval = 0
                )
            }
        }
    }
}