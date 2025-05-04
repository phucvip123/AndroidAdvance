package com.app.androidadvance.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MyBottomBar(navController: NavController){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var navItemCustom = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFFFE8C00),
        unselectedIconColor = Color(0xFF9E9E9E),
        selectedTextColor = Color(0xFFFE8C00),
        unselectedTextColor = Color(0xFF9E9E9E),
        indicatorColor = Color.Transparent
    )
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Transparent
    ){
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Trang chủ") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home"){
                    popUpTo(0)
                }
            },
            colors = navItemCustom
        )
    }
}