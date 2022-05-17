package aiss.utilities;

public class Pair {
    private String a;
    private String b;

    public Pair(String name, String value) {
        this.a = name;
        this.b = value;
    }

    public static Pair of(String a, String b) {
        return new Pair(a, b);
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    private Pair() {
    }
}
