package com.nyan.data.repository

import com.google.gson.GsonBuilder
import com.nyan.data.service.NetworkService
import com.nyan.data.util.enqueueResponse
import com.nyan.domain.entity.*
import com.nyan.domain.state.DataState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteRepositoryImplTest {

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val networkService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(NetworkService::class.java)

    private val sut = RemoteRepositoryImpl(networkService)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should fetch pokemon list and return 200 response`() {
        mockWebServer.enqueueResponse("pokemon-details-200.json", HttpURLConnection.HTTP_OK)

        runBlocking {
            val result = sut.loadPokemonDetails(1)

            var actualData: DataState<PokemonDetailEntity>? = null
            result.collect { dataState ->
                actualData = dataState
//                when (dataState) {
//                    is DataState.Success -> {
//                        actual = dataState.data
//                    }
//                }
            }

            val expectedItem = PokemonDetailEntity(
                types = arrayListOf(
                    TypesItem(
                    slot = 1,
                    type = Type(
                        name = "grass",
                        url = "https://pokeapi.co/api/v2/type/12/"
                    )
                ),
                TypesItem(
                    slot = 2,
                    type = Type(
                        name = "poison",
                        url = "https://pokeapi.co/api/v2/type/4/"
                    ))
                ),
                weight = 69,
                sprites = Sprites(
                    backDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
                    frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
                ),
                abilities = arrayListOf(
                    "Overgrow",
                    "Chlorophyll"
                ),
                stats = arrayListOf(
                    StatsItem(
                        stat = Stat(
                            name = "hp",
                            url = "https://pokeapi.co/api/v2/stat/1/"
                        ),
                        baseStat = 45,
                        effort = 0
                    ),
                    StatsItem(
                        stat = Stat(
                            name = "attack",
                            url = "https://pokeapi.co/api/v2/stat/2/"
                        ),
                        baseStat = 49,
                        effort = 0
                    ),
                    StatsItem(
                        stat = Stat(
                            name = "defense",
                            url = "https://pokeapi.co/api/v2/stat/3/"
                        ),
                        baseStat = 49,
                        effort = 0
                    ),
                    StatsItem(
                        stat = Stat(
                            name = "special-attack",
                            url = "https://pokeapi.co/api/v2/stat/4/"
                        ),
                        baseStat = 65,
                        effort = 1
                    ),
                    StatsItem(
                        stat = Stat(
                            name = "special-defense",
                            url = "https://pokeapi.co/api/v2/stat/5/"
                        ),
                        baseStat = 65,
                        effort = 0
                    ),
                    StatsItem(
                        stat = Stat(
                            name = "speed",
                            url = "https://pokeapi.co/api/v2/stat/6/"
                        ),
                        baseStat = 45,
                        effort = 0
                    )
                ),
                moves = arrayListOf(
                    "Razorwind",
                    "Swordsdance",
                    "Cut",
                    "Bind",
                    "Vinewhip",
                    "Headbutt",
                    "Tackle",
                    "Bodyslam",
                    "Takedown",
                    "Doubleedge",
                    "Growl",
                    "Strength",
                    "Megadrain",
                    "Leechseed",
                    "Growth",
                    "Razorleaf",
                    "Solarbeam",
                    "Poisonpowder",
                    "Sleeppowder",
                    "Petaldance",
                    "Stringshot",
                    "Toxic", "Rage",
                    "Mimic",
                    "Doubleteam",
                    "Defensecurl",
                    "Lightscreen",
                    "Reflect",
                    "Bide",
                    "Sludge",
                    "Skullbash",
                    "Amnesia",
                    "Flash",
                    "Rest",
                    "Substitute",
                    "Snore",
                    "Curse",
                    "Protect",
                    "Sludgebomb",
                    "Mudslap",
                    "Outrage",
                    "Gigadrain",
                    "Endure",
                    "Charm",
                    "Falseswipe",
                    "Swagger", "Furycutter", "Attract", "Sleeptalk", "Return", "Frustration",
                    "Safeguard", "Sweetscent", "Synthesis", "Hiddenpower", "Sunnyday", "Rocksmash",
                    "Facade", "Naturepower", "Helpinghand", "Ingrain", "Knockoff", "Secretpower",
                    "Weatherball", "Grasswhistle", "Bulletseed", "Magicalleaf", "Naturalgift", "Worryseed",
                    "Seedbomb", "Energyball", "Leafstorm", "Powerwhip", "Captivate", "Grassknot", "Venoshock",
                    "Round", "Echoedvoice", "Grasspledge", "Workup", "Grassyterrain", "Confide", "Grassyglide"
                ),
                name = "bulbasaur",
                id = 1,
                height = 7,
                order = 1
            )

            val expectedData = DataState.Success(expectedItem)

            assertEquals(expectedData, actualData)
        }
    }

}