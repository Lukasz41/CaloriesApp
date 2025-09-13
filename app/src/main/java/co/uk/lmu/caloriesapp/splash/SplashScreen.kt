package co.uk.lmu.caloriesapp.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.uk.lmu.caloriesapp.registration.RegistrationViewModel
import kotlinx.coroutines.delay

// displays a splash screen for 2 seconds and navigates based on whether a user is registered
@Composable
fun SplashScreen(navController: NavController, registrationViewModel: RegistrationViewModel) {
    LaunchedEffect(Unit) {
        delay(2000L)
        registrationViewModel.isUserRegistered { isRegistered ->
            if (isRegistered) {
                navController.navigate("dashboard") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("create_account") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Calorie Tracker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
