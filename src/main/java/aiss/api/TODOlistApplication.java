package aiss.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class TODOlistApplication extends Application {
	private Set<Object> singletons = new HashSet<>();

	// He quitado classes por que no parece útil.

	// Loads all resources that are implemented in the application
	// so that they can be found by RESTEasy.
	public TODOlistApplication() {
		// singletons.add(Class.getInstance());
	}


	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
