package com.nyan.data.repository

import com.nyan.data.model.PokemonsResponseModel
import com.nyan.data.model.mapToDomain
import com.nyan.data.service.NetworkService
import com.nyan.domain.entity.PokemonsEntity
import com.nyan.domain.network.ErrorHandler
import com.nyan.domain.repository.RemoteRepository
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

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

}