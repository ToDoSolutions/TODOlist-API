package aiss.model.resources;

import aiss.model.Task;
import aiss.model.repository.MapRepository;


public class TaskTest {


    static Task task1, task2, task3;
    static MapRepository repository = new MapRepository(); 

    /*
    @BeforeClass
    public static void setup() throws Exception {

        // Test Task 1
        repository.addTask(Task.of("Test title", "Test description", Status.DRAFT, Date.valueOf("2020-01-01"), Date.valueOf("2020-01-02"), "Test annotation", 1, Difficulty.EASY));
        task1 = repository.getTask("T0");
        // Test Task 2
        repository.addTask(Task.of("Test title_2", "Test description_2", Status.CANCELLED, Date.valueOf("2022-01-01"), Date.valueOf("2022-01-02"), "Test annotation_2", 1, Difficulty.MEDIUM));
        task2 = repository.getTask("T1");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        repository.deleteTask(task1.getIdTask());
        repository.deleteTask(task3.getIdTask());
        repository.resetIndex();
    }

    @Test
    public void testGetAll() {
        //autocompletando me sale asi sin errores

        Collection<Task> tasks = repository.getAllTask();

        assertNotNull("The collection of tasks is null", tasks);

        // Show result
        System.out.println("Getting all tasks :");
        int i = 1;
        for (Task task : tasks) {
            System.out.println("Task " + i++ + " : " + task.getTitle() + " (ID=" + task.getIdTask() + ")");
        }
    }

    @Test
    public void testGetTask() throws NotFoundException {
        Task t = repository.getTask(task1.getIdTask());

        assertEquals("The id of the task do not match", task1.getIdTask(), t.getIdTask());
        assertEquals("The title of the task do not match", task1.getTitle(), t.getTitle());

        // Show result
        System.out.println("Task id: " + t.getIdTask());
        System.out.println("Task Title: " + t.getTitle());

    }

    @Test
    public void testAddTask() {


        String title = "Add task test title";
        String description = "Add task description";
        Status status = Status.CANCELLED;
        Date startedDate = Date.valueOf("2023-01-01");
        Date finishedDate = Date.valueOf("2023-01-02");
        String annotation = "Add task annotation";
        Integer priority = 2;
        Difficulty difficulty = Difficulty.EASY;


        repository.addTask(Task.of(title, description, status, startedDate, finishedDate, annotation, priority, difficulty));
        task3 = repository.getTask("T2");

        //cambiar los " "
        assertNotNull("Error when adding the song", task1);
        assertEquals("The song title has not been setted correctly", title, task3.getTitle());
        assertEquals("The song artits has not been setted correctly", description, task3.getDescription());
        assertEquals("The song album has not been setted correctly", status, task3.getStatus());
        assertEquals("The song year has not been setted correctly", startedDate, task3.getStartDate());  //se ha cambiado el nombre al get?
        assertEquals("The song title has not been setted correctly", finishedDate, task3.getFinishedDate());
        assertEquals("The song artits has not been setted correctly", annotation, task3.getAnnotation());
        assertEquals("The song album has not been setted correctly", priority, task3.getPriority());
        assertEquals("The song year has not been setted correctly", difficulty, task3.getDifficulty());

    }

    @Test
    public void testUpdateTask() {

        String title = "Update task title";
        String description = "Update task description";
        Status status = Status.CANCELLED;
        Date startedDate = Date.valueOf("2023-01-01");
        Date finishedDate = Date.valueOf("2023-01-02");
        String annotation = "Update task annotation";
        Integer priority = 2;
        Difficulty difficulty = Difficulty.EASY;

        // Update song
        task1.setTitle(title);
        task1.setDescription(description);
        task1.setStatus(status);
        task1.setReleaseDate(startedDate);        //se ha cambiado el nombre al set?
        task1.setFinishedDate(finishedDate);
        task1.setAnnotation(annotation);
        task1.setPriority(priority);
        task1.setDifficulty(difficulty);

        repository.updateTask(task1);

        //cambiar los " "
        Task task = repository.getTask(task1.getIdTask());
        assertEquals("The song title has not been setted correctly", title, task.getTitle());
        assertEquals("The song artits has not been setted correctly", description, task.getDescription());
        assertEquals("The song album has not been setted correctly", status, task.getStatus());
        assertEquals("The song year has not been setted correctly", startedDate, task.getStartDate()); //se ha cambiado el nombre al get?
        assertEquals("The song title has not been setted correctly", finishedDate, task.getFinishedDate());
        assertEquals("The song artits has not been setted correctly", annotation, task.getAnnotation());
        assertEquals("The song album has not been setted correctly", priority, task.getPriority());
        assertEquals("The song year has not been setted correctly", difficulty, task.getDifficulty());
    }

    @Test
    public void testDeleteSong() {

        // Delete songs
        repository.deleteTask(task2.getIdTask());

        Task task = repository.getTask(task2.getIdTask()); // Esta línea debe de petar.
        assertNull("The song has not been deleted correctly", task);
    }
     */

}
