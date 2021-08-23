package com.nyan.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nyan.data.model.mapToDomain
import com.nyan.data.service.NetworkService
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.repository.DEFAULT_PAGE_LIMIT
import retrofit2.HttpException
import java.io.IOException


class PokemonPagingSource(private val networkService: NetworkService):
    PagingSource<Int, PokemonEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonEntity> {
        //For first case it will be null, then we can pass some default value, in our case it's 1.
        val page = params.key ?: DEFAULT_PAGE_LIMIT
        return try {
            val response = networkService.getPokemons(page.toString(), "20")

            val pokemonEntity = mapToDomain(response)

            LoadResult.Page(
                pokemonEntity.results,
                prevKey = null,
                nextKey = if (response.results.isEmpty()) null else page + 20
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonEntity>): Int? {
        return state.anchorPosition
    }


}