package com.sagirov.ilovedog.Databases

import android.content.Context
import androidx.room.*
import com.sagirov.ilovedog.MapConverter
import com.sagirov.ilovedog.DAOs.DocumentDAO
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Database(entities = [DocumentsEntity::class], version = 1, exportSchema = true/*,autoMigrations = [AutoMigration(from = 4, to = 5)]*/)
@TypeConverters(MapConverter::class)
@Module// свой вклад в граф объектов внедрения зависимостей.
@InstallIn(SingletonComponent::class) //говорит, что активности этой зависимость должны быть активными в течении жизни приложения
abstract class DocumentDatabase: RoomDatabase() {
    abstract fun getDao(): DocumentDAO

    companion object {
        // Singleton предотвращает одновременное открытие нескольких экземпляров базы данных //
        @Volatile
        private var INSTANCE: DocumentDatabase? = null
        // если ЭКЗЕМПЛЯР != null, то верните его,
        // если равен null, то создайте базу данных
        @Provides
        @Singleton
        fun getDb(@ApplicationContext ctx: Context, scope: CoroutineScope): DocumentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(ctx, DocumentDatabase::class.java, "docs")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}