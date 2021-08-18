package com.nyan.pokenyan.viewmodel.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nyan.data.model.PokemonsResponseModel
import com.nyan.data.model.ResultsItem
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.state.DataState
import com.nyan.domain.usecases.PokemonListUseCase
import com.nyan.foodie.event.Event
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

sealed class PokemonsStateEvent {
    object GetPokemonsEvent: PokemonsStateEvent()
    object GetPokemonsEventTest: PokemonsStateEvent()
}

class PokemonsViewModel(
    private val pokemonListUseCase: PokemonListUseCase): ViewModel() {

    private var offset = "0"

    private val _isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    private val _errorMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val errorMsg: LiveData<Event<String>> get() = _errorMsg

    private val _listPokemon: MutableLiveData<List<PokemonEntity>> = MutableLiveData()
    val listPokemon: LiveData<List<PokemonEntity>> = _listPokemon

    private val _listPokemonTest: MutableLiveData<PagingData<PokemonEntity>> = MutableLiveData()
    val listPokemonTest: LiveData<PagingData<PokemonEntity>> = _listPokemonTest

    init {
        getPokemons()
    }

    fun getPokemons() {
        setStateEvent(PokemonsStateEvent.GetPokemonsEventTest)
    }

    private fun setStateEvent(event: PokemonsStateEvent) {
        viewModelScope.launch {
            when(event) {
                is PokemonsStateEvent.GetPokemonsEvent -> {
                    pokemonListUseCase.execute(offset, "20")
                        .onEach { dataState ->
                            _isLoading.value = Event(false)
                            when(dataState) {
                                is DataState.Loading -> {
                                    _isLoading.value = Event(true)
                                }
                                is DataState.Success -> {
                                    offset = dataState.data.results?.size.toString()
                                    Timber.i("setStateEvent: Update offset to $offset")
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
                is PokemonsStateEvent.GetPokemonsEventTest -> {
                    Timber.e("setStateEvent: TEST PAGING")
//                    testPokemonPaging().distinctUntilChanged().collectLatest {
//                        Timber.i("setStateEvent: Collect Latest!")
//                        _listPokemonTest.value = it
//                    }
                }
            }
        }
    }

     fun testPokemonPaging(): Flow<PagingData<PokemonEntity>> {
        Timber.i("testPokemonPaging: ")
        return pokemonListUseCase
            .executePaging()
            .cachedIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}