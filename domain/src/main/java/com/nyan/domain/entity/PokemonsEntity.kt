package com.nyan.domain.entity

data class PokemonsEntity(
	val next: String? = null,
	val previous: Any? = null,
	val count: Int? = null,
	val results: List<Pokemon?>? = null
)

data class Pokemon(
	val name: String? = null,
	val url: String? = null
)

