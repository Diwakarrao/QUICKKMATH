package com.quickmath.app.ui.navigation

import androidx.compose.foundation.layout.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quickmath.app.data.AppSettings
import com.quickmath.app.ui.screens.AboutScreen
import com.quickmath.app.ui.screens.HomeScreen
import com.quickmath.app.ui.screens.LanguageScreen
import com.quickmath.app.ui.screens.OtpScreen
import com.quickmath.app.ui.screens.PermissionScreen
import com.quickmath.app.ui.screens.PrivacyScreen
import com.quickmath.app.ui.screens.SettingsScreen
import com.quickmath.app.ui.screens.SubscribeScreen
import com.quickmath.app.ui.viewmodel.AppViewModel

const val ROUTE_BOOTSTRAP = "bootstrap"
const val ROUTE_LANGUAGE = "language"
const val ROUTE_OTP = "otp"
const val ROUTE_PRIVACY = "privacy"
const val ROUTE_PERMISSION = "permission"
const val ROUTE_HOME = "home"
const val ROUTE_SETTINGS = "settings"
const val ROUTE_SUBSCRIBE = "subscribe"
const val ROUTE_ABOUT = "about"

@Composable
fun QuickMathNavHost(
    navController: NavHostController,
    viewModel: AppViewModel,
) {
    val settings by viewModel.settings.collectAsState(initial = AppSettings())

    NavHost(
        navController = navController,
        startDestination = ROUTE_BOOTSTRAP,
    ) {
        composable(ROUTE_BOOTSTRAP) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
            )
            LaunchedEffect(settings) {
                val target = when {
                    !settings.onboardingDone -> ROUTE_LANGUAGE
                    settings.mustVerifyOtp() -> ROUTE_OTP
                    else -> ROUTE_HOME
                }
                navController.navigate(target) { popUpTo(ROUTE_BOOTSTRAP) { inclusive = true } }
            }
        }
        composable(ROUTE_LANGUAGE) {
            LanguageScreen(
                viewModel = viewModel,
                onContinue = { navController.navigate(ROUTE_OTP) { popUpTo(ROUTE_LANGUAGE) { inclusive = true } } },
            )
        }
        composable(ROUTE_OTP) {
            OtpScreen(
                viewModel = viewModel,
                onVerified = { navController.navigate(ROUTE_PRIVACY) { popUpTo(ROUTE_OTP) { inclusive = true } } },
                onSkip = { navController.navigate(ROUTE_PRIVACY) { popUpTo(ROUTE_OTP) { inclusive = true } } },
            )
        }
        composable(ROUTE_PRIVACY) {
            PrivacyScreen(
                viewModel = viewModel,
                onAccept = { navController.navigate(ROUTE_PERMISSION) { popUpTo(ROUTE_PRIVACY) { inclusive = true } } },
            )
        }
        composable(ROUTE_PERMISSION) {
            PermissionScreen(
                viewModel = viewModel,
                onDone = { navController.navigate(ROUTE_HOME) { popUpTo(0) { inclusive = true } } },
            )
        }
        composable(ROUTE_HOME) {
            HomeScreen(
                viewModel = viewModel,
                onSettings = { navController.navigate(ROUTE_SETTINGS) },
                onSubscribe = { navController.navigate(ROUTE_SUBSCRIBE) },
                onAbout = { navController.navigate(ROUTE_ABOUT) },
            )
        }
        composable(ROUTE_SETTINGS) {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
        composable(ROUTE_SUBSCRIBE) {
            SubscribeScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
        composable(ROUTE_ABOUT) {
            AboutScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
