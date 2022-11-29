package com.sagirov.ilovedog.data.repoImpl

import com.sagirov.ilovedog.data.dao.DogsBreedEncyclopediaDAO
import com.sagirov.ilovedog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import kotlinx.coroutines.flow.Flow

class DogsEncyclopediaRepositoryImpl(private val dao: DogsBreedEncyclopediaDAO) :
    DogsEncyclopediaRepository {
    override fun getAllDogs(): Flow<MutableList<DogsBreedEncyclopediaEntity>> {
        return dao.getAll()
    }

    override suspend fun getDogById(id: Int): DogsBreedEncyclopediaEntity {
        return dao.getDogById(id = id)
    }


}