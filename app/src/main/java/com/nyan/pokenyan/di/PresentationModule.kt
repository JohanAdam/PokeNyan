package com.nyan.pokenyan.di

import com.nyan.pokenyan.viewmodel.list.PokemonsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object PresentationModule {

    val presentationModule = module {
        viewModel {
            PokemonsViewModel(get())
        }
    }

}