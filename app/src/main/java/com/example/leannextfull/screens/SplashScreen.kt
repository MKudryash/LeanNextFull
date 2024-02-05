package com.example.leannextfull.screens

import android.content.res.Configuration
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leannextfull.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0.5f)
    }
    var darkTheme: Boolean = isSystemInDarkTheme()
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        // portrait mode
     LaunchedEffect(key1 = true) {
                scale.animateTo(
                    targetValue = 1.5f,
                    // tween Animation
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = {
                            OvershootInterpolator(0.5f).getInterpolation(it)
                        })
                )
                // Customize the delay time
                delay(1500L)
                navController.navigate("main_screen"){ popUpTo("splash_screen") {
                    inclusive = true
                }}
            }

        BoxWithConstraints(
        ) {
            val derivedDimension = this.maxWidth
            // Image
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                    .padding(20.dp)
            ) {
                // Change the logo
                Image(
                    painter = when (darkTheme) {
                        true -> painterResource(id = R.drawable.logo_icon_invert)
                        false -> painterResource(id = R.drawable.logo_icon)
                    },
                    contentDescription = "Logo",
                    modifier = Modifier.scale(scale.value)
                )
            }
        }
    }

}