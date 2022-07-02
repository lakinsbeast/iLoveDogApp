package com.sagirov.ilovedog.DogsKnowledgeBaseDatabase

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DogsKnowledgeApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val db by lazy { DogsKnowledgeBaseDatabase.getDb(this, applicationScope) }
    val dbKnow by lazy { DogsKnowledgeBaseDatabase.getDb(this, applicationScope) }
    val repo by lazy { DogsKnowledgeBaseRepository(db.getDao()) }
    val repoKnow by lazy { DogsKnowledgeBaseRepository(dbKnow.getDao()) }
}