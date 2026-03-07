package com.quickmath.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickmath.app.R
import com.quickmath.app.ui.theme.AppColors
import com.quickmath.app.ui.viewmodel.AppViewModel

@Composable
fun OtpScreen(
    viewModel: AppViewModel,
    onVerified: () -> Unit,
    onSkip: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.enter_phone),
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.ink,
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.hapticMedium()
                onVerified()
            },
        ) {
            Text(stringResource(R.string.send_otp))
        }
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = {
            viewModel.hapticLight()
            viewModel.updateSettings {
                it.copy(otpSkippedAt = System.currentTimeMillis())
            }
            onSkip()
        }) {
            Text(stringResource(R.string.not_now))
        }
    }
}
