package com.sagirov.ilovedog

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class DogsApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

//    private val DocumentAppDatabase by lazy { DocumentDatabase.getDb(this, applicationScope) }
//    val DocumentAppRepo by lazy { DocumentRepository(DocumentAppDatabase.getDao()) }
//
//    private val DogsBreedEncyclopediaAppDatabase by lazy {
//        DogsBreedEncyclopediaDatabase.getDb(
//            this,
//            applicationScope
//        )
//    }
//    val DogsBreedEncyclopediaAppRepo by lazy {
//        DogsBreedEncyclopediaRepository(
//            DogsBreedEncyclopediaAppDatabase.getDao()
//        )
//    }
//
//    private val DogsInfoAppDatabase by lazy { DogsInfoDatabase.getDb(this, applicationScope) }
//    val DogsInfoRepo by lazy { DogsInfoRepository(DogsInfoAppDatabase.getDao()) }
//
//    private val VaccinationAppDatabase by lazy { VaccinationDatabase.getDb(this, applicationScope) }
//    val VaccinationAppRepo by lazy { VaccinationRepository(VaccinationAppDatabase.getDao()) }
//
//    private val ReminderAppDatabase by lazy { ReminderDatabase.getDb(this, applicationScope)}
//    val ReminderAppRepo by lazy { ReminderRepository(ReminderAppDatabase.getDao()) }
}