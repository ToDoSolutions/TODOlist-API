package aiss.model.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.jupiter.api.Test;


import aiss.model.pokemon.Pokemon;
import aiss.model.repository.MapRepository;

class PokemonTest {

	static Pokemon poke1;
	static Pokemon[] pokeAll;
	static MapRepository repository = new MapRepository(); 
	

	@Test
	public void testGetPokemon() {
		
		String name1= "Banette";
		poke1 = repository.getPokemon(name1);
		
		Pokemon p = repository.getPokemon(poke1.getName());
		
		
		assertEquals("The id of the Pokemons do not match", poke1.getName(), p.getName());
		assertEquals("The name of the Pokemons do not match", poke1.getGeneration(), p.getGeneration());
	
		// Show result
		System.out.println("Pokemon name: " +  p.getName());
		System.out.println("Pokemon generation: " +  p.getGeneration());

	}
//
//	@Test
//	public void testGetAlltPokemon() {
//		
//		
//		pokeAll = repository.getAllPokemons();
//		
//		Pokemon[] p = repository.getAllPokemons();
//		
//	
//		// Show result
//		System.out.println("Pokemon name: " +  pokeAll);
//		System.out.println("Pokemon generation: " +  pokeAll);
//	
//	}
	
	
}
