package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import android.app.Application
import com.sagirov.ilovedog.DogsKnowledgeBaseDatabase.DogsKnowledgeBaseDatabase
import com.sagirov.ilovedog.DogsKnowledgeBaseDatabase.DogsKnowledgeBaseRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class DogsApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val db by lazy { DogsBreedEncyclopediaDatabase.getDb(this, applicationScope) }
    val dbKnow by lazy { DogsKnowledgeBaseDatabase.getDb(this, applicationScope) }
    val repo by lazy { DogsBreedEncyclopediaRepository(db.getDao()) }
    val repoKnow by lazy { DogsKnowledgeBaseRepository(dbKnow.getDao()) }
}