package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.DocumentsEntity
import com.sagirov.ilovedog.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow

class getAllDocumentsUseCase(
    private val repo: DocumentRepository
) {
    operator fun invoke(): Flow<MutableList<DocumentsEntity>> = repo.getAllDocuments()
}