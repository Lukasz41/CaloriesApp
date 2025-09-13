package co.uk.lmu.caloriesapp.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun SettingsScreen(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: ""

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 96.dp)
                .padding(16.dp)
        ) {
            // screen title
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // menu items for navigation
            SettingsMenuItem("Shopping List") {
                navController.navigate("shopping_list")
            }

            SettingsMenuItem("Privacy Policy") {
                navController.navigate("privacy_policy")
            }
            SettingsMenuItem("App Reset ") {
                navController.navigate("app_reset")
            }

            SettingsMenuItem("Exit") {
                navController.navigate("exit")
            }
        }
        // bottom navigation bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            HorizontalDivider(thickness = 1.dp)
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}

@Composable
fun SettingsMenuItem(title: String, onClick: () -> Unit) {
    // reusable settings item with click action
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
        HorizontalDivider(thickness = 2.dp, color = Color(0xFF1976D2))
    }
}
