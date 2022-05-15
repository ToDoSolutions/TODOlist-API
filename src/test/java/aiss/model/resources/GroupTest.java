package aiss.model.resources;

import aiss.model.Group;
import aiss.model.User;
import aiss.model.repository.MapRepository;

public class GroupTest {


    static Group group1, group2, group3, group4;
    static User user;

    static MapRepository repository = new MapRepository();

    /*
    @BeforeClass
    public static void setUp() throws Exception {

        repository.addGroup(Group.of( "name" , "description", 2,  Date.valueOf("2022-01-01")));
        group1 = repository.getGroup("G0");
        repository.addGroup(Group.of("name" , "description", 2,  Date.valueOf("2022-01-01")));
        group2 = repository.getGroup("G1");
        repository.addGroup(Group.of("G2" , "description", 2,  Date.valueOf("2022-01-01")));
        group3 = repository.getGroup("G2");


        repository.addUser(User.of(null, null, null, null, null, null));
        user = repository.getUser("U0");
        if (user != null)
            repository.addTaskToUser(group1.getIdGroup(), user.getIdUser());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        repository.deleteGroup(group1.getIdGroup());
        repository.deleteGroup(group2.getIdGroup());
        repository.deleteGroup(group4.getIdGroup());
        if (user != null)
            repository.deleteTask(user.getIdUser());
        repository.resetIndex();
    }

    @Test
    public void testGetAll() {
        Collection<Group> groups = repository.getAllGroup();

        assertNotNull("The collection of playlists is null", groups);

        // Show result
        System.out.println("Listing all playlists:");
        int i = 1;
        for (Group group : groups) {
            System.out.println("Playlist " + i++ + " : " + group.getName() + " (ID=" + group.getIdGroup() + ")");
        }

    }

    @Test
    public void testGetGroup() {
        Group g = repository.getGroup(group1.getIdGroup());

        assertEquals("The id of the playlists do not match", group1.getIdGroup(), g.getIdGroup());
        assertEquals("The name of the playlists do not match", group1.getName(), g.getName());

        // Show result
        System.out.println("Playlist id: " + g.getIdGroup());
        System.out.println("Playlist name: " + g.getName());

    }

    @Test
    public void testAddGroup() {


        String name = "Add user test name";
        String description = "Add user test surname";
        Integer numTask = 2;
        Date createDate= Date.valueOf("2022-12-22");
        

        repository.addGroup(Group.of(name, description, numTask, createDate));
        group4 = repository.getGroup("G3");

        assertNotNull("Error when adding the playlist", group4);
        assertEquals("The playlist's name has not been setted correctly", name, group4.getName());
        assertEquals("The playlist's description has not been setted corretly", description, group4.getDescription());
        assertEquals("The playlist's name has not been setted correctly", numTask, group4.getNumTasks());
        assertEquals("The playlist's description has not been setted corretly", createDate, group4.getCreatedDate());
       }

    @Test
    public void testUpdateGroup() {
        String name = "Updated group name";

        // Update playlist
        group1.setName(name);

        repository.updateGroup(group1);
        Group group = repository.getGroup(group1.getIdGroup());

        assertEquals("The playlist's name has not been updated correctly", name, group.getName());
    }

    @Test
    public void testDeletePlaylist() {
        repository.deleteGroup(group2.getIdGroup());

        Group group = repository.getGroup(group2.getIdGroup());
        assertNull("The playlist has not been deleted correctly", user);
    }

    @Test
    public void testAddUser() {
        if (user != null) {
            repository.addUserToGroup(group3.getIdGroup(), user.getIdUser());
            Group group = repository.getGroup(group3.getIdGroup());
            assertEquals("The task has been added correctly", user.getIdUser(), group.getUsers().get(0).getIdUser());
        }
    }

    @Test
    public void testRemoveUser() {
        if (user != null) {
            repository.deleteUserToGroup(group3.getIdGroup(), user.getIdUser());
            Group group = repository.getGroup(group3.getIdGroup());
            assertEquals("The task has been removed correctly", 0, group.getUsers().size());
        }
    }
    */

}
