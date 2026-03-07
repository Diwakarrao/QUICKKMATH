package com.quickmath.app.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quickmath.app.ui.theme.AppColors
import com.quickmath.app.ui.viewmodel.FunPopupType
import kotlinx.coroutines.delay

@Composable
fun FunPopupDialog(
    type: FunPopupType,
    vibrationEnabled: Boolean,
    onDismiss: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F)),
    ) {
        when (type) {
            FunPopupType.DOTS -> {
                val leftScale by rememberInfiniteTransition().animateFloat(
                    initialValue = 1f,
                    targetValue = 1.25f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(900, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ),
                    label = "left",
                )
                val rightScale by rememberInfiniteTransition().animateFloat(
                    initialValue = 1f,
                    targetValue = 1.3f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ),
                    label = "right",
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    ) {
                        Box(
                            Modifier
                                .size(80.dp)
                                .scale(leftScale)
                                .background(Color(0xFFEF4444), CircleShape),
                        )
                        Box(
                            Modifier
                                .size(80.dp)
                                .scale(rightScale)
                                .background(Color(0xFF22C55E), CircleShape),
                        )
                    }
                }
            }
            FunPopupType.RING -> {
                val rotation by rememberInfiniteTransition().animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1400, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
                    label = "ring",
                )
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        Modifier
                            .size(90.dp)
                            .rotate(rotation)
                            .drawBehind {
                                drawCircle(
                                    color = Color.White,
                                    radius = size.minDimension / 2 - 6.dp.toPx(),
                                    style = Stroke(width = 6.dp.toPx()),
                                )
                            },
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        delay(20_000)
        onDismiss()
    }
}
