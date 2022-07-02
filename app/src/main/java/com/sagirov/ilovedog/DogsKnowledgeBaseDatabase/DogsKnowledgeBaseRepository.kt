package com.sagirov.ilovedog.DogsKnowledgeBaseDatabase

import androidx.lifecycle.LiveData
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaDAO
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity

class DogsKnowledgeBaseRepository(private val dogsDao: DogsKnowledgeBaseDAO) {
    fun getAllDogs(): LiveData<MutableList<DogsKnowledgeBaseEntity>> = dogsDao.getEnt()
}