package com.nyan.data.service

import com.nyan.data.model.PokemonsResponseModel
import com.nyan.domain.entity.PokemonsEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    companion object {
        const val BASE_URL = "https://pokeapi.co/"
    }

    @GET("/api/v2/pokemon")
    suspend fun getPokemons(
        @Query("offset") offset: String,
        @Query("limit") limit: String): PokemonsResponseModel

}