package com.sagirov.ilovedog.presentation

import androidx.lifecycle.ViewModel
import com.sagirov.ilovedog.domain.usecase.getAllDogsUseCase
import com.sagirov.ilovedog.domain.usecase.getDogByIdUseCase
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DogsEncyclopediaViewModel @Inject constructor(
    getAllDogsUseCase: getAllDogsUseCase,
    private val getDogByIdUseCase: getDogByIdUseCase
) : ViewModel() {

    val groups = getAllDogsUseCase.invoke()
//    private val _state = mutableStateOf(DogsEncyclopediaStates())
//    val state: State<DogsEncyclopediaStates> = _state

    //тут должно быть действие
    suspend fun getDogById(id: Int): DogsBreedEncyclopediaEntity {
        return getDogByIdUseCase.invoke(id = id)
    }


}