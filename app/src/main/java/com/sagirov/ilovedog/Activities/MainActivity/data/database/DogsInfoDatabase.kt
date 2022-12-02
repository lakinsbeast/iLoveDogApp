package com.sagirov.ilovedog.Activities.MainActivity.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sagirov.ilovedog.Activities.MainActivity.data.dao.DogsInfoDAO
import com.sagirov.ilovedog.Activities.MainActivity.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.domain.utils.DataConverter


@Database(entities = [DogsInfoEntity::class], version = 5, exportSchema = true/*,autoMigrations = [AutoMigration(from = 4, to = 5)]*/)
@TypeConverters(DataConverter::class)
//@Module// свой вклад в граф объектов внедрения зависимостей.
//@InstallIn(SingletonComponent::class) //говорит, что активности этой зависимость должны быть активными в течении жизни приложения
abstract class DogsInfoDatabase: RoomDatabase() {
    abstract fun getDao(): DogsInfoDAO


//    companion object {
//        // Singleton предотвращает одновременное открытие нескольких экземпляров базы данных //
//        @Volatile
//        private var INSTANCE: DogsInfoDatabase? = null
//        // если ЭКЗЕМПЛЯР != null, то верните его,
//        // если равен null, то создайте базу данных
//        @Provides
//        @Singleton
//        fun getDb(@ApplicationContext ctx: Context, scope: CoroutineScope): DogsInfoDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(ctx, DogsInfoDatabase::class.java, "dogsInfo")
//                    .createFromAsset("wtfdb.db")
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}