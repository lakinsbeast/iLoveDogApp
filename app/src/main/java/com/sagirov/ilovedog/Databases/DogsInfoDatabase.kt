package com.sagirov.ilovedog.Databases

import android.content.Context
import androidx.room.*
import com.sagirov.ilovedog.DAOs.DogsInfoDAO
import com.sagirov.ilovedog.DataConverter
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.VaccinationsEntity
import com.sagirov.ilovedog.MapConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Database(entities = [DogsInfoEntity::class], version = 5, exportSchema = true/*,autoMigrations = [AutoMigration(from = 4, to = 5)]*/)
@TypeConverters(DataConverter::class)
@Module// свой вклад в граф объектов внедрения зависимостей.
@InstallIn(SingletonComponent::class) //говорит, что активности этой зависимость должны быть активными в течении жизни приложения
abstract class DogsInfoDatabase: RoomDatabase() {
    abstract fun getDao(): DogsInfoDAO


    companion object {
        // Singleton предотвращает одновременное открытие нескольких экземпляров базы данных //
        @Volatile
        private var INSTANCE: DogsInfoDatabase? = null
        // если ЭКЗЕМПЛЯР != null, то верните его,
        // если равен null, то создайте базу данных
        @Provides
        @Singleton
        fun getDb(@ApplicationContext ctx: Context, scope: CoroutineScope): DogsInfoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(ctx, DogsInfoDatabase::class.java, "dogsInfo")
                    .createFromAsset("wtfdb.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}