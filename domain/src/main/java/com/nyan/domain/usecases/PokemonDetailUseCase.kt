package com.nyan.domain.usecases

import com.nyan.domain.entity.PokemonDetailEntity
import com.nyan.domain.repository.RemoteRepository
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow

class PokemonDetailUseCase(private val remoteRepository: RemoteRepository) {

    suspend fun execute(id: Int): Flow<DataState<PokemonDetailEntity>> {
        return remoteRepository.loadPokemonDetails(id)
    }

}