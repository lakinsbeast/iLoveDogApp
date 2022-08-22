package com.sagirov.ilovedog.Screens.DetailedDog.repos

import androidx.lifecycle.LiveData
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.Screens.DetailedDog.dao.DogsBreedEncyclopediaDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogsBreedEncyclopediaRepository @Inject constructor(private val dogsDao: DogsBreedEncyclopediaDAO) {
    fun getAllDogs(): LiveData<MutableList<DogsBreedEncyclopediaEntity>> = dogsDao.getAll()
}