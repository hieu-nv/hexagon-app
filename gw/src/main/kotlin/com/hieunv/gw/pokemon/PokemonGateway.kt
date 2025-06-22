package com.hieunv.gw.pokemon

import com.hieunv.app.core.pokemon.Pokemon
import com.hieunv.gw.client.Poke
import com.hieunv.gw.client.PokeClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service

@Service
class PokemonGateway(private val pokeClient: PokeClient) : com.hieunv.app.core.pokemon.PokemonGateway {

    override fun fetchPokemonList(
        limit: Int, offset: Int
    ): List<Pokemon?>? {
        return pokeClient.get(
            "https://pokeapi.co/api/v2/pokemon?limit=$limit&offset=$offset",
            object : ParameterizedTypeReference<Poke<Pokemon>>() {})?.let { response ->
            return response.results.map { it }
        }
    }
}