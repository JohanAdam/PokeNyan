package com.nyan.pokenyan.viewmodel.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nyan.domain.entity.PokemonDetailEntity
import com.nyan.domain.state.DataState
import com.nyan.domain.usecases.PokemonDetailUseCase
import com.nyan.pokenyan.utils.InstantExecutorExtension
import com.nyan.pokenyan.utils.MainCoroutineRule
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class PokemonDetailsViewModelTest {

    private lateinit var pokemonDetailsViewModel: PokemonDetailsViewModel

    // Use a fake UseCase to be injected into the viewModel
    private val pokemonDetailUseCase: PokemonDetailUseCase = mockk()

    //Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

    }

    @Test
    fun `get Pokemon Details same ID`() {
        val pokemonId = 1

        //The answer.
        val model = PokemonDetailEntity(id = pokemonId)

        //1-Mock calls.
        coEvery {
            pokemonDetailUseCase.execute(pokemonId)
        } returns flow {
            emit(DataState.Success(model))
        }

        //2-Call.
        pokemonDetailsViewModel = PokemonDetailsViewModel(pokemonDetailUseCase)
        pokemonDetailsViewModel.getPokemonDetails(pokemonId)
        //Active observer for live data.
        pokemonDetailsViewModel.pokemonDetails.observeForever { }

        System.out.println("DA THING : ${pokemonDetailsViewModel.pokemonDetails.value}")

        //3-Verify.
        val isIdSame = pokemonDetailsViewModel.pokemonDetails.value?.id == pokemonId
        assertEquals(model.id, pokemonDetailsViewModel.pokemonDetails.value?.id)
        assertEquals(true, isIdSame)
    }

}