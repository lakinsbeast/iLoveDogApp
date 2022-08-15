package com.sagirov.ilovedog.Repos

import androidx.lifecycle.LiveData
import com.sagirov.ilovedog.DAOs.DogsBreedEncyclopediaDAO
import com.sagirov.ilovedog.DAOs.DogsInfoDAO
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogsBreedEncyclopediaRepository @Inject constructor(private val dogsDao: DogsBreedEncyclopediaDAO) {
    fun getAllDogs(): LiveData<MutableList<DogsBreedEncyclopediaEntity>> = dogsDao.getAll()

}