package com.sagirov.ilovedog.DogsKnowledgeBaseDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [DogsKnowledgeBaseEntity::class, DogsKnowledgeBaseSubEntity::class], version = 1, exportSchema = true)
abstract class DogsKnowledgeBaseDatabase: RoomDatabase() {
    abstract fun getDao(): DogsKnowledgeBaseDAO

    companion object {
        @Volatile
        private var INSTANCE: DogsKnowledgeBaseDatabase? = null

        fun getDb(ctx: Context, scope: CoroutineScope): DogsKnowledgeBaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(ctx.applicationContext, DogsKnowledgeBaseDatabase::class.java, "dogsKnowsledge")
                    .createFromAsset("tset.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}