package aiss.model.repository;

import aiss.model.*;
import aiss.model.github.Owner;
import aiss.model.github.TaskGitHub;
import aiss.model.pokemon.Pokemon;
import org.restlet.resource.ClientResource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapRepository implements Repository {

    private static MapRepository instance = null;
    // Añadir los map para cada modelo.
    Map<String, Task> taskMap; // Contiene los datos de las Tasks.
    Map<String, User> userMap; // Contiene los datos de los Users.
    Map<String, Group> groupMap; // Contiene los datos de los grupos.
    private int indexTask = 0;
    private int indexUser = 0;
    private int indexGroup = 0;

    public static Repository getInstance() {
        // Si es null, entendemos que es la primera vez que se ejecuta este método.
        if (instance == null) {
            instance = new MapRepository(); // Creamos una instancia de la clase.
            instance.init(); // Cargamos los datos.
        }
        return instance; // Devolvemos la instancia.
    }

    private void init() {
        // Los map necesarios para cada modelo.
        groupMap = new HashMap<>();
        userMap = new HashMap<>();
        taskMap = new HashMap<>();

        // Create task
        Task t1 = Task.of("Vacaciones", "Quiero vacaciones", Status.DRAFT, "2020-01-01", "2020-01-31", "Vacaciones", 5, Difficulty.HARDCORE);
        Task t2 = Task.of("Bronce", "Salir de bronce en el lol", Status.IN_PROGRESS, "2021-01-01", "2022-12-15", "Quiero subir o mantenerme no bajar a hierro", 5, Difficulty.I_WANT_TO_DIE);
        Task t3 = Task.of("Aceitunas", "Comprar aceitunas sin hueso", Status.IN_PROGRESS, "2022-05-19", "2022-05-29", "Sin hueso pero con pepinillo", 2, Difficulty.MEDIUM);
        Task t4 = Task.of("VIVA ER BETIS", "Ver el betis", Status.DONE, "2022-08-07", "2022-08-08", "err Betiss", 5, Difficulty.EASY);
        Task t5 = Task.of("Entrenador Pokemon", "Completar la pokedex para el profesor Oak", Status.IN_REVISION, "2021-05-17", "2022-11-30", "Hazte con todos, los 892...", 3, Difficulty.I_WANT_TO_DIE);
        Task t6 = Task.of("Comprar mando nuevo", "Nuevo mando para jugar elden ring", Status.DRAFT, "2022-01-19", "2022-06-06", "Comprar uno a prueba de enfados", 4, Difficulty.MEDIUM);
        Task t7 = Task.of("¿Aprender Inglés? Na", "El inglés se enseña mal y punto", Status.CANCELLED, "2020-04-22", "2023-05-26", "Como aprendo el inglés si ni se el español", 0, Difficulty.HARDCORE);

        addTasks(t1, t2, t3, t4, t5, t6, t7);


        // Aquí yace la vista de laura que no se ha logueado con la cuenta correcta en Gooogle CLooud y puede que sea baneada.

        // Create user
        User u1 = User.of("Misco", "Jones", "miscosama@gmail.com", "https://es.web.img3.acsta.net/pictures/17/05/19/13/05/463219.jpg", "Ser celestial, nacido para ayudar", "mi casa");
        User u2 = User.of("El Pelón", "Calvo", "niunpelotonto@tortilla.ong", "http://pm1.narvii.com/6120/9cd70762280f430ded8158c06c287e82b84d0101_00.jpg", "Nacío en un día en el que el sol brillo de tal manera que dislumbró a los imples mortales", "3000 viviendas");
        User u3 = User.of("Yonatan", "Yostar", "jojito@gmail.com", "https://i.pinimg.com/originals/09/52/27/095227e83b41e44b8de3ba8e81efe2e1.jpg", "Solamente defender al mundo del caos", "La Tierra");
        User u4 = User.of("Kaeya", "Alberich", "tucopito@hotmal.com", "https://img-17.ccm2.net/M5IDYIxs4R9RmHBLCt9l-PWqYLc=/500x/eff2597a02394167920c9d1cf7945a3c/ccm-faq/C3.JPG", "Kaeya Alberich es el hijo adoptivo de los Ragnvindr, una familia magnate con muchas bodegas", "Khaenri'ah");
        User u5 = User.of("Aurelion", "Sol", "ElForjadorDeLasEstrellas@riot.com", "https://static.wikia.nocookie.net/yugiohenespanol/images/c/c4/Drag%C3%B3n_c%C3%B3smico_blazar.jpg/revision/latest/scale-to-width-down/1200?cb=20200201203300&path-prefix=es", "El ao shin que nunca salió", "En el espacio picha");
        User u6 = User.of("Paquito", "El Chocolatero", "kingafrica@us.es", "https://pbs.twimg.com/media/FNgG3rCXEAEng6B.jpg", "Fui sifu de Willy Wonka, el sabe todo gracias a mí", "ESPAÑA");

        addUsers(u1, u2, u3, u4, u5, u6);

        // Create group
        Group g1 = Group.of("Pepe", "Solo quieren ver el mundo arder", "2006-10-12");
        Group g2 = Group.of("Otakus", "Dicen que su factura del agua es negativa", "2022-05-06");
        Group g3 = Group.of("AISS enjoyers", "Se dice que son seres que existen desde el inicio de los multiversos", "2000-03-09");

        addGroups(g1, g2, g3);


        // Si uno de los modelos es contenedor de otro.
        addTaskToUser(u1.getIdUser(), t1.getIdTask());
        addTaskToUser(u2.getIdUser(), t2.getIdTask());
        addTaskToUser(u3.getIdUser(), t3.getIdTask());
        addTaskToUser(u4.getIdUser(), t4.getIdTask());
        addTaskToUser(u5.getIdUser(), t5.getIdTask());
        addTaskToUser(u6.getIdUser(), t6.getIdTask());
        addTaskToUser(u6.getIdUser(), t7.getIdTask());

        addUserToGroup(g1.getIdGroup(), u1.getIdUser());
        addUserToGroup(g1.getIdGroup(), u2.getIdUser());
        addUserToGroup(g2.getIdGroup(), u3.getIdUser());
        addUserToGroup(g2.getIdGroup(), u4.getIdUser());
        addUserToGroup(g3.getIdGroup(), u5.getIdUser());
        addUserToGroup(g3.getIdGroup(), u6.getIdUser());
    }

    // Para task.
    @Override
    public Collection<Task> getAllTask() {
        return taskMap.values();
    }

    @Override
    public Task getTask(String idTask) {
        return taskMap.get(idTask);
    }


    private void addTasks(Task... tasks) {
        for (Task task : tasks)
            addTask(task);
    }

    @Override
    public void addTask(Task task) {
        // Para el identificador de Tareas.
        String wordTasks = "T";
        String id = wordTasks + indexTask++;
        task.setIdTask(id);
        taskMap.put(id, task);
    }

    @Override
    public void updateTask(Task t) {
        // Task task = taskMap.get(t.getIdTask());
        taskMap.put(t.getIdTask(), t);
    }

    @Override
    public void deleteTask(String idTask) {
        taskMap.remove(idTask);
    }

    // Para user.
    @Override
    public Collection<User> getAllUser() {
        return userMap.values();
    }

    @Override
    public User getUser(String idUser) {
        return userMap.get(idUser);
    }


    private void addUsers(User... users) {
        for (User user : users)
            addUser(user);
    }

    @Override
    public void addUser(User user) {
        // Para el identificador de Usuarios.
        String wordUsers = "U";
        String id = wordUsers + indexUser++;
        user.setIdUser(id);
        userMap.put(id, user);
    }

    @Override
    public void updateUser(User user) {
        userMap.put(user.getIdUser(), user);
    }

    @Override
    public void deleteUser(String idUser) {
        userMap.remove(idUser);
    }

    @Override
    public Collection<Task> getAllTask(String idUser) {
        return userMap.get(idUser).getTasks();
    }

    @Override
    public void addTaskToUser(String idUser, String idTask) {
        userMap.get(idUser).addTask(taskMap.get(idTask));
    }

    @Override
    public void deleteTaskToOrder(String idUser, String idTask) {
        userMap.get(idUser).deleteTask(taskMap.get(idTask));
    }

    // Para Group.
    @Override
    public Collection<Group> getAllGroup() {
        return groupMap.values();
    }

    @Override
    public Group getGroup(String idGroup) {
        return groupMap.get(idGroup);
    }

    private void addGroups(Group... groups) {
        for (Group group : groups)
            addGroup(group);
    }

    @Override
    public void addGroup(Group group) {
        // Para el identificador de Grupos.
        String wordGroups = "G";
        String id = wordGroups + indexGroup++;
        group.setIdGroup(id);
        groupMap.put(id, group);
    }

    @Override
    public void updateGroup(Group group) {
        groupMap.put(group.getIdGroup(), group);
    }

    @Override
    public void deleteGroup(String idGroup) {
        groupMap.remove(idGroup);
    }

    @Override
    public Collection<User> getAllUser(String idGroup) {
        return groupMap.get(idGroup).getUsers();
    }

    @Override
    public void addUserToGroup(String idGroup, String idUser) {
        groupMap.get(idGroup).addUser(userMap.get(idUser));
    }

    @Override
    public void deleteUserToGroup(String idGroup, String idUser) {
        groupMap.get(idGroup).deleteUser(userMap.get(idUser));
    }

    @Override
    public void addTaskToGroup(String idGroup, String idTask) {
        groupMap.get(idGroup).addTask(taskMap.get(idTask));
    }

    @Override
    public void deleteTaskToGroup(String idGroup, String idTask) {
        groupMap.get(idGroup).deleteTask(taskMap.get(idTask));
    }

    // Para GitHub.
    @Override
    public TaskGitHub getRepo(String account, String repo) {
        try {
            account = URLEncoder.encode(account, "UTF-8");
            repo = URLEncoder.encode(repo, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String uri = "https://api.github.com/repos/" + account + "/" + repo;
        // Logger.getLogger(MapRepository.class.getName()).log(Level.FINE, "TASK URI: " + uri);
        ClientResource cr = new ClientResource(uri);
        return cr.get(TaskGitHub.class);
    }

    @Override
    public Owner getOwner(String account) {
        try {
            account = URLEncoder.encode(account, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String uri = "https://api.github.com/users/" + account;
        // Logger.getLogger(MapRepository.class.getName()).log(Level.FINE, "TASK URI: " + uri);
        ClientResource cr = new ClientResource(uri);
        return cr.get(Owner.class);
    }
    
    @Override 
    public TaskGitHub[] getAllRepos(String account) {
    	Owner owner = getOwner(account);
    	ClientResource cr = new ClientResource(owner.getReposUrl());
    	return cr.get(TaskGitHub[].class);
    }

    // Para pokemon.
    @Override
    public Pokemon getPokemon(String name) {
        try {
            name = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String uri = "https://pokemonapiaiss.lm.r.appspot.com/api/pokemons/" + name;
        // Logger.getLogger(MapRepository.class.getName()).log(Level.FINE, "TASK URI: " + uri);
        ClientResource cr = new ClientResource(uri);
        return cr.get(Pokemon.class);
    }
    
    @Override
    public Pokemon[] getAllPokemons() {
        String uri = "https://pokemonapiaiss.lm.r.appspot.com/api/pokemons/";
        // Logger.getLogger(MapRepository.class.getName()).log(Level.FINE, "TASK URI: " + uri);
        ClientResource cr = new ClientResource(uri);
        return cr.get(Pokemon[].class);
    }

    public void resetIndex() {
        indexUser = 0;
        indexTask = 0;
    }

}
