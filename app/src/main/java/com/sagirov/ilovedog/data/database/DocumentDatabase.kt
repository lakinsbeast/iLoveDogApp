package com.sagirov.ilovedog.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sagirov.ilovedog.data.dao.DocumentDAO
import com.sagirov.ilovedog.domain.model.DocumentsEntity
import com.sagirov.ilovedog.domain.utils.MapStringToStringConverter

@Database(entities = [DocumentsEntity::class], version = 1, exportSchema = true/*,autoMigrations = [AutoMigration(from = 4, to = 5)]*/)
@TypeConverters(MapStringToStringConverter::class)
//@Module// свой вклад в граф объектов внедрения зависимостей.
//@InstallIn(SingletonComponent::class) //говорит, что активности этой зависимость должны быть активными в течении жизни приложения
abstract class DocumentDatabase: RoomDatabase() {
    abstract fun getDao(): DocumentDAO

//    companion object {
//        // Singleton предотвращает одновременное открытие нескольких экземпляров базы данных //
//        @Volatile
//        private var INSTANCE: DocumentDatabase? = null
//        // если ЭКЗЕМПЛЯР != null, то верните его,
//        // если равен null, то создайте базу данных
//        @Provides
//        @Singleton
//        fun getDb(@ApplicationContext ctx: Context, scope: CoroutineScope): DocumentDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(ctx, DocumentDatabase::class.java, "docs")
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}