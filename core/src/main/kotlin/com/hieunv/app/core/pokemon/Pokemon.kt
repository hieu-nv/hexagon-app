package com.hieunv.app.core.pokemon

/**
 * Data class representing a Pokémon.
 *
 * @code {
 *   {
 *     "name": "bulbasaur",
 *     "url": "https://pokeapi.co/api/v2/pokemon/1/"
 *   }
 * }
 */
data class Pokemon(
    val name: String = "", val url: String = ""
)