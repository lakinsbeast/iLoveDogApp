package com.sagirov.ilovedog.di

import com.sagirov.ilovedog.Activities.Documents.dao.DocumentDAO
import com.sagirov.ilovedog.Activities.Documents.repo.DocumentRepository
import com.sagirov.ilovedog.Activities.MainActivity.dao.DogsInfoDAO
import com.sagirov.ilovedog.Activities.MainActivity.repo.DogsInfoRepository
import com.sagirov.ilovedog.Screens.DetailedDog.data.database.DogsBreedEncyclopediaDatabase
import com.sagirov.ilovedog.Screens.DetailedDog.data.repositoryImpl.DogsEncyclopediaRepositoryImpl
import com.sagirov.ilovedog.Screens.DetailedDog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.Screens.Reminder.data.database.ReminderDatabase
import com.sagirov.ilovedog.Screens.Reminder.data.repoImpl.ReminderRepositoryImpl
import com.sagirov.ilovedog.Screens.Reminder.domain.repository.ReminderRepository
import com.sagirov.ilovedog.Screens.Vaccinations.data.database.VaccinationDatabase
import com.sagirov.ilovedog.Screens.Vaccinations.data.repoimpl.VaccinationRepositoryImpl
import com.sagirov.ilovedog.Screens.Vaccinations.domain.repo.VaccinationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideDogEncyclopediaRepository(db: DogsBreedEncyclopediaDatabase): DogsEncyclopediaRepository {
        return DogsEncyclopediaRepositoryImpl(db.getDao())
    }

    @Singleton
    @Provides
    fun provideReminderRepository(db: ReminderDatabase): ReminderRepository {
        return ReminderRepositoryImpl(db.getDao())
    }

    @Singleton
    @Provides
    fun provideVaccinationRepository(db: VaccinationDatabase): VaccinationRepository {
        return VaccinationRepositoryImpl(db.getDao())
    }

    @Singleton
    @Provides
    fun provideDocumentRepository(dao: DocumentDAO) = DocumentRepository(dao)

    @Singleton
    @Provides
    fun provideDogsInfo(dao: DogsInfoDAO) = DogsInfoRepository(dao)
}