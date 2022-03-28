package com.nyan.pokenyan.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyan.domain.entity.PokemonDetailEntity
import com.nyan.domain.state.DataState
import com.nyan.domain.usecases.PokemonDetailUseCase
import com.nyan.foodie.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class DetailsStateEvent {
    class GetPokemonDetailsEvent(val id: Int?) : DetailsStateEvent()
}

@HiltViewModel
class PokemonDetailsViewModel

@Inject
constructor(
    private val useCase: PokemonDetailUseCase) : ViewModel() {

    private val _isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    private val _errorMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val errorMsg: LiveData<Event<String>> get() = _errorMsg

    private val _pokemonDetails: MutableLiveData<PokemonDetailEntity> = MutableLiveData()
    val pokemonDetails: LiveData<PokemonDetailEntity> = _pokemonDetails

    private fun setStateEvent(event: DetailsStateEvent) {
        viewModelScope.launch {
            when (event) {
                is DetailsStateEvent.GetPokemonDetailsEvent -> {
                    useCase.execute(event.id!!)
                        .collect { dataState ->
                            _isLoading.value = Event(false)
                            when (dataState) {
                                is DataState.Loading -> {
                                    _isLoading.value = Event(true)
                                }
                                is DataState.Success -> {
                                    _pokemonDetails.value = dataState.data
                                }
                                is DataState.Failed -> {
                                    _errorMsg.value = Event(dataState.error.errorMsg)
                                }
                            }
                        }
//                        .launchIn(viewModelScope)
                }
            }
        }
    }

    fun getPokemonDetails(id: Int?) {
        setStateEvent(DetailsStateEvent.GetPokemonDetailsEvent(id))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}