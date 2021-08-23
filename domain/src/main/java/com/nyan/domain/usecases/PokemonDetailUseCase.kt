package com.nyan.domain.usecases

import com.nyan.domain.entity.PokemonDetailEntity
import com.nyan.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow

class PokemonDetailUseCase(private val remoteRepository: RemoteRepository) {

    fun execute(id: Int): Flow<PokemonDetailEntity> {
        return remoteRepository.loadPokemonDetails(id)
    }

}