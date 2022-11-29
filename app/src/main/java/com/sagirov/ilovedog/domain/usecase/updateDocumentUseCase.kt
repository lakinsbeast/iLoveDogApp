package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.DocumentRepository

class updateDocumentUseCase(
    private val repo: DocumentRepository
) {
    suspend operator fun invoke(id: Int, doc: Map<String, String>) = repo.update(id, doc)
}