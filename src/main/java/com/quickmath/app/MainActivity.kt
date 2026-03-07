package com.quickmath.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.quickmath.app.ui.navigation.QuickMathNavHost
import com.quickmath.app.ui.theme.QuickMathTheme
import com.quickmath.app.ui.theme.AppColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickMathTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = AppColors.surface) {
                    val navController = rememberNavController()
                    val viewModel: com.quickmath.app.ui.viewmodel.AppViewModel = viewModel(
                        factory = com.quickmath.app.ui.viewmodel.AppViewModelFactory(applicationContext)
                    )
                    QuickMathNavHost(
                        navController = navController,
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}
