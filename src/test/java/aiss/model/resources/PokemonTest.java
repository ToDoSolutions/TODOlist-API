package aiss.model.resources;

import static org.junit.Assert.*;

import org.junit.Test;

import aiss.model.repository.MapRepository;
import aiss.model.pokemon.Pokemon;

public class PokemonTest {

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
		
}
