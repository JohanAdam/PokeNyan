package com.nyan.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nyan.data.paging.PokemonPagingSource
import com.nyan.data.model.mapToDomain
import com.nyan.data.service.NetworkService
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.entity.PokemonsEntity
import com.nyan.domain.network.ErrorHandler
import com.nyan.domain.repository.RemoteRepository
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class RemoteRepositoryImpl(
    private val networkService: NetworkService) : RemoteRepository {

    override fun loadPokemons(offset: String, limit: String): Flow<DataState<PokemonsEntity>> = flow {
        //Return loading.
        emit(DataState.Loading)

        try {
            //Result data from API.
            val pokemonsResponse = networkService.getPokemons(offset, limit)

            //Mapping network model to domain model.
            val pokemons = mapToDomain(pokemonsResponse)

            //Return result.
            emit(DataState.Success(pokemons))

        } catch (e: HttpException) {
            //Return error.
            e.printStackTrace()
            emit(DataState.Failed(ErrorHandler(e)))
        }
    }

    override fun loadPokemonsPaging(
        pagingConfig: PagingConfig): Flow<PagingData<PokemonEntity>> {
        Timber.i("loadPokemonsPaging: ")
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                PokemonPagingSource(networkService)
            }
        ).flow
    }

//    fun loadPokemonsFlow(pagingConfig: PagingConfig = getDefaultPageConfig()) :
//            Flow<PagingData<ResultsItem>> {
//        return Pager(
//            config = pagingConfig,
//            pagingSourceFactory = {
//                PokemonPagingSource(networkService)
//            }
//        ).flow
//    }

}