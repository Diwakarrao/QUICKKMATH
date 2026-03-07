package com.quickmath.app.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickmath.app.R
import com.quickmath.app.ui.theme.AppColors
import com.quickmath.app.ui.viewmodel.AppViewModel

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onSettings: () -> Unit,
    onSubscribe: () -> Unit,
    onAbout: () -> Unit,
) {
    val settings by viewModel.settings.collectAsState()
    val session by viewModel.session.collectAsState()
    val cardUi by viewModel.cardUi.collectAsState()
    val funPopup by viewModel.funPopup.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        ) {
                            if (session.active) viewModel.stopSession()
                            else viewModel.startSession()
                        },
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (session.active) AppColors.success else AppColors.accent),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "QM",
                            style = MaterialTheme.typography.titleMedium,
                            color = androidx.compose.ui.graphics.Color.White,
                        )
                    }
                    Spacer(Modifier.size(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                stringResource(R.string.quick_math),
                                style = MaterialTheme.typography.headlineMedium,
                                color = AppColors.ink,
                            )
                            Spacer(Modifier.size(6.dp))
                            if (session.active) {
                                val scale by rememberInfiniteTransition().animateFloat(
                                    initialValue = 1f,
                                    targetValue = 1.4f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(600),
                                        repeatMode = RepeatMode.Reverse,
                                    ),
                                )
                                Box(
                                    Modifier
                                        .size(8.dp)
                                        .scale(scale)
                                        .clip(CircleShape)
                                        .background(AppColors.success),
                                )
                            }
                        }
                        Text(
                            if (session.active) stringResource(R.string.tap_hint_active)
                            else stringResource(R.string.tap_hint_start),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.secondary,
                        )
                    }
                }
                IconButton(onClick = onSettings) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = AppColors.ink)
                }
            }

            Spacer(Modifier.height(24.dp))

            if (settings.overlayPermissionDeclined) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = AppColors.accentLight),
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    Text(
                        stringResource(R.string.overlay_warning),
                        Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.secondary,
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (session.active) AppColors.accentLight.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.surface,
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            if (session.active) stringResource(R.string.session_running)
                            else stringResource(R.string.start_session_subtitle),
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.ink,
                        )
                        Text(
                            if (session.active) stringResource(R.string.cards_shown, session.cardsShown)
                            else stringResource(R.string.start_session_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.secondary,
                        )
                    }
                    if (session.active) {
                        androidx.compose.material3.Button(
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AppColors.danger),
                            onClick = { viewModel.hapticMedium(); viewModel.stopSession() },
                        ) {
                            Text(stringResource(R.string.stop_session), color = androidx.compose.ui.graphics.Color.White)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onAbout() },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.about_us), style = MaterialTheme.typography.labelMedium, color = AppColors.ink)
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSubscribe() },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.choose_plan), style = MaterialTheme.typography.labelMedium, color = AppColors.ink)
                    }
                }
            }
        }

        if (cardUi.visible && cardUi.card != null) {
            QuizCardDialog(
                card = cardUi.card!!,
                answerIndex = cardUi.answerIndex,
                timerComplete = cardUi.timerComplete,
                cardDurationMs = settings.cardDurationMs,
                cardSizeFull = settings.cardSizeFull,
                vibrationEnabled = settings.vibration,
                viewModel = viewModel,
            )
        }

        if (funPopup.visible) {
            FunPopupDialog(
                type = funPopup.type,
                vibrationEnabled = settings.vibration,
                onDismiss = viewModel::dismissFunPopup,
            )
        }
    }
}
