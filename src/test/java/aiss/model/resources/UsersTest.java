package aiss.model.resources;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import aiss.api.resources.TaskResource;
import aiss.api.resources.UserResource;
import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;

public class UsersTest {

	static User User1, User2, User3, User4;
	static Task task;
	static UserResource usr = UserResource.getInstance();
	static TaskResource tsk = TaskResource.getInstance();
	
	@BeforeClass
	public static void setUp() throws Exception {
		
		User1 = usr.addUser(User.of("Test list 1", null, null, null, null, null));
		User2 = usr.addUser(User.of("Test list 2", null, null, null, null, null));
		User3 = usr.addUser(User.of("Test list 3", null, null, null, null, null));
		
	
		task = tsk.addTask(Task.of("Test title_2","Test description_2", Status.CANCELLED ,Date.valueOf("2022-01-01"),Date.valueOf("2022-01-02"),"Test annotation_2",1,Difficulty.MEDIUM));
		if(task!=null)
			usr.addTask(null, User1.getIdUser(), task.getIdTask());
	}

	@AfterClass
	public static void tearDown() throws Exception {
		usr.deleteUser(User1.getIdUser());
		usr.deleteUser(User2.getIdUser());
		usr.deleteUser(User4.getIdUser());
		if(task!=null)
			tsk.deleteTask(task.getIdTask());
	}
	@Test
	public void testGetAll() {
		Collection<Map<String, Object>> user = usr.getAll(null, null, null, null, null, null, null, null, null, null); 
		
		assertNotNull("The collection of playlists is null", User1);
		
		// Show result
		System.out.println("Listing all playlists:");
		int i=1;
		for (Map<String, Object> us : user) {
			System.out.println("Playlist " + i++ + " : " + us.getName() + " (ID=" + us.getIdUser() + ")");
		}
		
	}

	@Test
	public void testGetPlaylist() {
		User p = usr.getUser(User1.getIdUser(), User1.getBio(),User1.getAvatar());
		
		assertEquals("The id of the playlists do not match", User1.getIdUser(), p.getIdUser());
		assertEquals("The name of the playlists do not match", User1.getName(), p.getName());
		
		// Show result
		System.out.println("Playlist id: " +  p.getIdUser());
		System.out.println("Playlist name: " +  p.getName());

	}

	@Test
	public void testAddPlaylist() {
	 
        
		String name = "Add playlist test title";
		String surname = "Add playlist test description";
		String email;
		String avatar;
		String bio;
		String location;
		
		User4 = usr.addUser(User.of(name,surname,email,avatar,bio,location));
		
		assertNotNull("Error when adding the playlist", User4);
		assertEquals("The playlist's name has not been setted correctly", name, User4.getName());
		assertEquals("The playlist's description has not been setted corretly", surname, User4.getSurname());
		assertEquals("The playlist's name has not been setted correctly", email, User4.getEmail());
		assertEquals("The playlist's description has not been setted corretly", avatar, User4.getAvatar());
		assertEquals("The playlist's name has not been setted correctly", bio, User4.getBio());
		assertEquals("The playlist's description has not been setted corretly", location, User4.getLocation());
	}

	@Test
	public void testUpdatePlaylist() {
		String name = "Updated playlist name";

		// Update playlist
		User1.setName(name);

		boolean success = usr.updateUser(name);
		
		assertTrue("Error when updating the playlist", success);
		
		User pl  = usr.getUser(User1.getIdUser());
		assertEquals("The playlist's name has not been updated correctly", name, pl.getName());

	}

	@Test
	public void testDeletePlaylist() {
		boolean success = usr.deleteUser(User2.getIdUser());
		assertTrue("Error when deleting the playlist", success);
		
		User pl = usr.getUser(User2.getIdUser());
		assertNull("The playlist has not been deleted correctly", pl);
	}

	@Test
	public void testaddTask() {
		if(task!=null) {
			boolean success = usr.addTask(User3.getIdUser(), task.getIdTask());
			assertTrue("Error when adding the task", success);
		}
	}

	@Test
	public void testRemovetask() {
		//TODO
		if(task!=null) {
			boolean success = usr.deleteTask(User3.getIdUser(), task.getIdTask());
			assertTrue("Error when adding the task", success);
		}
	}
}
