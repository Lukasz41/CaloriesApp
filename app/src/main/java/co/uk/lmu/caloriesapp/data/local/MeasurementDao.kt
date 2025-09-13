package co.uk.lmu.caloriesapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    // get all measurements ordered by date
    @Query("SELECT * FROM measurements ORDER BY date ASC")
    fun getAll(): Flow<List<Measurement>>
    // insert a new measurement
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measurement: Measurement)
    // delete all measurements from the table
    @Query("DELETE FROM measurements")
    suspend fun clearAll()
}