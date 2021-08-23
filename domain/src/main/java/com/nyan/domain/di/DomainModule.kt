package com.nyan.domain.di

import com.nyan.domain.usecases.PokemonDetailUseCase
import com.nyan.domain.usecases.PokemonListUseCase
import org.koin.dsl.module

object DomainModule {

    val domainModule = module {
        factory { PokemonListUseCase(remoteRepository = get()) }
        factory { PokemonDetailUseCase(remoteRepository = get()) }
    }

}