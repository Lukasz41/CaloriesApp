package co.uk.lmu.caloriesapp.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.uk.lmu.caloriesapp.data.local.MealDao

// factory class to create an instance of dashboardviewmodel with a mealdao dependency
// required because viewmodelprovider does not support viewmodels with non-empty constructors by default


class DashboardViewModelFactory(
    private val dao: MealDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // return dashboard viewmodel instance if requested
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(dao) as T
        }
        // throw if an unsupported viewmodel class is requested
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
