package co.uk.lmu.caloriesapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    // get all shopping items as a flow
    @Query("SELECT * FROM shopping_items")
    fun getAllItems(): Flow<List<ShoppingItem>>
    // insert a new shopping item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItem)
    // delete a specific shopping item
    @Delete
    suspend fun delete(item: ShoppingItem)
    // delete all shopping items
    @Query("DELETE FROM shopping_items")
    suspend fun clearAll()
}