package aiss.model.resources;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.repository.MapRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.util.Collection;

import static org.junit.Assert.*;

public class UsersTest {

    static User user1, user2, user3, User4;
    static Task task;

    static MapRepository repository = new MapRepository();

    /*
    @BeforeClass
    public static void setUp() throws Exception {

        repository.addUser(User.of("Test list 1", null, null, null, null, null));
        user1 = repository.getUser("U0");
        repository.addUser(User.of("Test list 2", null, null, null, null, null));
        user2 = repository.getUser("U1");
        repository.addUser(User.of("Test list 3", null, null, null, null, null));
        user3 = repository.getUser("U2");


        repository.addTask(Task.of("Test title_2", "Test description_2", Status.CANCELLED, Date.valueOf("2022-01-01"), Date.valueOf("2022-01-02"), "Test annotation_2", 1, Difficulty.MEDIUM));
        task = repository.getTask("T0");
        if (task != null)
            repository.addTaskToUser(user1.getIdUser(), task.getIdTask());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        repository.deleteUser(user1.getIdUser());
        repository.deleteUser(user2.getIdUser());
        repository.deleteUser(User4.getIdUser());
        if (task != null)
            repository.deleteTask(task.getIdTask());
        repository.resetIndex();
    }

    @Test
    public void testGetAll() {
        Collection<User> users = repository.getAllUser();

        assertNotNull("The collection of playlists is null", users);

        // Show result
        System.out.println("Listing all playlists:");
        int i = 1;
        for (User user : users) {
            System.out.println("Playlist " + i++ + " : " + user.getName() + " (ID=" + user.getIdUser() + ")");
        }

    }

    @Test
    public void testGetPlaylist() {
        User u = repository.getUser(user1.getIdUser());

        assertEquals("The id of the playlists do not match", user1.getIdUser(), u.getIdUser());
        assertEquals("The name of the playlists do not match", user1.getName(), u.getName());

        // Show result
        System.out.println("Playlist id: " + u.getIdUser());
        System.out.println("Playlist name: " + u.getName());

    }

    @Test
    public void testAddPlaylist() {


        String name = "Add user test name";
        String surname = "Add user test surname";
        String email = "Add user test email";
        String avatar = "Add user test avatar";
        String bio = "Add user test bio";
        String location = "Add user test location";

        repository.addUser(User.of(name, surname, email, avatar, bio, location));
        User4 = repository.getUser("U3");

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
        String name = "Updated user name";

        // Update playlist
        user1.setName(name);

        repository.updateUser(user1);
        User user = repository.getUser(user1.getIdUser());

        assertEquals("The playlist's name has not been updated correctly", name, user.getName());
    }

    @Test
    public void testDeletePlaylist() {
        repository.deleteUser(user2.getIdUser());

        User user = repository.getUser(user2.getIdUser());
        assertNull("The playlist has not been deleted correctly", user);
    }

    @Test
    public void testAddTask() {
        if (task != null) {
            repository.addTaskToUser(user3.getIdUser(), task.getIdTask());
            User user = repository.getUser(user3.getIdUser());
            assertEquals("The task has been added correctly", task.getIdTask(), user.getTasks().get(0).getIdTask());
        }
    }

    @Test
    public void testRemoveTask() {
        if (task != null) {
            repository.deleteTaskToOrder(user3.getIdUser(), task.getIdTask());
            User user = repository.getUser(user3.getIdUser());
            assertEquals("The task has been removed correctly", 0, user.getTasks().size());
        }
    }
    */
}
