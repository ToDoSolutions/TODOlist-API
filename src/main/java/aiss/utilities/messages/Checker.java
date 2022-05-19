package aiss.utilities.messages;

import aiss.model.*;
import aiss.utilities.Pair;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

import javax.ws.rs.core.Response;
import java.sql.Date;

public class Checker {

    public static void isTaskCorrect(Task task, ControllerResponse controller) {

        // Comprobamos formato de fecha de inicio.
        isDateCorrect(task.getStartDateString(), controller);

        // Comprobamos formato de fecha de fin.
        isDateCorrect(task.getFinishedDateString(), controller);
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (task.getTitle() != null && task.getTitle().length() > 50)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The title is too long")));
        else if (task.getDescription() != null && task.getDescription().length() > 200)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The description of the task must be less than 200 characters and it's " + task.getDescription().length())));
        else if (task.getStatus() != null && task.getStatus() != Status.DRAFT && task.getStatus() != Status.IN_PROGRESS &&
                task.getStatus() != Status.DONE && task.getStatus() != Status.IN_REVISION && task.getStatus() != Status.CANCELLED)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The status of the task must be one of the following: DRAFT, IN_PROGRESS, DONE, IN_REVISION, CANCELLED")));
        else if (task.getFinishedDate() != null && task.getStartDate() != null && task.getDuration() < 0)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The duration of the task must be a positive number and it's " + task.getDuration())));
        else if (task.getAnnotation() != null && task.getAnnotation().length() > 50)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The annotation of the task must be less than 50 characters and it's " + task.getAnnotation().length())));
        else if (task.getPriority() != null && (task.getPriority() < 0 || task.getPriority() > 5))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The priority of the task is not valid, it must be a number between 0 and 5 and it's " + task.getPriority())));
        else if (task.getDifficulty() != null)
            auxIsDifficultyCorrect(task.getDifficulty(), controller);
    }

    public static void isUserCorrect(User user, ControllerResponse controller) {
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (user.getEmail() != null && !EmailValidator.getInstance().isValid(user.getEmail()))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The email of the user is not valid")));
        else if (user.getName() != null && user.getName().length() > 50)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the user must be less than 50 characters and it's " + user.getName().length())));
        else if (user.getSurname() != null && user.getSurname().length() > 50)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The surname of the user must be less than 50 characters and it's " + user.getSurname().length())));
        else if (user.getAvatar() != null && !UrlValidator.getInstance().isValid(user.getAvatar()))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The avatar of the user is not valid")));
        else if (user.getBio() != null && user.getBio().length() > 500)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The bio of the user must be less than 500 characters and it's " + user.getBio().length())));
        else if (user.getLocation() != null && user.getLocation().length() > 50)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The location of the user must be less than 50 characters and it's " + user.getLocation().length())));
    }

    public static void isGroupCorrect(Group group, ControllerResponse controller) {
        // Comprobamos formato de fecha de inicio.
        isDateCorrect(group.getCreatedDateString(), controller);
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (group.getName() != null && group.getName().length() > 50)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The name of the group must be less than 50 characters and it's " + group.getName().length())));
        else if (group.getDescription() != null && group.getDescription().length() > 200)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The description of the group must be less than 200 characters and it's " + group.getDescription().length())));
        else if (group.getCreatedDate() != null && group.getCreatedDate().before(new Date(0)))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The created date must be before the current date ant it's " + group.getCreatedDate())));
    }

    public static void isRepoCorrect(Task repo, ControllerResponse controller) {

        isDateCorrect(repo.getFinishedDateString(), controller);
        if (Boolean.TRUE.equals(controller.hasError())) {
        } else if (repo.getFinishedDate() != null && repo.getFinishedDate().before(new Date(0)))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The finished date must be before the current date ant it's " + repo.getFinishedDate())));
        else if (repo.getPriority() != null && (repo.getPriority() < 0 || repo.getPriority() > 5))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The priority of the task is not valid, it must be a number between 0 and 5 and it's " + repo.getPriority())));
        else if (repo.getStatus() != null && repo.getStatus() != Status.DRAFT && repo.getStatus() != Status.IN_PROGRESS &&
                repo.getStatus() != Status.DONE && repo.getStatus() != Status.IN_REVISION && repo.getStatus() != Status.CANCELLED)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The status of the task must be one of the following: DRAFT, IN_PROGRESS, DONE, IN_REVISION, CANCELLED")));
        else if (repo.getDifficulty() != null)
            auxIsDifficultyCorrect(repo.getDifficulty(), controller);
    }

    private static void auxIsDifficultyCorrect(Difficulty difficulty, ControllerResponse controller) {
        if (difficulty != Difficulty.EASY &&
                difficulty != Difficulty.MEDIUM && difficulty != Difficulty.HARD &&
                difficulty != Difficulty.HARDCORE && difficulty != Difficulty.I_WANT_TO_DIE)
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The difficulty of the task must be one of the following: EASY, MEDIUM, HARD, HARDCORE, I_WANT_TO_DIE")));
    }

    public static Date isDateCorrect(String date, ControllerResponse controller) {
        if (date != null && !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
        	controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The date is not valid, it should be yyyy-MM-dd format")));
        	return null;
        } else if (date == null)
        	return null;
            
        return Date.valueOf(date);
    }
    
    public static Status isStatusCorrect(String status, ControllerResponse controller) {
    	Status auxStatus = null;
        if (status != null) {
            auxStatus = Status.parse(status);
            if (auxStatus == null)  controller.addError(Message.send(Response.Status.BAD_REQUEST, Pair.of("status: ", "400"),
                    Pair.of("message: ", "The status is not correct, it must be one of the following: DRAFT, IN_PROGRESS, IN_REVIEW, DONE, CANCELLED")));
        }
        return auxStatus;
    }
    
    public static Difficulty isDifficultyCorrect(String status, ControllerResponse controller) {
    	Difficulty auxDifficulty = null;
        if (status != null) {
            auxDifficulty = Difficulty.parse(status);
            if (auxDifficulty == null)  controller.addError(Message.send(Response.Status.BAD_REQUEST, Pair.of("status: ", "400"),
                    Pair.of("message: ", "The difficulty is not correct, it must be one of the following: EASY, MEDIUM, HARD, HARDCORE, I_WANT_TO_DIE")));
        }
        return auxDifficulty;
    }

    public static void isParamGELNumber(String param, ControllerResponse controller) {
        if (param != null && !param.matches("[<>=]{2}\\d+") && !param.matches("[<>=]\\d+"))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The parameter is not valid, does not have a number or it must have one or two of the following characters: =, <, >")));
    }

    public static void isParamGELDate(String param, ControllerResponse controller) {
        if (param != null && !param.matches("[<>=]{2}\\d{4}-\\d{2}-\\d{2}")
                && !param.matches("[<>=]\\d{4}-\\d{2}-\\d{2}"))
            controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The parameter is not valid, does not have a date or it must have one or two of the following characters: =, <, >")));
    }
    
    public static Integer isNumberCorrect(String number, ControllerResponse controller) {
    	if (number != null && !number.matches("\\d+") && !number.matches("-\\d+") ) {
    		controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "You must introduce a number and it's " + number)));
    		return null;
    	} else if (number == null)
    		return null;
    		
    	
    	return Integer.parseInt(number);
    }
    
    public static void isPriorityCorrect(Integer priority, ControllerResponse controller) {
    	
    	if (Boolean.TRUE.equals(controller.hasError())) {	
    	} else if (priority != null && (priority < 0 || priority > 5)) 
    		controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The priority must be between 0 and 5 and it's " + priority)));
    		
	}
	
    public static void isAfter(Date start, Date finish, ControllerResponse controller) {
		if (Boolean.TRUE.equals(controller.hasError())) {	
    	} else if (finish != null && start != null && start.after(finish))
    		controller.addError(Message.send(Response.Status.BAD_REQUEST,
                    Pair.of("status", "400"),
                    Pair.of("message", "The finish date is " + finish + " and it must be after " + start)));
		
	}
    
    private Checker() {
    }

	
}
