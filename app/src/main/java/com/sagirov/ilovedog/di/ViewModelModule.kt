package com.sagirov.ilovedog.di

import com.sagirov.ilovedog.domain.repository.DocumentRepository
import com.sagirov.ilovedog.domain.usecase.deleteDocumentUseCase
import com.sagirov.ilovedog.domain.usecase.getAllDocumentsUseCase
import com.sagirov.ilovedog.domain.usecase.insertDocumentUseCase
import com.sagirov.ilovedog.domain.usecase.updateDocumentUseCase
import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository
import com.sagirov.ilovedog.Activities.MainActivity.domain.usecase.*
import com.sagirov.ilovedog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.domain.usecase.getAllDogsUseCase
import com.sagirov.ilovedog.domain.usecase.getDogByIdUseCase
import com.sagirov.ilovedog.domain.repository.ReminderRepository
import com.sagirov.ilovedog.domain.usecase.deleteReminderUseCase
import com.sagirov.ilovedog.domain.usecase.getAllRemindersUseCase
import com.sagirov.ilovedog.domain.usecase.insertReminderUseCase
import com.sagirov.ilovedog.domain.repository.VaccinationRepository
import com.sagirov.ilovedog.domain.usecase.deleteVaccinationUseCase
import com.sagirov.ilovedog.domain.usecase.getAllVaccinationsUseCase
import com.sagirov.ilovedog.domain.usecase.insertVaccinationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {
    @Provides
    fun provideGetAllDogs(repo: DogsEncyclopediaRepository): getAllDogsUseCase =
        getAllDogsUseCase(repo)

    @Provides
    fun provideGetDogById(repo: DogsEncyclopediaRepository): getDogByIdUseCase =
        getDogByIdUseCase(repo)

    @Provides
    fun provideGetAllReminders(repo: ReminderRepository): getAllRemindersUseCase =
        getAllRemindersUseCase(repo)

    @Provides
    fun provideDeleteReminders(repo: ReminderRepository): deleteReminderUseCase =
        deleteReminderUseCase(repo)

    @Provides
    fun provideInsertReminder(repo: ReminderRepository): insertReminderUseCase =
        insertReminderUseCase(repo)

    @Provides
    fun provideGetAllVaccinations(repo: VaccinationRepository): getAllVaccinationsUseCase =
        getAllVaccinationsUseCase(repo)

    @Provides
    fun provideInsertVaccination(repo: VaccinationRepository): insertVaccinationUseCase =
        insertVaccinationUseCase(repo)

    @Provides
    fun provideDeleteVaccination(repo: VaccinationRepository): deleteVaccinationUseCase =
        deleteVaccinationUseCase(repo)

    @Provides
    fun provideGetAllDocuments(repo: DocumentRepository): getAllDocumentsUseCase =
        getAllDocumentsUseCase(repo)

    @Provides
    fun provideInsertDocument(repo: DocumentRepository): insertDocumentUseCase =
        insertDocumentUseCase(repo)

    @Provides
    fun provideUpdateDocument(repo: DocumentRepository): updateDocumentUseCase =
        updateDocumentUseCase(repo)

    @Provides
    fun provideDeleteDocument(repo: DocumentRepository): deleteDocumentUseCase =
        deleteDocumentUseCase(repo)

    @Provides
    fun provideGetAllDogProfiles(repo: DogsInfoRepository): getAllProfilesUseCase =
        getAllProfilesUseCase(repo)

    @Provides
    fun provideDeleteDogProfile(repo: DogsInfoRepository): deleteDogProfileUseCase =
        deleteDogProfileUseCase(repo)

    @Provides
    fun provideInsertDogProfile(repo: DogsInfoRepository): insertDogProfileUseCase =
        insertDogProfileUseCase(repo)

    @Provides
    fun provideUpdateDogProfile(repo: DogsInfoRepository): updateDogProfileUseCase =
        updateDogProfileUseCase(repo)

    @Provides
    fun provideUpdateDogsDate(repo: DogsInfoRepository): updateDogsDateUseCase =
        updateDogsDateUseCase(repo)

    @Provides
    fun provideUpdateDogsTime(repo: DogsInfoRepository): updateDogsTimeUseCase =
        updateDogsTimeUseCase(repo)

}