package com.hieunv.gw.pokemon

import com.hieunv.app.core.poke.Pokemon
import com.hieunv.app.core.poke.PokemonGateway
import com.hieunv.gw.client.Poke
import com.hieunv.gw.client.PokeClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service

@Service
class PokemonGatewayImpl(private val pokeClient: PokeClient) : PokemonGateway {

  override fun fetchPokemonList(
    limit: Int, offset: Int
  ): List<Pokemon?>? {
    return pokeClient["https://pokeapi.co/api/v2/pokemon?limit=$limit&offset=$offset", object :
      ParameterizedTypeReference<Poke<Pokemon>>() {}]?.let { response ->
      return response.results.map { it }
    }
  }
}