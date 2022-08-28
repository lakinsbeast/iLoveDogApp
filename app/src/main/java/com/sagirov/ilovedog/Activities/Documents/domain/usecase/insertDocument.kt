package com.sagirov.ilovedog.Activities.Documents.domain.usecase

import com.sagirov.ilovedog.Activities.Documents.domain.repository.DocumentRepository
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity

class insertDocument(
    private val repo: DocumentRepository
) {
    suspend operator fun invoke(doc: DocumentsEntity) = repo.insert(doc)
}