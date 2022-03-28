package com.nyan.pokenyan.viewmodel.list

import com.nyan.domain.usecases.PokemonListUseCase
import com.nyan.pokenyan.utils.InstantExecutorExtension
import com.nyan.pokenyan.utils.MainCoroutineRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class PokemonsViewModelTest {

    private lateinit var pokemonListViewModel: PokemonsViewModel

    private val pokemonListUseCase: PokemonListUseCase = mockk()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

}