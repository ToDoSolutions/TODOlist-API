package aiss.model.resources;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import aiss.api.resources.TaskResource;
import aiss.model.Difficulty;
import aiss.model.Song;
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
	public void testGetSong() {
		Task t = tsk.getTask(task1.getIdTask());
		
		assertEquals("The id of the task do not match", task1.getIdTask(), t.getIdTask());
		assertEquals("The title of the task do not match", task1.getTitle(), t.getTitle());
	
		// Show result
		System.out.println("Task id: " +  t.getIdTask());
		System.out.println("Task Title: " +  t.getTitle());

	}

}
