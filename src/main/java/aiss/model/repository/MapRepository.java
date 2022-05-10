package aiss.model.repository;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapRepository implements Repository {

    private String letraTasks ="T";      // Para el identificador de Tareas.
    private String letraUsers ="U";      // Para el identificador de Usuarios.
    private int index=0;			// Índice para creal el identificador de los modelos.

    // Añadir los map para cada modelo.
    Map<String, Task> taskMap; // Contiene los datos de las Tasks.
    Map<String, User> userMap; // Contiene los datos de los Users.
    
    private static MapRepository instance = null;
    
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
        userMap = new HashMap<>();
        taskMap = new HashMap<>();

        // Create task
        Task t1= Task.of("Quiero vacaciones", Status.DRAFT, Date.valueOf("2020-01-01"), Date.valueOf("2020-01-31"), "Vacaciones", 5, Difficulty.HARDCORE);
        addTask(t1);
        
        // Aquí yace la vista de laura que no se ha logueado con la cuenta correcta en Gooogle CLooud y puede que sea baneada.


        // Create user
        User u1 = User.of("Marío", "Zefilio de tos los Santos", "mazetosan@hotmail.com", "un perrete", "no, cono","mi casa");
        addUser(u1);


        // Si uno de los modelos es contenedor de otro.
        // addOther(model.getId(), other.getId());
    }

    // Para task.
    @Override
    public Collection<Task> getAllTask() {
        return null;
    }

    @Override
    public Task getTask(String idTask) {
        return null;
    }

    @Override
    public void addTask(Task task) {

    }

    @Override
    public void updateTask(Task task) {

    }

    @Override
    public void deleteTask(String idTask) {

    }

    // Para user.
    @Override
    public Collection<User> getAllUser() {
        return null;
    }

    @Override
    public User getUser(String idUser) {
        return null;
    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(String idUser) {

    }

    @Override
    public void getAllTask(String idUser) {

    }

    @Override
    public void addTask(String idUser, String idTask) {

    }

    @Override
    public void deleteTask(String idUser, String idTask) {

    }

    // Task functions
}
