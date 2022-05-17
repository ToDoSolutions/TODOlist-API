package aiss.api;

import aiss.api.resources.*;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class TODOlistApplication extends Application {
    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> classes = new HashSet<>();

    // Loads all resources that are implemented in the application
    // so that they can be found by RESTEasy.
    public TODOlistApplication() {
        singletons.add(UserResource.getInstance());
        singletons.add(TaskResource.getInstance());
        singletons.add(GitHubResource.getInstance());
        singletons.add(GroupResource.getInstance());
        singletons.add(PokemonResource.getInstance());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
