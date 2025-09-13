package co.uk.lmu.caloriesapp.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    NavigationBar(
        containerColor = Color(0xFF1976D2),
        contentColor = Color.Black
    ) {
        // dashboard navigation item
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = {
                if (currentRoute != "dashboard") navController.navigate("dashboard")
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Meal Plan") },
            label = { Text("Meal Plan") }
        )
        // measurements navigation item
        NavigationBarItem(
            selected = currentRoute == "measurements",
            onClick = {
                if (currentRoute != "measurements") navController.navigate("measurements")
            },
            icon = { Icon(Icons.Default.Star, contentDescription = "Measurements") },
            label = { Text("Measurements") }
        )
        // settings navigation item
        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") navController.navigate("settings")
            },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}
