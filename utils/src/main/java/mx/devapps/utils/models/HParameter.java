package mx.devapps.utils.models;

public class HParameter {

    public final String name;

    public final String value;

    public HParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
