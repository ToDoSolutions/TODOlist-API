package aiss.model;

public enum Difficulty {
    SLEEP,
    EASY,
    MEDIUM,
    HARD,
    HARDCORE,
    I_WANT_TO_DIE;

    public static Difficulty parse(String difficulty) {
        if (difficulty.equals("sleep"))
            return SLEEP;
        else if (difficulty.equals("easy"))
            return EASY;
        else if (difficulty.equals("medium"))
            return MEDIUM;
        else if (difficulty.equals("hard"))
            return HARD;
        else if (difficulty.equals("hardcore"))
            return HARDCORE;
        else if (difficulty.equals("i_want_to_die") || difficulty.equals("i want to die"))
            return I_WANT_TO_DIE;
        else
        	return null;
    }
}
