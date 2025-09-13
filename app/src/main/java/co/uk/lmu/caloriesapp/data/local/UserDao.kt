package co.uk.lmu.caloriesapp.data.local

import androidx.room.*

@Dao
interface UserDao {
    // insert user record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    // get user record, limited to 1
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): User?
    // delete user records
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}