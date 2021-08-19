package com.nyan.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.entity.PokemonsEntity
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow

const val DEFAULT_PAGE_LIMIT = 0

interface RemoteRepository {

//    fun loadPokemons(offset: String, limit: String): Flow<DataState<PokemonsEntity>>

    fun loadPokemons(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<PokemonEntity>>

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 25, enablePlaceholders = false)
    }
}