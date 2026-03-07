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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickmath.app.R
import com.quickmath.app.ui.theme.AppColors
import com.quickmath.app.ui.viewmodel.AppViewModel

@Composable
fun PrivacyScreen(
    viewModel: AppViewModel,
    onAccept: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.privacy),
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.ink,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.privacy_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.secondary,
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                viewModel.hapticLight()
                onAccept()
            },
        ) {
            Text(stringResource(R.string.accept_and_continue))
        }
    }
}
