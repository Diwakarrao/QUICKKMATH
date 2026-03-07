package com.quickmath.app.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quickmath.app.data.AppSettings
import com.quickmath.app.data.Difficulty
import com.quickmath.app.data.MathQuiz
import com.quickmath.app.data.QuizCard
import com.quickmath.app.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job

data class SessionState(
    val active: Boolean = false,
    val cardsShown: Int = 0,
)

data class CardUiState(
    val visible: Boolean = false,
    val card: QuizCard? = null,
    val answerIndex: Int? = null,
    val timerComplete: Boolean = false,
)

data class FunPopupState(
    val visible: Boolean = false,
    val type: FunPopupType = FunPopupType.DOTS,
)

enum class FunPopupType { DOTS, RING }

class AppViewModel(
    private val context: Context,
    private val repo: SettingsRepository,
) : ViewModel() {

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private val _session = MutableStateFlow(SessionState())
    val session: StateFlow<SessionState> = _session.asStateFlow()

    private val _cardUi = MutableStateFlow(CardUiState())
    val cardUi: StateFlow<CardUiState> = _cardUi.asStateFlow()

    private val _funPopup = MutableStateFlow(FunPopupState())
    val funPopup: StateFlow<FunPopupState> = _funPopup.asStateFlow()

    private var sessionJob: Job? = null
    private var cardJob: Job? = null
    private var popupCount = 0
    private var pendingPopup = false
    private var cardShownAt = 0L
    private var answerLockedAt: Long? = null

    init {
        viewModelScope.launch {
            repo.settingsFlow.collect { _settings.value = it }
        }
    }

    fun updateSettings(transform: (AppSettings) -> AppSettings) {
        viewModelScope.launch {
            repo.updateSettings(transform)
        }
    }

    fun updateSettings(new: AppSettings) {
        viewModelScope.launch {
            repo.updateSettings(new)
        }
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun hapticLight() {
        if (!_settings.value.vibration) return
        (context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)?.let { v ->
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                v.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else @Suppress("DEPRECATION") { v.vibrate(30) }
        }
    }

    fun hapticMedium() {
        if (!_settings.value.vibration) return
        (context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)?.let { v ->
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else @Suppress("DEPRECATION") { v.vibrate(50) }
        }
    }

    fun startSession() {
        if (_session.value.active) return
        _session.value = SessionState(active = true, cardsShown = 0)
        popupCount = 0
        val freq = maxOf(_settings.value.frequencyMs, 30_000L)
        val duration = _settings.value.durationMs
        sessionJob?.cancel()
        sessionJob = viewModelScope.launch {
            delay(45_000) // first card after 45s in original; we use 2s for testing
            if (!_session.value.active) return@launch
            showNextCard()
            cardJob = viewModelScope.launch {
                while (_session.value.active) {
                    delay(freq)
                    if (!_session.value.active) break
                    showNextCard()
                }
            }
            delay(duration)
            stopSession()
        }
    }

    fun stopSession() {
        sessionJob?.cancel()
        cardJob?.cancel()
        _session.value = SessionState()
        _cardUi.value = _cardUi.value.copy(visible = false, card = null)
        _funPopup.value = _funPopup.value.copy(visible = false)
    }

    private fun showNextCard() {
        val s = _settings.value
        val card = MathQuiz.generateQuizCard(s.currentDifficulty)
        _cardUi.value = _cardUi.value.copy(
            visible = true,
            card = card,
            answerIndex = null,
            timerComplete = false,
        )
        cardShownAt = System.currentTimeMillis()
        answerLockedAt = null
        _session.value = _session.value.copy(cardsShown = _session.value.cardsShown + 1)

        if (s.funPopups) {
            popupCount++
            if (popupCount >= s.cardsBeforePopup) {
                popupCount = 0
                pendingPopup = true
            }
        }
    }

    fun onAnswer(index: Int) {
        val c = _cardUi.value
        if (c.answerIndex != null) return
        hapticMedium()
        answerLockedAt = System.currentTimeMillis()
        _cardUi.value = _cardUi.value.copy(answerIndex = index)
    }

    fun markTimerComplete() {
        _cardUi.update { it.copy(timerComplete = true) }
    }

    fun dismissCard() {
        val c = _cardUi.value
        if (!c.timerComplete || c.card == null) return
        hapticLight()
        val s = _settings.value
        val responseMs = (answerLockedAt ?: cardShownAt) - cardShownAt
        val wasCorrect = c.answerIndex == c.card.correctIndex
        val newDiff = MathQuiz.adaptDifficulty(s.currentDifficulty, responseMs, wasCorrect)
        updateSettings { it.copy(currentDifficulty = newDiff) }

        _cardUi.value = _cardUi.value.copy(visible = false, card = null, answerIndex = null, timerComplete = false)

        if (pendingPopup) {
            pendingPopup = false
            viewModelScope.launch {
                delay(500)
                _funPopup.value = _funPopup.value.copy(
                    visible = true,
                    type = if (kotlin.random.Random.nextBoolean()) FunPopupType.DOTS else FunPopupType.RING,
                )
                if (s.vibration) {
                    repeat(25) {
                        delay(800)
                        hapticLight()
                    }
                }
                delay(20_000)
                _funPopup.value = _funPopup.value.copy(visible = false)
            }
        }
    }

    fun dismissFunPopup() {
        _funPopup.value = _funPopup.value.copy(visible = false)
    }
}

class AppViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = SettingsRepository(context)
        return AppViewModel(context, repo) as T
    }
}
