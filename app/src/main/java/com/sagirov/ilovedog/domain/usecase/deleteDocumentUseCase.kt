package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.DocumentRepository

class deleteDocumentUseCase(
    private val repo: DocumentRepository
) {
    suspend operator fun invoke(id: Int) = repo.delete(id)
}