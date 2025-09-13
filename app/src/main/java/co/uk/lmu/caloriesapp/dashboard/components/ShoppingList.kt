package co.uk.lmu.caloriesapp.dashboard.components

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.util.*
import co.uk.lmu.caloriesapp.dashboard.BottomNavBar
import co.uk.lmu.caloriesapp.dashboard.ShoppingListViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun ShoppingListScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: ""
    val context = LocalContext.current
    var productInput by remember { mutableStateOf("") }

    val productList = viewModel.shoppingList
    val primaryColor = Color(0xFF1976D2)
    // setup voice input launcher to receive spoken product names
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spokenText = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()

        spokenText?.let {
            viewModel.addProduct(it)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 96.dp)
                .padding(16.dp)
        ) {
            // screen title
            Text(
                text = "Shopping List",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // input field for typing a product name
            OutlinedTextField(
                value = productInput,
                onValueChange = { productInput = it },
                label = { Text("Add Product") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // action buttons to add or clear the list
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (productInput.isNotBlank()) {
                            val trimmed = productInput.trim()
                            viewModel.addProduct(trimmed)
                            productInput = ""
                        } else {
                            Toast.makeText(context, "Enter a product name", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Add", color = Color.White)
                }

                Button(
                    onClick = {
                        viewModel.clearAllProducts()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Clear List", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // display the list of products
            val products = productList.collectAsState().value

            LazyColumn {
                items(products) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        IconButton(onClick = {
                            viewModel.removeProduct(product)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete product",
                                tint = Color.Red
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }

        }
        // bottom section with voice input and navigation bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    startVoiceInput(context, speechLauncher)
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Add by Voice", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(thickness = 1.dp)
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}

private fun startVoiceInput(context: Context, launcher: androidx.activity.result.ActivityResultLauncher<Intent>) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        // intent for starting voice recognition
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a product name")
    }
    // launch voice recognition if supported
    try {
        launcher.launch(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Voice input not supported", Toast.LENGTH_SHORT).show()
    }
}
