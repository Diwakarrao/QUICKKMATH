package com.quickmath.app.data

enum class Language { EN, TE, HI }

enum class Difficulty { EASY, MEDIUM, HARD }

data class AppSettings(
    val cardSizeFull: Boolean = false,
    val strictMode: Boolean = false,
    val audioCue: Boolean = true,
    val vibration: Boolean = true,
    val funPopups: Boolean = false,
    val cardsBeforePopup: Int = 3,
    val durationMs: Long = 1_200_000L,
    val frequencyMs: Long = 60_000L,
    val cardDurationMs: Long = 15_000L,
    val language: Language = Language.EN,
    val premiumToken: String = "",
    val onboardingDone: Boolean = false,
    val userVerified: Boolean = false,
    val overlayPermissionDeclined: Boolean = false,
    val currentDifficulty: Difficulty = Difficulty.EASY,
    val freeTrialStartedAt: Long? = null,
    val otpSkippedAt: Long? = null,
) {
    companion object {
        const val STORAGE_KEY = "qm_settings_v2"
        const val FREE_TRIAL_MS = 3L * 24 * 60 * 60 * 1000
        const val OTP_SKIP_GRACE_MS = 24L * 60 * 60 * 1000

        fun isPremium(token: String): Boolean =
            token.isNotEmpty() && token.startsWith("qm_")

        fun AppSettings.isInFreeTrial(): Boolean {
            val started = freeTrialStartedAt ?: return false
            return System.currentTimeMillis() - started < FREE_TRIAL_MS
        }

        fun AppSettings.freeTrialDaysLeft(): Int {
            val started = freeTrialStartedAt ?: return 0
            val elapsed = System.currentTimeMillis() - started
            val remaining = FREE_TRIAL_MS - elapsed
            return maxOf(0, (remaining / (24 * 60 * 60 * 1000)).toInt())
        }

        fun AppSettings.hasAnyPremium(): Boolean =
            isPremium(premiumToken) || isInFreeTrial()

        fun AppSettings.mustVerifyOtp(): Boolean {
            if (userVerified) return false
            if (!onboardingDone) return false
            val skipped = otpSkippedAt ?: return false
            val skipGraceExpired = System.currentTimeMillis() - skipped >= OTP_SKIP_GRACE_MS
            val trialEnded = freeTrialStartedAt != null &&
                System.currentTimeMillis() - freeTrialStartedAt!! >= FREE_TRIAL_MS
            return skipGraceExpired || trialEnded
        }
    }
}
