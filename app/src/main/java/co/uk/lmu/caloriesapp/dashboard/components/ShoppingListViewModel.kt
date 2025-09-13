package co.uk.lmu.caloriesapp.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.uk.lmu.caloriesapp.data.local.AppDatabase
import co.uk.lmu.caloriesapp.data.local.ShoppingItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).shoppingItemDao()
    // observe the shopping list and expose only product names
    val shoppingList = dao.getAllItems()
        .map { it.map { item -> item.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // add a product to the list
    fun addProduct(product: String) {
        viewModelScope.launch {
            dao.insert(ShoppingItem(name = product))
        }
    }
    // remove a specific product from the list
    fun removeProduct(product: String) {
        viewModelScope.launch {
            val currentItems = dao.getAllItems().stateIn(viewModelScope).value
            currentItems.find { it.name == product }?.let {
                dao.delete(it)
            }
        }
    }
    // clear the entire shopping list
    fun clearAllProducts() {
        viewModelScope.launch {
            dao.clearAll()
        }
    }
}