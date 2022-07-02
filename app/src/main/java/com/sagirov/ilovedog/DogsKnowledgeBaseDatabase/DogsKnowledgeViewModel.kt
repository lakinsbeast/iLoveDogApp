package com.sagirov.ilovedog.DogsKnowledgeBaseDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModel

class DogsKnowledgeViewModel(repo: DogsKnowledgeBaseRepository): ViewModel() {
    val allKnowledge: LiveData<MutableList<DogsKnowledgeBaseEntity>> = repo.getAllDogs()

}

class DogsKnowledgeBaseViewModelFactory(private val repo: DogsKnowledgeBaseRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DogsKnowledgeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DogsKnowledgeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}