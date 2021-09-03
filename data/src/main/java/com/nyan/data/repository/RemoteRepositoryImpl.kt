package com.nyan.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nyan.data.model.mapToDomain
import com.nyan.data.paging.PokemonPagingSource
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

    override fun searchPokemon(searchKey: String): Flow<DataState<List<PokemonEntity>>> = flow {
        //Return loading.
        emit(DataState.Loading)

        try {

            val total = networkService.getPokemons("0", "1").count

            val pokemonResponse = networkService.getPokemons("0", total.toString())

            val pokemonsEntity = mapToDomain(pokemonResponse)

            val filteredList = pokemonsEntity.results.filter { item ->
                item.name!!.contains(searchKey, true)
            }

            emit(DataState.Success(filteredList))

        } catch (e: Exception) {
            e.printStackTrace()
            //Return error.
            emit(DataState.Failed(ErrorHandler(e)))
        }

    }

}