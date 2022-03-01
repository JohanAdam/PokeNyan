package com.nyan.pokenyan.viewmodel.list

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.state.DataState
import com.nyan.domain.usecases.PokemonListUseCase
import com.nyan.domain.usecases.PokemonSearchUseCase
import com.nyan.foodie.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class PokemonsStateEvent {
    object GetPokemonsEvent : PokemonsStateEvent()
    object OpenPokemonDetailEvent : PokemonsStateEvent()
    object SearchPokemonEvent : PokemonsStateEvent()
}

@HiltViewModel
class PokemonsViewModel
@Inject constructor(
    private val pokemonListUseCase: PokemonListUseCase,
    private val pokemonSearchUseCase: PokemonSearchUseCase
) : ViewModel() {

    private val _isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    private val _errorMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val errorMsg: LiveData<Event<String>> get() = _errorMsg

    private val _listPokemon: MutableLiveData<PagingData<PokemonEntity>> = MutableLiveData()
    val listPokemon: LiveData<PagingData<PokemonEntity>> = _listPokemon

    private val _navigateToDetails: MutableLiveData<Event<PokemonEntity>> = MutableLiveData()
    val navigateToDetails: LiveData<Event<PokemonEntity>> get() = _navigateToDetails

    private var searchKey: String? = null
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
                is PokemonsStateEvent.OpenPokemonDetailEvent -> {
                    Timber.i("setStateEvent: Open Pokemon Details")
                    _navigateToDetails.postValue(Event(selectedPokemon!!))
                }
                is PokemonsStateEvent.SearchPokemonEvent -> {
                    Timber.i("setStateEvent: Search Pokemon Event")
                    pokemonSearchUseCase
                        .execute(searchKey!!)
                        .onEach { dataState ->
                            _isLoading.value = Event(false)
                            when (dataState) {
                                is DataState.Loading -> {
                                    Timber.i("setStateEvent: Loading")
                                    _isLoading.value = Event(true)
                                }
                                is DataState.Success -> {
                                    Timber.i("setStateEvent: Success")
                                    Timber.i("setStateEvent: ${dataState.data.size}")
                                    _listPokemon.value = PagingData.from(dataState.data)
                                }
                                is DataState.Failed -> {
                                    Timber.i("setStateEvent: Failed")
                                    _errorMsg.value = Event(dataState.error.errorMsg)
                                }
                                else -> _isLoading.value = Event(false)
                            }
                        }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun searchPokemon(searchKey: String) {
        this.searchKey = searchKey
        if (!TextUtils.isEmpty(searchKey)) {
            setStateEvent(PokemonsStateEvent.SearchPokemonEvent)
        } else {
            setStateEvent(PokemonsStateEvent.GetPokemonsEvent)
        }
    }

    fun openPokemonDetails(selectedPokemon: PokemonEntity) {
        this.selectedPokemon = selectedPokemon
        setStateEvent(PokemonsStateEvent.OpenPokemonDetailEvent)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}