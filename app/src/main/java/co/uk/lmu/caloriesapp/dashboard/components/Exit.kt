package co.uk.lmu.caloriesapp.dashboard.components

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ExitScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    // show a toast message to inform the user the app is exiting
    Toast.makeText(context, "Exiting app...", Toast.LENGTH_SHORT).show()
    // close all activities and exit the app
    activity?.finishAffinity()
}
