package com.sagirov.ilovedog.di

import android.content.Context
import androidx.room.Room
import com.sagirov.ilovedog.DAOs.DocumentDAO
import com.sagirov.ilovedog.DAOs.DogsInfoDAO
import com.sagirov.ilovedog.Databases.DocumentDatabase
import com.sagirov.ilovedog.Databases.DogsInfoDatabase
import com.sagirov.ilovedog.Screens.DetailedDog.dao.DogsBreedEncyclopediaDAO
import com.sagirov.ilovedog.Screens.DetailedDog.database.DogsBreedEncyclopediaDatabase
import com.sagirov.ilovedog.Screens.Reminder.dao.ReminderDAO
import com.sagirov.ilovedog.Screens.Reminder.database.ReminderDatabase
import com.sagirov.ilovedog.Screens.Vaccinations.dao.VaccinationDAO
import com.sagirov.ilovedog.Screens.Vaccinations.database.VaccinationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModules {

    @Provides
    fun provideReminderDAO(appDatabase: ReminderDatabase): ReminderDAO {
        return appDatabase.getDao()
    }
    @Provides
    fun provideReminderDatabase(@ApplicationContext ctx: Context): ReminderDatabase {
        return Room.databaseBuilder(ctx, ReminderDatabase::class.java, "reminders")
            .build()
    }

    @Provides
    fun provideDogsBreedEncyclopediaDAO(appDatabase: DogsBreedEncyclopediaDatabase): DogsBreedEncyclopediaDAO {
        return appDatabase.getDao()
    }
    @Provides
    fun provideDogsBreedEncyclopediaDatabase(@ApplicationContext ctx: Context): DogsBreedEncyclopediaDatabase {
        return Room.databaseBuilder(ctx, DogsBreedEncyclopediaDatabase::class.java, "dogsBreed")
            .createFromAsset("wtfdb.db")
            .build()
    }

    @Provides
    fun provideVaccinationDAO(appDatabase: VaccinationDatabase): VaccinationDAO {
        return appDatabase.getDao()
    }
    @Provides
    fun provideVaccinationDatabase(@ApplicationContext ctx: Context): VaccinationDatabase {
        return Room.databaseBuilder(ctx, VaccinationDatabase::class.java, "vaccine")
            .build()
    }

    @Provides
    fun provideDocumentDAO(appDatabase: DocumentDatabase): DocumentDAO {
        return appDatabase.getDao()
    }
    @Provides
    fun provideDocumentDatabase(@ApplicationContext ctx: Context): DocumentDatabase {
        return Room.databaseBuilder(ctx, DocumentDatabase::class.java, "docs")
            .build()
    }

    @Provides
    fun provideDogsInfoDAO(appDatabase: DogsInfoDatabase): DogsInfoDAO {
        return appDatabase.getDao()
    }
    @Provides
    fun provideDogsInfoDatabase(@ApplicationContext ctx: Context): DogsInfoDatabase {
        return Room.databaseBuilder(ctx, DogsInfoDatabase::class.java, "dogsInfo")
            .createFromAsset("wtfdb.db")
            .build()
    }
}