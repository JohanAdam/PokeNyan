package com.nyan.data.model

import com.google.gson.annotations.SerializedName
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.entity.PokemonsEntity
import java.util.*

data class PokemonsResponseModel(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("previous")
	val previous: String? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null
)

data class ResultsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

fun mapToDomain(response: PokemonsResponseModel) : PokemonsEntity {
	return PokemonsEntity(
		next = response.next,
		previous = response.previous,
		count = response.count,
		results = response.results?.map {
			mapToPokemonItemEntity(it)
		}
	)
}

private fun mapToPokemonItemEntity(item: ResultsItem?) : PokemonEntity {
	return PokemonEntity(
		name = item?.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
		url = item?.url
	)
}
