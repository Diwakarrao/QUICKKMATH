package com.quickmath.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickmath.app.R
import com.quickmath.app.data.AppSettings
import com.quickmath.app.ui.theme.AppColors
import com.quickmath.app.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
) {
    val settings by viewModel.settings.collectAsState()
    var draft by remember(settings) { mutableStateOf(settings) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    if (draft != settings) {
                        androidx.compose.material3.TextButton(
                            onClick = {
                                viewModel.updateSettings { _ -> draft }
                                onBack()
                            },
                        ) {
                            Text(stringResource(R.string.save), color = AppColors.accent)
                        }
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(stringResource(R.string.display_mode), style = MaterialTheme.typography.labelMedium, color = AppColors.secondary)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !draft.cardSizeFull,
                    onClick = { draft = draft.copy(cardSizeFull = false); viewModel.hapticLight() },
                )
                Text(stringResource(R.string.half_screen), modifier = Modifier.padding(start = 8.dp), color = AppColors.ink)
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = draft.cardSizeFull,
                    onClick = { draft = draft.copy(cardSizeFull = true); viewModel.hapticLight() },
                )
                Text(stringResource(R.string.full_screen), modifier = Modifier.padding(start = 8.dp), color = AppColors.ink)
            }

            Text(stringResource(R.string.overlay_permission), style = MaterialTheme.typography.labelMedium, color = AppColors.secondary)
            Text(stringResource(R.string.overlay_desc), style = MaterialTheme.typography.bodySmall, color = AppColors.secondary)
            androidx.compose.material3.Button(
                onClick = { viewModel.hapticLight(); viewModel.openAppSettings() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.open_app_settings))
            }
            androidx.compose.material3.TextButton(
                onClick = { draft = draft.copy(overlayPermissionDeclined = false); viewModel.hapticLight() },
            ) {
                Text(stringResource(R.string.overlay_granted), color = AppColors.secondary)
            }

            Text(stringResource(R.string.vibration), style = MaterialTheme.typography.labelMedium, color = AppColors.secondary)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.vibration), color = AppColors.ink)
                Switch(
                    checked = draft.vibration,
                    onCheckedChange = { draft = draft.copy(vibration = it); viewModel.hapticLight() },
                )
            }

            Text(stringResource(R.string.fun_pop_ups), style = MaterialTheme.typography.labelMedium, color = AppColors.secondary)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.fun_pop_ups), color = AppColors.ink)
                Switch(
                    checked = draft.funPopups,
                    onCheckedChange = { draft = draft.copy(funPopups = it); viewModel.hapticLight() },
                )
            }
        }
    }
}
