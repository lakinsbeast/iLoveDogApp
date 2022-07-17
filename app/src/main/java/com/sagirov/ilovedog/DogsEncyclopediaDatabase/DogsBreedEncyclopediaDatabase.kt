package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sagirov.ilovedog.DataConverter
import com.sagirov.ilovedog.MapConverter
import kotlinx.coroutines.CoroutineScope


@Database(entities = [DogsBreedEncyclopediaEntity::class, DogsInfoEntity::class, DocumentsEntity::class], version = 3, exportSchema = true, autoMigrations = [AutoMigration(from = 2, to = 3)])
@TypeConverters(DataConverter::class, MapConverter::class)
abstract class DogsBreedEncyclopediaDatabase: RoomDatabase() {
    abstract fun getDao(): DogsBreedEncyclopediaDAO


    companion object {
        // Singleton предотвращает одновременное открытие нескольких экземпляров базы данных //
        @Volatile
        private var INSTANCE: DogsBreedEncyclopediaDatabase? = null
        // если ЭКЗЕМПЛЯР != null, то верните его,
        // если равен null, то создайте базу данных
        fun getDb(ctx: Context, scope: CoroutineScope): DogsBreedEncyclopediaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(ctx.applicationContext, DogsBreedEncyclopediaDatabase::class.java, "dogs")
                    .createFromAsset("wtfdb.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}