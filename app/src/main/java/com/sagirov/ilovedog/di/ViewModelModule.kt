package com.sagirov.ilovedog.di

import com.sagirov.ilovedog.Screens.DetailedDog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.Screens.DetailedDog.domain.usecase.DogsEncyclopedia.getAllDogs
import com.sagirov.ilovedog.Screens.DetailedDog.domain.usecase.DogsEncyclopedia.getDogById
import com.sagirov.ilovedog.Screens.Reminder.domain.repository.ReminderRepository
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.deleteReminder
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.getAllReminders
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.insertReminder
import com.sagirov.ilovedog.Screens.Vaccinations.domain.repo.VaccinationRepository
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.deleteVaccination
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.getAllVaccinations
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.insertVaccination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {
    @Provides
    fun provideGetAllDogs(repo: DogsEncyclopediaRepository): getAllDogs = getAllDogs(repo)

    @Provides
    fun provideGetDogById(repo: DogsEncyclopediaRepository): getDogById = getDogById(repo)

    @Provides
    fun provideGetAllReminders(repo: ReminderRepository): getAllReminders = getAllReminders(repo)

    @Provides
    fun provideDeleteReminders(repo: ReminderRepository): deleteReminder = deleteReminder(repo)

    @Provides
    fun provideInsertReminder(repo: ReminderRepository): insertReminder = insertReminder(repo)

    @Provides
    fun provideGetAllVaccinations(repo: VaccinationRepository): getAllVaccinations =
        getAllVaccinations(repo)

    @Provides
    fun provideInsertVaccination(repo: VaccinationRepository): insertVaccination =
        insertVaccination(repo)

    @Provides
    fun provideDeleteVaccination(repo: VaccinationRepository): deleteVaccination =
        deleteVaccination(repo)
}