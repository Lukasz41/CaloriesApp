package co.uk.lmu.caloriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.uk.lmu.caloriesapp.dashboard.DashboardScreen
import co.uk.lmu.caloriesapp.dashboard.DashboardViewModel
import co.uk.lmu.caloriesapp.dashboard.DashboardViewModelFactory
import co.uk.lmu.caloriesapp.dashboard.MeasurementsScreen
import co.uk.lmu.caloriesapp.dashboard.MeasurementsViewModel
import co.uk.lmu.caloriesapp.registration.RegistrationViewModel
import co.uk.lmu.caloriesapp.registration.screens.ActivityLevelScreen
import co.uk.lmu.caloriesapp.registration.screens.AgeScreen
import co.uk.lmu.caloriesapp.registration.screens.CreateAccountScreen
import co.uk.lmu.caloriesapp.registration.screens.GenderScreen
import co.uk.lmu.caloriesapp.registration.screens.GoalWeightScreen
import co.uk.lmu.caloriesapp.registration.screens.HeightScreen
import co.uk.lmu.caloriesapp.registration.screens.SummaryScreen
import co.uk.lmu.caloriesapp.registration.screens.WeightLossRateScreen
import co.uk.lmu.caloriesapp.registration.screens.WeightScreen
import co.uk.lmu.caloriesapp.splash.SplashScreen
import co.uk.lmu.caloriesapp.dashboard.SettingsScreen
import co.uk.lmu.caloriesapp.dashboard.ShoppingListViewModel
import co.uk.lmu.caloriesapp.dashboard.components.AppResetScreen
import co.uk.lmu.caloriesapp.dashboard.components.ShoppingListScreen
import co.uk.lmu.caloriesapp.dashboard.components.ExitScreen
import co.uk.lmu.caloriesapp.dashboard.components.PrivacyPolicyScreen
import co.uk.lmu.caloriesapp.data.local.AppDatabase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dao = AppDatabase.getInstance(applicationContext).mealDao()
            val navController = rememberNavController()
            val registrationViewModel: RegistrationViewModel = viewModel(
                factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModelFactory(dao)
            )
            val shoppingListViewModel = viewModel<ShoppingListViewModel>(
                factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )
            val measurementsViewModel = viewModel<MeasurementsViewModel>(
                factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )


            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") {
                    SplashScreen(navController, registrationViewModel)
                }

                // registration steps
                composable("create_account") {
                    CreateAccountScreen(navController)
                }
                composable("gender") {
                    GenderScreen(navController, registrationViewModel)
                }
                composable("age") {
                    AgeScreen(navController, registrationViewModel)
                }
                composable("weight") {
                    WeightScreen(navController, registrationViewModel)
                }
                composable("height") {
                    HeightScreen(navController, registrationViewModel)
                }
                composable("activity_level") {
                    ActivityLevelScreen(navController, registrationViewModel)
                }
                composable("goal_weight") {
                    GoalWeightScreen(navController, registrationViewModel)
                }
                composable("weight_loss_rate") {
                    WeightLossRateScreen(navController, registrationViewModel)
                }
                composable("summary") {
                    SummaryScreen(navController, registrationViewModel)
                }

                // dashboard
                composable("dashboard") {
                    DashboardScreen(navController, dashboardViewModel)
                }
                composable("measurements") {
                    MeasurementsScreen(navController, viewModel = measurementsViewModel)
                }
                composable("settings") {
                    SettingsScreen(navController)
                }
                composable("shopping_list") {
                    ShoppingListScreen(navController, viewModel = shoppingListViewModel)
                }
                composable("privacy_policy") {
                    PrivacyPolicyScreen(navController)
                }
                composable("app_reset") {
                    AppResetScreen(navController)
                }
                composable("exit") {
                    ExitScreen()
                }

            }
        }
    }
}










