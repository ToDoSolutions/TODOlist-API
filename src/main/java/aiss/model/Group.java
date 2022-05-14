package aiss.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Group {
	
	// Para el getFields.
    public static final String ALL_ATTRIBUTES = "idUser,name,surname,email,avatar,bio,location,taskCompleted,tasks";

    // Atributos de la clase.
    private String idGroup, name, description;
    private Integer numTasks;
    private Date createdDate;
    private List<User> users;
    
    // Constructor, crear nueva clase para disminuir parámetros (ni idea).
    public Group(String name, String description, Date createdDate, Integer numTasks) {
        this.idGroup = null;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.numTasks = numTasks;
        this.users = new ArrayList<>();
    }
    
    private Group() {
    }
    
    public static Group of(String name, String description, Date createdDate, Integer numTasks) {
        return new Group(name, description, createdDate, numTasks);
    }

    // Getter y setters.
	public String getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(String idGroup) {
		this.idGroup = idGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNumTasks() {
		return numTasks;
	}

	public void setNumTasks(Integer numTasks) {
		this.numTasks = numTasks;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	 public User getUser(String id) {
		 if(users == null)
			 return null;
		 User user = null;
		 for(User u : users) {
			 if(u.getIdUser().equals(id)) {
				 user = u;
				 break;
			 }
		 }
		 return user;
	 }
	 
	 // Para modificar Users.
	 public void addUser(User u) {
		 if(users == null)
			 users = new ArrayList<>();
		 users.add(u);
	 }

	 public void deleteUser(User u) {
		 users.remove(u);
	 }

	 public void deleteUser(String id) {
		 User u = getUser(id);
		 if(u != null)
			 users.remove(u);
	 }

	@Override
	public String toString() {
		return "Group [idGroup=" + idGroup + ", name=" + name + ", description=" + description + ", numTasks="
				+ numTasks + ", createdDate=" + createdDate + ", users=" + users + "]";
	}
	 
	public Map<String, Object> getFields(String fieldsGroup, String fieldsUser, String fieldsTask) {
        List<String> attributesShown = Stream.of(fieldsGroup.split(",")).map(String::trim).collect(Collectors.toList());
        Map<String, Object> map = new TreeMap<>();
        for (String attribute : attributesShown) {
            if (Objects.equals(attribute, "idGroup"))
                map.put(attribute, getIdGroup());
            else if (Objects.equals(attribute, "name"))
                map.put(attribute, getName());
            else if (Objects.equals(attribute, "description"))
                map.put(attribute, getDescription());
            else if (Objects.equals(attribute, "createdDate"))
                map.put(attribute, getCreatedDate());
            else if (Objects.equals(attribute, "numTasks"))
                map.put(attribute, getNumTasks());
            else if (Objects.equals(attribute, "users"))
                map.put(attribute, getUsers().stream().map(u -> u.getFields(fieldsUser == null ? User.ALL_ATTRIBUTES : fieldsUser, fieldsTask)).collect(Collectors.toList()));
        }
        return map;
    }
    

}
