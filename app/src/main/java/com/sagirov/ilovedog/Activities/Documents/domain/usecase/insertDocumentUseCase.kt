package com.sagirov.ilovedog.Activities.Documents.domain.usecase

import com.sagirov.ilovedog.Activities.Documents.domain.model.DocumentsEntity
import com.sagirov.ilovedog.Activities.Documents.domain.repository.DocumentRepository

class insertDocumentUseCase(
    private val repo: DocumentRepository
) {
    suspend operator fun invoke(doc: DocumentsEntity) = repo.insert(doc)
}