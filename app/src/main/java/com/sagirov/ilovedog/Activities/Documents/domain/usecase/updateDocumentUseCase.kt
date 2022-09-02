package com.sagirov.ilovedog.Activities.Documents.domain.usecase

import com.sagirov.ilovedog.Activities.Documents.domain.repository.DocumentRepository

class updateDocumentUseCase(
    private val repo: DocumentRepository
) {
    suspend operator fun invoke(id: Int, doc: Map<String, String>) = repo.update(id, doc)
}