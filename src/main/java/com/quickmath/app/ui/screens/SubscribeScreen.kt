package com.quickmath.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickmath.app.R
import com.quickmath.app.ui.theme.AppColors
import com.quickmath.app.ui.viewmodel.AppViewModel

@Composable
fun SubscribeScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(AppColors.accent)
            .padding(24.dp),
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.Close, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                stringResource(R.string.choose_plan),
                style = MaterialTheme.typography.headlineMedium,
                color = androidx.compose.ui.graphics.Color.White,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.trial_note),
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f),
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    viewModel.hapticLight()
                    viewModel.updateSettings { it.copy(premiumToken = "qm_${System.currentTimeMillis()}", strictMode = true) }
                    onBack()
                },
            ) {
                Text(stringResource(R.string.join_premium))
            }
        }
    }
}
