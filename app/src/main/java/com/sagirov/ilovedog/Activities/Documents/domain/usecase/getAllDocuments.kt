package com.sagirov.ilovedog.Activities.Documents.domain.usecase

import com.sagirov.ilovedog.Activities.Documents.domain.repository.DocumentRepository
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import kotlinx.coroutines.flow.Flow

class getAllDocuments(
    private val repo: DocumentRepository
) {
    operator fun invoke(): Flow<MutableList<DocumentsEntity>> = repo.getAllDocuments()
}