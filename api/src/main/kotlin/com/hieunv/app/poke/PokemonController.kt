package com.hieunv.app.poke

import com.hieunv.app.core.poke.Pokemon
import com.hieunv.app.core.poke.PokemonGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/pokemon")
class PokemonController(
    private val pokemonGateway: PokemonGateway
) {
    /**
     * Get a list of Pokemon with pagination support
     *
     * @param limit maximum number of Pokemon to return (default: 20)
     * @param offset the offset for pagination (default: 0)
     * @return a list of Pokemon
     */
    @GetMapping
    fun getPokemonList(
        @RequestParam(defaultValue = "20") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): List<Pokemon> {
        return pokemonGateway.fetchPokemonList(limit, offset)?.filterNotNull() ?: emptyList()
    }
}
