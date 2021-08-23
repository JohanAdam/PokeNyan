package com.nyan.pokenyan.viewmodel.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.usecases.PokemonListUseCase
import com.nyan.foodie.event.Event
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

sealed class PokemonsStateEvent {
    object GetPokemonsEvent : PokemonsStateEvent()
    object OpenPokemonDetail : PokemonsStateEvent()
}

class PokemonsViewModel(
    private val pokemonListUseCase: PokemonListUseCase
) : ViewModel() {

    private val _isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    private val _errorMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val errorMsg: LiveData<Event<String>> get() = _errorMsg

    private val _listPokemon: MutableLiveData<PagingData<PokemonEntity>> = MutableLiveData()
    val listPokemon: LiveData<PagingData<PokemonEntity>> = _listPokemon

    private val _navigateToDetails: MutableLiveData<Event<PokemonEntity>> = MutableLiveData()
    val navigateToDetails: LiveData<Event<PokemonEntity>> get() = _navigateToDetails

    private var selectedPokemon: PokemonEntity? = null

    init {
        getPokemons()
    }

    fun getPokemons() {
        setStateEvent(PokemonsStateEvent.GetPokemonsEvent)
    }

    private fun setStateEvent(event: PokemonsStateEvent) {
        viewModelScope.launch {
            when (event) {
//                is PokemonsStateEvent.GetPokemonsEvent -> {
//                    pokemonListUseCase.execute(offset, "20")
//                        .onEach { dataState ->
//                            _isLoading.value = Event(false)
//                            when(dataState) {
//                                is DataState.Loading -> {
//                                    _isLoading.value = Event(true)
//                                }
//                                is DataState.Success -> {
//                                    offset = dataState.data.results?.size.toString()
//                                    Timber.i("setStateEvent: Update offset to $offset")
//                                    _listPokemon.value = dataState.data.results
//                                }
//                                is DataState.Failed -> {
//                                    _errorMsg.value = Event(dataState.error.errorMsg)
//                                }
//                                else -> _isLoading.value = Event(false)
//                            }
//                        }
//                        .launchIn(viewModelScope)
//                }
                is PokemonsStateEvent.GetPokemonsEvent -> {
                    Timber.e("setStateEvent: Get Pokemon")
                    pokemonListUseCase
                        .execute()
                        .cachedIn(viewModelScope)
                        .distinctUntilChanged()
                        .collectLatest {
                            _listPokemon.value = it
                        }
                }
                is PokemonsStateEvent.OpenPokemonDetail -> {
                    Timber.i("setStateEvent: Open Pokemon Details")
                    _navigateToDetails.postValue(Event(selectedPokemon!!))
                }
            }
        }
    }

    fun openPokemonDetails(selectedPokemon: PokemonEntity) {
        this.selectedPokemon = selectedPokemon
        setStateEvent(PokemonsStateEvent.OpenPokemonDetail)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}