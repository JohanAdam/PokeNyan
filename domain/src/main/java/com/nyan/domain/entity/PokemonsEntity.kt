package com.nyan.domain.entity

data class PokemonsEntity(
	val next: String? = null,
	val previous: Any? = null,
	val count: Int? = null,
	val results: List<PokemonEntity>? = null
)

data class PokemonEntity(
	val name: String? = null,
	val url: String? = null,
	val imgUrl: String? = null
)

