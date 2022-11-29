package com.sagirov.ilovedog.di

import com.sagirov.ilovedog.data.database.DocumentDatabase
import com.sagirov.ilovedog.data.repoImpl.DocumentRepositoryImpl
import com.sagirov.ilovedog.domain.repository.DocumentRepository
import com.sagirov.ilovedog.Activities.MainActivity.data.database.DogsInfoDatabase
import com.sagirov.ilovedog.Activities.MainActivity.data.repoImpl.DogInfoRepositoryImpl
import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository
import com.sagirov.ilovedog.data.database.DogsBreedEncyclopediaDatabase
import com.sagirov.ilovedog.data.repoImpl.DogsEncyclopediaRepositoryImpl
import com.sagirov.ilovedog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.data.database.ReminderDatabase
import com.sagirov.ilovedog.data.repoImpl.ReminderRepositoryImpl
import com.sagirov.ilovedog.domain.repository.ReminderRepository
import com.sagirov.ilovedog.data.database.VaccinationDatabase
import com.sagirov.ilovedog.data.repoImpl.VaccinationRepositoryImpl
import com.sagirov.ilovedog.domain.repository.VaccinationRepository
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
    fun provideDocumentRepository(db: DocumentDatabase): DocumentRepository {
        return DocumentRepositoryImpl(db.getDao())
    }

    @Singleton
    @Provides
    fun provideDogsInfoRepository(db: DogsInfoDatabase): DogsInfoRepository {
        return DogInfoRepositoryImpl(db.getDao())
    }
}