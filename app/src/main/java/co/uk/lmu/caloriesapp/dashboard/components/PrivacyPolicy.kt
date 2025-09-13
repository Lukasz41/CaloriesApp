package co.uk.lmu.caloriesapp.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // display the screen title
        Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF1976D2)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // show the description
        Text(
            text = "We value your privacy. This app does not collect or store any personal data. " +
                    "All information entered stays on your device.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.weight(1f))
        // back button to return to the previous screen
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Back", color = Color.White)
        }
    }
}
