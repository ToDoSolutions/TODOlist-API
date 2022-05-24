package aiss.model.resources;

import static org.junit.Assert.*;

import org.junit.Test;

import aiss.model.User;
import aiss.model.github.Owner;
import aiss.model.github.TaskGitHub;
import aiss.model.repository.MapRepository;
import aiss.utilities.Parse;

public class GitHubTest {

	static Owner owner1;
	static TaskGitHub taskg1, taskg2;
	static MapRepository repository = (MapRepository) new MapRepository().getInstance(); 
	
	
	
	@Test
	public void testGetOnwer() {
		
		
		 //Damos un owner 
		String propietario1= "alesanfe";
		
		owner1 = repository.getOwner(propietario1);
		
		//test
	
		User p = Parse.userFromGitHub(owner1);
		repository.addUser(p);

		
		assertEquals("The id of the GitHub do not match", owner1.getAvatarUrl(), p.getAvatar());
	
		// Show result
		System.out.println("GitHub Avatar: " +  owner1.getAvatarUrl());
	//	System.out.println("GitHub generation: " +  p.getAvatar());

	}
//	
//	
//	
//	@Test
//	public void testGetRepo() {
//		
//		//Damos un owner y un repositorio 
//		String propietarioGit= "alesanfe";
//		String repositorio= "TODOlist-API";
//		
//		taskg1 = repository.getRepo(propietarioGit, repositorio);
//		
//		
//		//test
//		TaskGitHub tg = repository.getRepo(taskg1.getId().toString(),repositorio);
//		
//		
//		assertEquals("The id of the GitHub do not match", taskg1.getId(), tg.getId());
//		assertEquals("The name of the GitHub do not match", taskg1.getUrl(), tg.getUrl());
//	
//		// Show result
//		System.out.println("GitHub name: " +  tg.getId());
//		System.out.println("GitHub generation: " +  tg.getUrl());
//
//	}
}
