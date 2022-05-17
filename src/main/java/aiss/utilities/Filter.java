package aiss.utilities;

import java.sql.Date;
import java.util.Objects;

public class Filter {

    public static boolean isGEL(Long value, String parameter) {
        String restriccion = String.valueOf(parameter.charAt(0));
        String aux = String.valueOf(parameter.charAt(1));
        String restriccion2 = aux.equals("<") || aux.equals("=") || aux.equals(">") ? aux : null;
        Long num;
        if (restriccion2 == null)
            num = Long.parseLong(parameter.substring(1));
        else {
            num = Long.parseLong(parameter.substring(2));
            restriccion += restriccion2;
        }
        return restriccion.contains("=") && Objects.equals(value, num) ||
                restriccion.contains("<") && value < num ||
                restriccion.contains(">") && value > num;
    }

    public static boolean isGEL(Date value, String parameter) {
        String restriccion = String.valueOf(parameter.charAt(0));
        String aux = String.valueOf(parameter.charAt(1));
        String restriccion2 = aux.equals("<") || aux.equals("=") || aux.equals(">") ? aux : null;
        Date date;
        if (restriccion2 == null)
            date = Date.valueOf(parameter.substring(1));
        else {
            date = Date.valueOf(parameter.substring(2));
            restriccion += restriccion2;
        }
        return restriccion.contains("=") && Objects.equals(value, date) ||
                restriccion.contains("<") && value.before(date) ||
                restriccion.contains(">") && value.after(date);
    }

    private Filter() {
    }
}
