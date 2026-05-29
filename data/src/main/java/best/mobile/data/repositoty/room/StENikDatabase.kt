package best.mobile.data.repositoty.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import best.mobile.data.entity.TextStENikEntity
import best.mobile.data.entity.VocabularyStENikEntity

@Database(
    entities = [TextStENikEntity::class, VocabularyStENikEntity::class],
    version = 1
)
abstract class StENikDatabase : RoomDatabase() {
    abstract val getDao: StENikDao
}

fun provideStENikDatabase(app: Application): StENikDatabase {
    return Room.databaseBuilder(
        app,
        StENikDatabase::class.java,
        "StENikDatabase"
    ).build()
}