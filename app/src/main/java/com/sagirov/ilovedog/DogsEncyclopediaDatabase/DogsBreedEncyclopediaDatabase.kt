package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope


@Database(entities = [DogsBreedEncyclopediaEntity::class], version = 1, exportSchema = true)
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