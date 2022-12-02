package com.sagirov.ilovedog.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sagirov.ilovedog.data.dao.DogsBreedEncyclopediaDAO
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity

@Database(
    entities = [DogsBreedEncyclopediaEntity::class],
    version = 1,
    exportSchema = true,/*autoMigrations = [AutoMigration(from = 4, to = 5)]*/
)
//@Module// свой вклад в граф объектов внедрения зависимостей.
//@InstallIn(SingletonComponent::class) //говорит, что активности этой зависимость должны быть активными в течении жизни приложения
abstract class DogsBreedEncyclopediaDatabase: RoomDatabase() {
    abstract fun getDao(): DogsBreedEncyclopediaDAO


//    companion object {
//        // Singleton предотвращает одновременное открытие нескольких экземпляров базы данных //
//        @Volatile
//        private var INSTANCE: DogsBreedEncyclopediaDatabase? = null
//        // если ЭКЗЕМПЛЯР != null, то верните его,
//        // если равен null, то создайте базу данных
//        @Provides
//        @Singleton
//        fun getDb(@ApplicationContext ctx: Context, scope: CoroutineScope): DogsBreedEncyclopediaDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(ctx, DogsBreedEncyclopediaDatabase::class.java, "dogsBreed")
//                    .createFromAsset("wtfdb.db")
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}