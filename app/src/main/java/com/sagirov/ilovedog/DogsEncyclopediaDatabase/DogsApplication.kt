package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DogsApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val db by lazy { DogsBreedEncyclopediaDatabase.getDb(this, applicationScope) }
    val repo by lazy { DogsBreedEncyclopediaRepository(db.getDao()) }
}