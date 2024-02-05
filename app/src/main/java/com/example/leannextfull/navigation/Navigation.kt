package com.example.leannextfull.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.leannextfull.screens.MainScreen
import com.example.leannextfull.screens.SplashScreen
import com.example.leannextfull.viewModel.MainViewModel
/**Навгиция после запуска SplachScreen*/
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen( navController = navController )
        }

        // Main Screen
        composable("main_screen") {
            MainScreen(viewModel)
        }
    }
}