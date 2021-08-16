package com.nyan.domain.usecases

import com.nyan.domain.entity.PokemonsEntity
import com.nyan.domain.repository.RemoteRepository
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow

class PokemonListUseCase(private val remoteRepository: RemoteRepository) {

    fun execute(): Flow<DataState<PokemonsEntity>> {
        return remoteRepository.loadPokemons()
    }

}