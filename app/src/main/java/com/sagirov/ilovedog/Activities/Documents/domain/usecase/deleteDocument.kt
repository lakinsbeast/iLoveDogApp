package com.sagirov.ilovedog.Activities.Documents.domain.usecase

import com.sagirov.ilovedog.Activities.Documents.domain.repository.DocumentRepository

class deleteDocument(
    private val repo: DocumentRepository
) {
    suspend operator fun invoke(id: Int) = repo.delete(id)
}