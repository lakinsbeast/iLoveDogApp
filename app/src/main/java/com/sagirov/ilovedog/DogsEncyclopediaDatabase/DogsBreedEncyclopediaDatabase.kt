package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sagirov.ilovedog.DataConverter
import com.sagirov.ilovedog.MapConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Database(entities = [DogsBreedEncyclopediaEntity::class, DogsInfoEntity::class, DocumentsEntity::class], version = 4, exportSchema = true, autoMigrations = [AutoMigration(from = 3, to = 4)])
@TypeConverters(DataConverter::class, MapConverter::class)
@Module// свой вклад в граф объектов внедрения зависимостей.
@InstallIn(SingletonComponent::class) //говорит, что активности этой зависимость должны быть активными в течении жизни приложения
abstract class DogsBreedEncyclopediaDatabase: RoomDatabase() {
    abstract fun getDao(): DogsBreedEncyclopediaDAO


    companion object {
        // Singleton предотвращает одновременное открытие нескольких экземпляров базы данных //
        @Volatile
        private var INSTANCE: DogsBreedEncyclopediaDatabase? = null
        // если ЭКЗЕМПЛЯР != null, то верните его,
        // если равен null, то создайте базу данных
        @Provides
        @Singleton
        fun getDb(@ApplicationContext ctx: Context, scope: CoroutineScope): DogsBreedEncyclopediaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(ctx, DogsBreedEncyclopediaDatabase::class.java, "dogs")
                    .createFromAsset("wtfdb.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}