package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.DocumentsEntity
import com.sagirov.ilovedog.domain.repository.DocumentRepository

class insertDocumentUseCase(
    private val repo: DocumentRepository
) {
    suspend operator fun invoke(doc: DocumentsEntity) = repo.insert(doc)
}