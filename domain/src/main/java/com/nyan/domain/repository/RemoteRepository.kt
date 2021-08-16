package com.nyan.domain.repository

import com.nyan.domain.entity.PokemonsEntity
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    fun loadPokemons(offset: String, limit: String): Flow<DataState<PokemonsEntity>>

}