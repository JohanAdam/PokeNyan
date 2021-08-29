package com.nyan.domain.entity

data class PokemonDetailEntity(
	val types: List<TypesItem?>? = null,
	val weight: Int? = null,
	val sprites: Sprites? = null,
	val abilities: List<String?>? = null,
	val stats: List<StatsItem?>? = null,
	val moves: List<String?>? = null,
	val name: String? = null,
	val id: Int? = null,
	val height: Int? = null,
	val order: Int? = null
)

data class StatsItem(
	val stat: Stat? = null,
	val baseStat: Int? = null,
	val effort: Int? = null
)

data class Stat(
	val name: String? = null,
	val url: String? = null
)

data class TypesItem(
	val slot: Int? = null,
	val type: Type? = null
)

data class Type(
	val name: String? = null,
	val url: String? = null
)

data class Sprites(
	val backDefault: String? = null,
	val frontDefault: String? = null,
)

data class MovesItem(
	val name: String? = null,
	val url: String? = null)

data class AbilitiesItem(
	val name: String? = null,
	val url: String? = null,
	val isHidden: Boolean? = null,
	val slot: Int? = null
)

