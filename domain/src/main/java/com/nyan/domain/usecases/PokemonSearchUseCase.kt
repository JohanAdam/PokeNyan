package com.nyan.domain.usecases

import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.entity.PokemonsEntity
import com.nyan.domain.repository.RemoteRepository
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow

class PokemonSearchUseCase(private val remoteRepository: RemoteRepository) {

    fun execute(searchKey: String): Flow<DataState<List<PokemonEntity>>> {
        return remoteRepository.searchPokemon(searchKey)
    }

}