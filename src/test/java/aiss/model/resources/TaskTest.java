package aiss.model.resources;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.google.api.client.util.Data;

import aiss.api.resources.TaskResource;
import aiss.model.Difficulty;

import aiss.model.Status;
import aiss.model.Task;




public class TaskTest {
	
	
	static Task task1, task2, task3;
	static TaskResource tsk = new TaskResource(); // hay alguna forma de quitar que sea publico

	@BeforeClass
	public static void setup() throws Exception {
		
		// Test Task 1
		task1 = tsk.addTask(new Task("Test title","Test description", Status.DRAFT ,Date.valueOf("2020-01-01"),Date.valueOf("2020-01-02"),"Test annotation",1,Difficulty.EASY));
		
		// Test Task 2
		task2 = tsk.addTask(new Task("Test title_2","Test description_2", Status.CANCELED ,Date.valueOf("2022-01-01"),Date.valueOf("2022-01-02"),"Test annotation_2",1,Difficulty.MEDIUM));
		
	}

	@AfterClass
	public static void tearDown() throws Exception {
		tsk.deleteTask(task1.getIdTask());
		tsk.deleteTask(task3.getIdTask());
	}

	@Test
	public void testGetAll() {
		//autocompletando me sale asi sin errores
		
		Collection<Map<String, Object>> tasks = tsk.getAllTasks(null, null, null, null);
		
		assertNotNull("The collection of tasks is null", tasks);
		
		// Show result
		System.out.println("Getting all tasks :");
		int i=1;
		for (Map<String, Object> s : tasks) {
			System.out.println("Task " + i++ + " : " + ((Task) s).getTitle() + " (ID=" + ((Task) s).getIdTask() + ")");
		}
	}
	
	@Test
	public void testGetTask() {
		Task t = tsk.getTask(task1.getIdTask());
		
		assertEquals("The id of the task do not match", task1.getIdTask(), t.getIdTask());
		assertEquals("The title of the task do not match", task1.getTitle(), t.getTitle());
	
		// Show result
		System.out.println("Task id: " +  t.getIdTask());
		System.out.println("Task Title: " +  t.getTitle());

	}
	
	@Test
	public void testAddTask() {
		
		
		String title = "Add task test title";
		String description = "Add task description";
		Status status = Status.CANCELED;
		Date  startedDate = Date.valueOf("2023-01-01");
		Date finishedDate=Date.valueOf("2023-01-02");
		String annotation= "Add task annotation";
		Integer priority = 2;
		Difficulty difficulty= Difficulty.EASY;
		
		
		task3 = tsk.addTask(null, new Task(title,description,status,startedDate,finishedDate,annotation,priority,difficulty));
				
			//cambiar los " " 	
		assertNotNull("Error when adding the song", task1);
		assertEquals("The song title has not been setted correctly", title, task3.getTitle());
		assertEquals("The song artits has not been setted correctly", description, task3.getDescription());
		assertEquals("The song album has not been setted correctly", status, task3.getStatus());
		assertEquals("The song year has not been setted correctly", startedDate, task3.getStartedDate());
		assertEquals("The song title has not been setted correctly", finishedDate, task3.getFinishedDate());
		assertEquals("The song artits has not been setted correctly", annotation, task3.getAnnotation());
		assertEquals("The song album has not been setted correctly", priority, task3.getPriority());
		assertEquals("The song year has not been setted correctly", difficulty, task3.getDifficulty());
		
	}
	
	@Test
	public void testUpdateTask() {
		
		String title = "Update task title";
		String description = "Update task description";
		Status status = Status.CANCELED;
		Date  startedDate = Date.valueOf("2023-01-01");
		Date finishedDate=Date.valueOf("2023-01-02");
		String annotation= "Update task annotation";
		Integer priority = 2;
		Difficulty difficulty= Difficulty.EASY;
		
		// Update song
		task1.setTitle(title);
		task1.setDescription(description);
		task1.setStatus(status);
		task1.setStartedDate(startedDate);
		task1.setFinishedDate(finishedDate);
		task1.setAnnotation(annotation);
		task1.setPriority(priority);
		task1.setDifficulty(difficulty);
		
		Response success = tsk.updateTask(task1);
		
		assertTrue("Error when updating the song", success);
		
		//cambiar los " " 	
		Task task  = tsk.getTask(task1.getIdTask());
		assertEquals("The song title has not been setted correctly", title, task.getTitle());
		assertEquals("The song artits has not been setted correctly", description, task.getDescription());
		assertEquals("The song album has not been setted correctly", status, task.getStatus());
		assertEquals("The song year has not been setted correctly", startedDate, task.getStartedDate());
		assertEquals("The song title has not been setted correctly", finishedDate, task.getFinishedDate());
		assertEquals("The song artits has not been setted correctly", annotation, task.getAnnotation());
		assertEquals("The song album has not been setted correctly", priority, task.getPriority());
		assertEquals("The song year has not been setted correctly", difficulty, task.getDifficulty());
	}
	
	@Test
	public void testDeleteSong() {
		
		// Delete songs
		Response success = tsk.deleteTask(task2.getIdTask());
		
		assertTrue("Error when deleting the song", success);
		
		Task task  = tsk.getTask(task2.getIdTask());
		assertNull("The song has not been deleted correctly", task);
	}

}
