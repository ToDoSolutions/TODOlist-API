package aiss.utilities;

import aiss.model.Difficulty;
import aiss.model.Status;
import aiss.model.Task;
import aiss.model.User;
import aiss.model.github.Owner;
import aiss.model.github.TaskGitHub;
import aiss.model.pokemon.Pokemon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public static Task taskFromGitHub(TaskGitHub repo, Status status, String finishedDate, Integer priority, Difficulty difficulty) {

        Object language = repo.getLanguage();
        return Task.of(
                repo.getName(),
                repo.getDescription(),
                status,
                repo.getCreatedAt().split("T")[0],
                finishedDate,
                language == null ? null : language.toString(),
                priority,
                difficulty);
    }

    private static String getAdditional(Map<String, Object> additional, String key) {
        Object aux = additional.get(key);
        return aux == null ? null : aux.toString();
    }

    public static Task taskFromPokemon(Pokemon pokemon, Status status, String finishedDate, String priority) {
        Integer auxPriority = priority == null ? null : Integer.parseInt(priority);
        String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Task.of(
                "Catch " + pokemon.getName(),
                "The pokemon is type " + (pokemon.getType2() == null ? pokemon.getType1() : pokemon.getType1() + " and " + pokemon.getType2()),
                status,
                startDate,
                finishedDate,
                getPokemonAnnotation(pokemon),
                auxPriority,
                getPokemonDifficulty(pokemon));
    }

    private static String getPokemonAnnotation(Pokemon pokemon) {
        if (pokemon.getLegend()) {
            return "Be careful, you will need a masterball!!!";
        } else {
            if (pokemon.getLegend()) {
                return "easy peasy lemon squeezy, take one pokeball";
            } else if (getAvgStats(pokemon) < 100) {
                return "mmmh, you will nead some pokeballs";
            } else if (getAvgStats(pokemon) < 150) {
                return "uffff, you must take a great a amount of superballs";
            } else if (getAvgStats(pokemon) < 200) {
                return "Yisus, if you do not catch dozens of super balls, you will not be able to catch it.";
            } else {
                return "LMFAO, take the entire Pokemon Center in your bag";
            }
        }
    }

    private static Difficulty getPokemonDifficulty(Pokemon pokemon) {
        if (pokemon.getLegend()) {
            return Difficulty.I_WANT_TO_DIE;
        } else {
            if (getAvgStats(pokemon) < 100) {
                return Difficulty.EASY;
            } else if (getAvgStats(pokemon) < 150) {
                return Difficulty.MEDIUM;
            } else if (getAvgStats(pokemon) < 200) {
                return Difficulty.HARD;
            } else {
                return Difficulty.HARDCORE;
            }
        }
    }

    private static Integer getAvgStats(Pokemon pokemon) {
        return (pokemon.getHp() + pokemon.getAttack() + pokemon.getDefense()) / 3;
    }

    private Parse() {}
}
