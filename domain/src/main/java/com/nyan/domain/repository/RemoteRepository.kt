package com.nyan.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.entity.PokemonsEntity
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow

const val DEFAULT_PAGE_LIMIT = 1

interface RemoteRepository {

    fun loadPokemons(offset: String, limit: String): Flow<DataState<PokemonsEntity>>

    fun loadPokemonsPaging(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<PokemonEntity>>

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_LIMIT, enablePlaceholders = true,
        maxSize = 100)
    }
}