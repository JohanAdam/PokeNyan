package com.nyan.domain.repository

import com.nyan.domain.entity.PokemonsEntity
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    fun loadPokemons(): Flow<DataState<PokemonsEntity>>

}