package com.restable.library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.restable.library.app.App
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSplashScreen()
        setContent {
            App()
        }
    }

    private fun setSplashScreen() = lifecycleScope.launch {
        installSplashScreen().setKeepOnScreenCondition {
            //TODO: Splash screen logic
            false
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}