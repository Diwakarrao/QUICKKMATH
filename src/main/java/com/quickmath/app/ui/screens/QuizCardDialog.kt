package com.quickmath.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickmath.app.R
import com.quickmath.app.data.QuizCard
import com.quickmath.app.ui.theme.AppColors
import com.quickmath.app.ui.viewmodel.AppViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizCardDialog(
    card: QuizCard,
    answerIndex: Int?,
    timerComplete: Boolean,
    cardDurationMs: Long,
    cardSizeFull: Boolean,
    vibrationEnabled: Boolean,
    viewModel: AppViewModel,
) {
    var remainingMs by remember { mutableLongStateOf(cardDurationMs) }
    LaunchedEffect(card) {
        remainingMs = cardDurationMs
        val start = System.currentTimeMillis()
        while (remainingMs > 0) {
            delay(100)
            remainingMs = maxOf(0, cardDurationMs - (System.currentTimeMillis() - start))
        }
        viewModel.markTimerComplete()
    }

    val progress by animateFloatAsState(
        targetValue = remainingMs.toFloat() / cardDurationMs.toFloat(),
        label = "progress",
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = if (cardSizeFull) Alignment.Center else Alignment.BottomCenter,
    ) {
        Card(
            modifier = Modifier
                .then(
                    if (cardSizeFull) Modifier
                        .fillMaxWidth(0.92f)
                        .padding(24.dp)
                    else Modifier.fillMaxWidth()
                ),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = if (cardSizeFull) 28.dp else 0.dp, bottomEnd = if (cardSizeFull) 28.dp else 0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppColors.accent),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("QM", style = MaterialTheme.typography.labelMedium, color = Color.White)
                        }
                        Text(" QUICK MATH", style = MaterialTheme.typography.labelMedium, color = AppColors.accent, modifier = Modifier.padding(start = 8.dp))
                    }
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(40.dp),
                        color = AppColors.accent,
                    )
                }
                Text(
                    if (timerComplete) stringResource(R.string.time_up) else stringResource(R.string.thinking),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.secondary,
                )
                Text(
                    card.question,
                    style = MaterialTheme.typography.headlineMedium,
                    color = AppColors.ink,
                    modifier = Modifier.fillMaxWidth(),
                )
                card.options.forEachIndexed { idx, opt ->
                    val text = opt.replace(Regex("^[A-D]\\. "), "")
                    val selected = answerIndex == idx
                    val isCorrect = timerComplete && idx == card.correctIndex
                    val isWrong = timerComplete && selected && idx != card.correctIndex
                    OutlinedCard(
                        onClick = {
                            if (answerIndex == null) viewModel.onAnswer(idx)
                        },
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = when {
                                isCorrect -> AppColors.success.copy(alpha = 0.2f)
                                isWrong -> AppColors.danger.copy(alpha = 0.2f)
                                selected -> AppColors.accentLight
                                else -> Color.Transparent
                            },
                        ),
                    ) {
                        Text(
                            text = text,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.ink,
                        )
                    }
                }
                if (timerComplete) {
                    Button(
                        onClick = { viewModel.dismissCard() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        }
    }
}
