package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.screens.AlertsScreen
import com.example.screens.AuthScreen
import com.example.screens.DashboardScreen
import com.example.screens.ProfileScreen
import com.example.screens.RadarScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { 
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        if (navBackStackEntry?.destination?.route != "auth") {
                            WeatherBottomNavBar(navController) 
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "auth",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("auth") { AuthScreen(navController) }
                        composable("dashboard") { DashboardScreen() }
                        composable("radar") { RadarScreen() }
                        composable("alerts") { AlertsScreen() }
                        composable("profile") { ProfileScreen(navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherBottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem("dashboard", "Dashboard", Icons.Filled.WbSunny, currentRoute, navController)
        NavItem("radar", "Radar", Icons.Filled.Map, currentRoute, navController)
        NavItem("alerts", "Alerts", Icons.Filled.Notifications, currentRoute, navController)
        NavItem("profile", "Profile", Icons.Filled.Person, currentRoute, navController)
    }
}

@Composable
fun NavItem(route: String, label: String, icon: ImageVector, currentRoute: String?, navController: NavHostController) {
    val selected = currentRoute == route
    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    
    Column(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .padding(horizontal = if (selected) 20.dp else 12.dp, vertical = if (selected) 8.dp else 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.size(28.dp).scale(if (selected) 1.1f else 1f)
        ) {
            Icon(icon, contentDescription = label, tint = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else color, modifier = Modifier.size(24.dp))
        }
        if (selected) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        } else {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = color)
        }
    }
}
