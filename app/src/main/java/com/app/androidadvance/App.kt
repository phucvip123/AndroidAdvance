package com.app.androidadvance

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.androidadvance.services.UserService
import com.app.androidadvance.ui.screens.HomeScreen
import com.app.androidadvance.ui.screens.LoginScreen
import com.app.androidadvance.ui.screens.RegisterScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(){
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
            innerPadding ->
        NavGraph(modifier = Modifier.padding(innerPadding),navController = navController)
    }
}
@Composable
fun NavGraph(modifier: Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if(!UserService.isLogin()) "register" else "home"
    )
    {
        composable("register"){
            RegisterScreen(navController)
        }
        composable("login"){
            LoginScreen(navController)
        }
        composable("home"){
            HomeScreen(navController)
        }

    }
}