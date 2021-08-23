package com.nyan.data.model

import com.google.gson.annotations.SerializedName
import com.nyan.domain.entity.PokemonEntity
import com.nyan.domain.entity.PokemonsEntity
import timber.log.Timber
import java.net.URI
import java.util.*

data class PokemonsResponseModel(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("previous")
	val previous: String? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem>
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
		results = response.results.map {
			mapToPokemonItemEntity(it)
		}
	)
}

private fun mapToPokemonItemEntity(item: ResultsItem?) : PokemonEntity {

	//Extract id from url last segment.
	val uri = URI(item?.url)
	val paths = uri.path.split("/").toTypedArray()
	val id = paths.get(paths.lastIndex - 1).toInt()
//	https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png
//	Timber.i("mapToPokemonItemEntity: ${paths.get(paths.lastIndex - 1)}")
	val imgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

	return PokemonEntity(
		name = item?.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
		url = item?.url,
		imgUrl = imgUrl
	)
}
