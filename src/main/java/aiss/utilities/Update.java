package aiss.utilities;

import aiss.model.Group;
import aiss.model.Task;
import aiss.model.User;

public class Update {

    public static void taskFromOther(Task task, Task oldTask) {
        if (task.getTitle() != null)
            oldTask.setTitle(task.getTitle());
        if (task.getDescription() != null)
            oldTask.setDescription(task.getDescription());
        if (task.getStatus() != null)
            oldTask.setStatus(task.getStatus());
        if (task.getFinishedDate() != null)
            oldTask.setFinishedDate(task.getFinishedDateString());
        if (task.getStartDate() != null)
            oldTask.setReleaseDate(task.getStartDateString());
        if (task.getAnnotation() != null)
            oldTask.setAnnotation(task.getAnnotation());
        if (task.getPriority() != null)
            oldTask.setPriority(task.getPriority());
        if (task.getDifficulty() != null)
            oldTask.setDifficulty(task.getDifficulty());
    }

    public static void userFromOther(User user, User oldUser) {
        // Update name.
        if (user.getName() != null)
            oldUser.setName(user.getName());
        // Update surname.
        if (user.getSurname() != null)
            oldUser.setSurname(user.getSurname());
        // Update email.
        if (user.getEmail() != null)
            oldUser.setEmail(user.getEmail());
        // Update avatar.
        if (user.getAvatar() != null)
            oldUser.setAvatar(user.getAvatar());
        // Update bio.
        if (user.getBio() != null)
            oldUser.setBio(user.getBio());
        // Update location.
        if (user.getLocation() != null)
            oldUser.setLocation(user.getLocation());
    }

    public static void groupFromOther(Group group, Group oldGroup) {
        // Update name.
        if (group.getName() != null)
            oldGroup.setName(group.getName());
        // Update description.
        if (group.getDescription() != null)
            oldGroup.setDescription(group.getDescription());
        // Update avatar.
        if (group.getCreatedDate() != null)
            oldGroup.setCreatedDate(group.getCreatedDateString());
    }

    private Update() {
    }
}
