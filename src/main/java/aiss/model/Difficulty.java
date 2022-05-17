package aiss.model;

public enum Difficulty {
    SLEEP,
    EASY,
    MEDIUM,
    HARD,
    HARDCORE,
    I_WANT_TO_DIE;

    public static Difficulty parse(String difficulty) {
        String difficultyLowerCase = difficulty.toLowerCase();
        if (difficultyLowerCase.equals("sleep"))
            return SLEEP;
        else if (difficultyLowerCase.equals("easy"))
            return EASY;
        else if (difficultyLowerCase.equals("medium"))
            return MEDIUM;
        else if (difficultyLowerCase.equals("hard"))
            return HARD;
        else if (difficultyLowerCase.equals("hardcore"))
            return HARDCORE;
        else if (difficultyLowerCase.equals("i_want_to_die") || difficulty.equals("i want to die"))
            return I_WANT_TO_DIE;
        else
            return null;
    }
}
