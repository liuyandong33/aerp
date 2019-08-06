package build.dream.aerp.domains;

public class Config {
    public static final String TABLE_NAME = "config";
    private Long id;
    private String name;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public static class Builder {
        private final Config instance = new Config();

        public Builder id(Long id) {
            instance.setId(id);
            return this;
        }

        public Builder name(String name) {
            instance.setName(name);
            return this;
        }

        public Builder value(String value) {
            instance.setValue(value);
            return this;
        }

        public Config build() {
            Config config = new Config();
            config.setId(instance.getId());
            config.setName(instance.getName());
            config.setValue(instance.getValue());
            return config;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class ColumnName {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String VALUE = "value";
    }

    public static class FieldName {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String VALUE = "value";
    }
}
