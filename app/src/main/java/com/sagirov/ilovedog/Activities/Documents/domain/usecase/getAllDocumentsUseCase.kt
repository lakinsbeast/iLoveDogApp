package com.sagirov.ilovedog.Activities.Documents.domain.usecase

import com.sagirov.ilovedog.Activities.Documents.domain.model.DocumentsEntity
import com.sagirov.ilovedog.Activities.Documents.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow

class getAllDocumentsUseCase(
    private val repo: DocumentRepository
) {
    operator fun invoke(): Flow<MutableList<DocumentsEntity>> = repo.getAllDocuments()
}