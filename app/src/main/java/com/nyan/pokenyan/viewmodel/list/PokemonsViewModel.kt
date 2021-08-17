package com.nyan.pokenyan.viewmodel.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyan.data.model.PokemonsResponseModel
import com.nyan.data.model.ResultsItem
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.state.DataState
import com.nyan.domain.usecases.PokemonListUseCase
import com.nyan.foodie.event.Event
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class PokemonsStateEvent {
    object GetPokemonsEvent: PokemonsStateEvent()
}

class PokemonsViewModel(
    private val pokemonListUseCase: PokemonListUseCase): ViewModel() {

    private val _isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    private val _errorMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val errorMsg: LiveData<Event<String>> get() = _errorMsg

    private val _listPokemon: MutableLiveData<List<PokemonEntity>> = MutableLiveData()
    val listPokemon: LiveData<List<PokemonEntity>> = _listPokemon

    init {
        getPokemons()
    }

    fun getPokemons() {
        setStateEvent(PokemonsStateEvent.GetPokemonsEvent)
    }

    private fun setStateEvent(event: PokemonsStateEvent) {
        viewModelScope.launch {
            when(event) {
                is PokemonsStateEvent.GetPokemonsEvent -> {
                    pokemonListUseCase.execute("0", "1110")
                        .onEach { dataState ->
                            _isLoading.value = Event(false)
                            when(dataState) {
                                is DataState.Loading -> {
                                    _isLoading.value = Event(true)
                                }
                                is DataState.Success -> {
                                    _listPokemon.value = dataState.data.results
                                }
                                is DataState.Failed -> {
                                    _errorMsg.value = Event(dataState.error.errorMsg)
                                }
                                else -> _isLoading.value = Event(false)
                            }
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        //TODO Yep, I aware about this, and find a better solution for this.
        viewModelScope.cancel()
    }

}