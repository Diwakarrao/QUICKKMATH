package com.quickmath.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppSettings.STORAGE_KEY)

class SettingsRepository(private val context: Context) {

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            cardSizeFull = prefs[Keys.cardSizeFull] ?: false,
            strictMode = prefs[Keys.strictMode] ?: false,
            audioCue = prefs[Keys.audioCue] ?: true,
            vibration = prefs[Keys.vibration] ?: true,
            funPopups = prefs[Keys.funPopups] ?: false,
            cardsBeforePopup = (prefs[Keys.cardsBeforePopup] ?: 3L).toInt(),
            durationMs = prefs[Keys.durationMs] ?: 1_200_000L,
            frequencyMs = prefs[Keys.frequencyMs] ?: 60_000L,
            cardDurationMs = prefs[Keys.cardDurationMs] ?: 15_000L,
            language = (prefs[Keys.language] ?: "EN").let { Language.valueOf(it) },
            premiumToken = prefs[Keys.premiumToken] ?: "",
            onboardingDone = prefs[Keys.onboardingDone] ?: false,
            userVerified = prefs[Keys.userVerified] ?: false,
            overlayPermissionDeclined = prefs[Keys.overlayPermissionDeclined] ?: false,
            currentDifficulty = (prefs[Keys.currentDifficulty] ?: "EASY").let { Difficulty.valueOf(it) },
            freeTrialStartedAt = prefs[Keys.freeTrialStartedAt],
            otpSkippedAt = prefs[Keys.otpSkippedAt],
        )
    }

    suspend fun updateSettings(transform: (AppSettings) -> AppSettings) {
        val current = settingsFlow.first()
        updateSettings(transform(current))
    }

    suspend fun updateSettings(new: AppSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.cardSizeFull] = new.cardSizeFull
            prefs[Keys.strictMode] = new.strictMode
            prefs[Keys.audioCue] = new.audioCue
            prefs[Keys.vibration] = new.vibration
            prefs[Keys.funPopups] = new.funPopups
            prefs[Keys.cardsBeforePopup] = new.cardsBeforePopup.toLong()
            prefs[Keys.durationMs] = new.durationMs
            prefs[Keys.frequencyMs] = new.frequencyMs
            prefs[Keys.cardDurationMs] = new.cardDurationMs
            prefs[Keys.language] = new.language.name
            prefs[Keys.premiumToken] = new.premiumToken
            prefs[Keys.onboardingDone] = new.onboardingDone
            prefs[Keys.userVerified] = new.userVerified
            prefs[Keys.overlayPermissionDeclined] = new.overlayPermissionDeclined
            prefs[Keys.currentDifficulty] = new.currentDifficulty.name
            
            if (new.freeTrialStartedAt != null) {
                prefs[Keys.freeTrialStartedAt] = new.freeTrialStartedAt
            } else {
                prefs.remove(Keys.freeTrialStartedAt)
            }
            
            if (new.otpSkippedAt != null) {
                prefs[Keys.otpSkippedAt] = new.otpSkippedAt
            } else {
                prefs.remove(Keys.otpSkippedAt)
            }
        }
    }

    private object Keys {
        val cardSizeFull = booleanPreferencesKey("cardSizeFull")
        val strictMode = booleanPreferencesKey("strictMode")
        val audioCue = booleanPreferencesKey("audioCue")
        val vibration = booleanPreferencesKey("vibration")
        val funPopups = booleanPreferencesKey("funPopups")
        val cardsBeforePopup = longPreferencesKey("cardsBeforePopup")
        val durationMs = longPreferencesKey("durationMs")
        val frequencyMs = longPreferencesKey("frequencyMs")
        val cardDurationMs = longPreferencesKey("cardDurationMs")
        val language = stringPreferencesKey("language")
        val premiumToken = stringPreferencesKey("premiumToken")
        val onboardingDone = booleanPreferencesKey("onboardingDone")
        val userVerified = booleanPreferencesKey("userVerified")
        val overlayPermissionDeclined = booleanPreferencesKey("overlayPermissionDeclined")
        val currentDifficulty = stringPreferencesKey("currentDifficulty")
        val freeTrialStartedAt = longPreferencesKey("freeTrialStartedAt")
        val otpSkippedAt = longPreferencesKey("otpSkippedAt")
    }
}
