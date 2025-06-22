package com.hieunv.gw.client

/**
 * Data class representing a Poke API response.
 *
 * @code {
 * {
 *   "count": 1302,
 *   "next": "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
 *   "previous": null,
 *   "results": [
 *     {
 *       "name": "bulbasaur",
 *       "url": "https://pokeapi.co/api/v2/pokemon/1/"
 *     },
 *     {
 *       "name": "ivysaur",
 *       "url": "https://pokeapi.co/api/v2/pokemon/2/"
 *     }
 *   ]
 * }
 */
data class Poke<T>(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<T> = emptyList()
)