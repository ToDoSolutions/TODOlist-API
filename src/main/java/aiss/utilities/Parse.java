package aiss.utilities;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.github.Owner;
import aiss.model.github.TaskGitHub;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Parse {

    public static User userFromGitHub(Owner owner) {
        Map<String, Object> additional = owner.getAdditionalProperties();
        Object auxName = additional.get("name");
        List<String> fullName;
        String name = null;
        String surname = null;
        if (auxName != null) {
            fullName = Arrays.asList(additional.get("name").toString().split(" "));
            name = fullName.get(0);
            surname = fullName.size() == 1 ? null : fullName.stream().skip(1).reduce("", (ac, nx) -> ac + " " + nx);
        }
        String email = getAdditional(additional, "email");
        String bio = getAdditional(additional, "bio");
        String location = getAdditional(additional, "location");
        return User.of(name, surname, email, owner.getAvatarUrl(), bio, location);
    }

    public static Task taskFromGitHub(TaskGitHub repo, String status, String finishedDate, Integer priority, String difficulty) {
        Status auxStatus = status == null ? null : Status.parse(status);
        Date auxFinishedDate = finishedDate == null ? null : Date.valueOf(finishedDate);
        Difficulty auxDifficulty = difficulty == null ? null : Difficulty.valueOf(difficulty);
        Object language = repo.getLanguage();
        return Task.of(
                repo.getName(),
                repo.getDescription(),
                auxStatus,
                Date.valueOf(repo.getCreatedAt().split("T")[0]),
                auxFinishedDate,
                language == null ? null : language.toString(),
                priority,
                auxDifficulty);
    }

    private static String getAdditional(Map<String, Object> additional, String key) {
        Object aux = additional.get(key);
        return aux == null ? null : aux.toString();
    }
}
