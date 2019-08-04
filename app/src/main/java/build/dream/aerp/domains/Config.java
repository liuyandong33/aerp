package build.dream.aerp.domains;

import java.math.BigInteger;

public class Config {
    public static final String TABLE_NAME = "config";
    private BigInteger id;
    private String name;
    private String value;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
