package com.sagirov.ilovedog.Screens.DetailedDog.presentation

import androidx.lifecycle.ViewModel
import com.sagirov.ilovedog.Screens.DetailedDog.domain.usecase.DogsEncyclopedia.getAllDogs
import com.sagirov.ilovedog.Screens.DetailedDog.domain.usecase.DogsEncyclopedia.getDogById
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DogsEncyclopediaViewModel @Inject constructor(
    getAllDogs: getAllDogs,
    val getDogById: getDogById
) : ViewModel() {

    val groups = getAllDogs.invoke()
//    private val _state = mutableStateOf(DogsEncyclopediaStates())
//    val state: State<DogsEncyclopediaStates> = _state

    //тут должно быть действие
    suspend fun getDogById(id: Int): DogsBreedEncyclopediaEntity {
        return getDogById.invoke(id = id)
    }


}