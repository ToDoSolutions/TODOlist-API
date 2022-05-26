package aiss.model.resources;

import static org.junit.Assert.*;

import org.junit.Test;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.github.Owner;
import aiss.model.github.TaskGitHub;
import aiss.model.repository.MapRepository;
import aiss.utilities.Parse;

public class GitHubTest {

	static Owner owner1;
	static TaskGitHub repo1;
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

	}
	
	
	
	@Test
	public void testGetRepo() {
		
		//Damos un owner y un repositorio 
		String propietario1= "alesanfe";
		String repositorio= "TODOlist-API";
		Status status= Status.IN_PROGRESS;
       
        Integer priority = 2;
        Difficulty difficulty = Difficulty.EASY;
		
		repo1 = repository.getRepo(propietario1, repositorio);
		
		
		//test
		Task tg = Parse.taskFromGitHub(repo1, status, repositorio, priority, difficulty);
		repository.addTask(tg);;
	
	
		// Show result
		System.out.println("GitHubTask Difficulty: " +  tg.getDifficulty());
	//	System.out.println("GitHub generation: " +  tg.getUrl());

	}
}
