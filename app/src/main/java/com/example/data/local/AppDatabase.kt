package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "saved_locations")
data class SavedLocation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isUserCurrent: Boolean = false
)

@Dao
interface LocationDao {
    @Query("SELECT * FROM saved_locations ORDER BY isUserCurrent DESC, id ASC")
    fun getAllLocations(): Flow<List<SavedLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: SavedLocation)

    @Delete
    suspend fun deleteLocation(location: SavedLocation)

    @Query("SELECT * FROM saved_locations WHERE isUserCurrent = 1 LIMIT 1")
    suspend fun getCurrentUserLocation(): SavedLocation?
}

@Database(entities = [SavedLocation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}
