package aiss.model.repository;

import aiss.model.Model;
import aiss.model.Task;
import aiss.model.User;
import main.java.common.extension.Map2;

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

    /*
    private void init() {
        // Los map necesarios para cada modelo.
        userMap = Map2.empty();

        // Create task
        t1= new Task();
        addTask(t1);
        
        // Aquí yace la vista de laura que no se ha logueado con la cuenta correcta en Gooogle CLooud y puede que sea baneada.


        // Create user
        u1 = new User();
        addUser(u1);


        // Si uno de los modelos es contenedor de otro.
        // addOther(model.getId(), other.getId());
    }

    //
    @Override
    public Model get(String id) {
        return modelMap.get(id);
    }

    @Override
    public void add(Model model) {
        String id = letra + index++;
        model.setId(id);
        modelMap.put(id, model);
    }

    @Override
    public void delete(String id) {
        modelMap.remove(id);
    }

    @Override
    public Collection<Model> getAll() {
        return modelMap.values();
    }

    @Override
    public void update(Model model) {
           modelMap.put(model.getId(), model);
    }
    */

    // Task functions
}
