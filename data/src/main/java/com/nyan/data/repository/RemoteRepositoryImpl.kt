package com.nyan.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nyan.data.model.PokemonResponseModel
import com.nyan.data.paging.PokemonPagingSource
import com.nyan.data.model.mapToDomain
import com.nyan.data.model.mapToPokemonDetailEntity
import com.nyan.data.service.NetworkService
import com.nyan.domain.entity.PokemonDetailEntity
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

    override fun loadPokemons(
        pagingConfig: PagingConfig): Flow<PagingData<PokemonEntity>> {
        Timber.i("loadPokemons: ")
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                PokemonPagingSource(networkService)
            }
        ).flow
    }

    override fun loadPokemonDetails(id: Int): Flow<DataState<PokemonDetailEntity>> = flow {
        //Return Loading.
        emit(DataState.Loading)

        try {

            //Result from the API.
            val pokemonResponse = networkService.getPokemonDetails(id)

            //Mapping the data model response to local model.
            val pokemon = mapToPokemonDetailEntity(pokemonResponse)

            //Return result.
            emit(DataState.Success(pokemon))

        } catch (e: Exception) {
            e.printStackTrace()
            //Return error.
            emit(DataState.Failed(ErrorHandler(e)))
        }

    }

}