package com.quickmath.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickmath.app.R
import com.quickmath.app.data.Language
import com.quickmath.app.ui.theme.AppColors
import com.quickmath.app.ui.viewmodel.AppViewModel

@Composable
fun LanguageScreen(
    viewModel: AppViewModel,
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val settings = viewModel.settings
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.select_language),
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.ink,
        )
        Spacer(Modifier.height(24.dp))
        Language.entries.forEach { lang ->
            OutlinedCard(
                onClick = {
                    viewModel.hapticLight()
                    viewModel.updateSettings { it.copy(language = lang) }
                },
                modifier = Modifier.padding(vertical = 6.dp),
            ) {
                Text(
                    text = when (lang) {
                        Language.EN -> stringResource(R.string.english)
                        Language.TE -> stringResource(R.string.telugu)
                        Language.HI -> stringResource(R.string.hindi)
                    },
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                viewModel.hapticLight()
                onContinue()
            },
        ) {
            Text(stringResource(R.string.continue_btn))
        }
    }
}
