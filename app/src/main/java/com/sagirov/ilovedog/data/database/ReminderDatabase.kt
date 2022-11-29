package com.sagirov.ilovedog.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sagirov.ilovedog.domain.utils.MapStringToStringConverter
import com.sagirov.ilovedog.data.dao.ReminderDAO
import com.sagirov.ilovedog.domain.model.ReminderEntity

@Database(entities = [ReminderEntity::class], version = 1, exportSchema = true/*,autoMigrations = [AutoMigration(from = 4, to = 5)]*/)
@TypeConverters(MapStringToStringConverter::class)
//@Module// свой вклад в граф объектов внедрения зависимостей.
//@InstallIn(SingletonComponent::class) //говорит, что активности этой зависимость должны быть активными в течении жизни приложения
abstract class ReminderDatabase: RoomDatabase() {
    abstract fun getDao(): ReminderDAO

//    companion object {
//        // Singleton предотвращает одновременное открытие нескольких экземпляров базы данных //
//        @Volatile
//        private var INSTANCE: ReminderDatabase? = null
//        // если ЭКЗЕМПЛЯР != null, то верните его,
//        // если равен null, то создайте базу данных
//        @Provides
//        @Singleton
//        fun getDb(@ApplicationContext ctx: Context, scope: CoroutineScope): ReminderDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(ctx, ReminderDatabase::class.java, "reminders")
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}