package com.sagirov.ilovedog.Activities.Documents.domain.usecase

import com.sagirov.ilovedog.Activities.Documents.domain.repository.DocumentRepository

class deleteDocumentUseCase(
    private val repo: DocumentRepository
) {
    suspend operator fun invoke(id: Int) = repo.delete(id)
}