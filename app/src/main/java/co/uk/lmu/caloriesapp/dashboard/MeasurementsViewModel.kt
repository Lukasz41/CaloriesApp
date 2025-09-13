package co.uk.lmu.caloriesapp.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.uk.lmu.caloriesapp.data.local.AppDatabase
import co.uk.lmu.caloriesapp.data.local.Measurement
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeasurementsViewModel(application: Application) : AndroidViewModel(application) {
    // access the measurement dao from the app database
    private val dao = AppDatabase.getInstance(application).measurementDao()
    // collect all measurements as state
    val measurements = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // insert a new measurement into the database
    fun addMeasurement(date: String, weight: Double) {
        viewModelScope.launch {
            dao.insert(Measurement(date = date, weight = weight))
        }
    }
}

