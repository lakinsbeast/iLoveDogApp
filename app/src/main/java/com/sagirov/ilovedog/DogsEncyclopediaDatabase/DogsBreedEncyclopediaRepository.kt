package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData

class DogsBreedEncyclopediaRepository(private val dogsDao: DogsBreedEncyclopediaDAO) {
    fun getAllDogs(): LiveData<MutableList<DogsBreedEncyclopediaEntity>> = dogsDao.getAll()
}