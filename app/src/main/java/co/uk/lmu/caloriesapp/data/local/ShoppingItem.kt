package co.uk.lmu.caloriesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// entity representing a shopping list item
@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)